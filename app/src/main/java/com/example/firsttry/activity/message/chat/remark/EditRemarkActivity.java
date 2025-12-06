// Java
package com.example.firsttry.activity.message.chat.remark;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.firsttry.Database.UserDbHelper;
import com.example.firsttry.activity.message.Message;
import com.example.firsttry.R;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class EditRemarkActivity extends AppCompatActivity {

    private EditText etRemark;
    private MaterialButton btnSave;
    private UserDbHelper dbHelper;
    private String conversationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remark);

        etRemark = findViewById(R.id.et_remark);
        btnSave = findViewById(R.id.btn_save_remark);
        dbHelper = UserDbHelper.getInstance(this);

        conversationId = getIntent().getStringExtra("conversationId");
        if (conversationId != null) {
            // 从会话列表中寻找已有的 remark（项目中已有 loadAllConversations）
            List<Message> convs = dbHelper.loadAllConversations();
            for (Message m : convs) {
                if (conversationId.equals(m.getId())) {
                    String r = m.getRemark();
                    if (!TextUtils.isEmpty(r)) etRemark.setText(r);
                    break;
                }
            }
        }

        // 统一保存逻辑：更新 conversations 并在后台更新 user 表
        btnSave.setOnClickListener(v -> {
            String newRemark = etRemark.getText().toString().trim();
            if (conversationId != null) {
                // 更新会话表（主线程）
                dbHelper.saveRemark(conversationId, newRemark);
                // 后台更新 user 表的 remark 字段
                new Thread(() -> {
                    UserDbHelper db = UserDbHelper.getInstance(EditRemarkActivity.this);
                    db.saveUserRemark(conversationId, newRemark);
                }).start();
            }
            Intent result = new Intent();
            result.putExtra("new_remark", newRemark);
            setResult(RESULT_OK, result);
            finish();
        });

    }
}
