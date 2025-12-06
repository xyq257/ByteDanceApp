package bytedance.app.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSONObject;
import bytedance.app.pojo.User;
import bytedance.app.service.implement.user.UserDetailsImpl;
import bytedance.app.service.message.GetMessageService;

@RestController

public class GetMessageController {

    @Autowired
    private GetMessageService getMessageService;

    @PostMapping("/api/getMessages/")
    public JSONObject getMessages(@RequestBody Map<String, String> map) {
        String receiver = map.get("receiver");
        return getMessageService.getMessages(receiver);
    }
}
