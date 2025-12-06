package bytedance.app.service.message;

import com.alibaba.fastjson2.JSONObject;

public interface GetMessageService {

   JSONObject getMessages(String receiver);
}
