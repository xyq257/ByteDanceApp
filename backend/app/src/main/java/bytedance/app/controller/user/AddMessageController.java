package bytedance.app.controller.user;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import bytedance.app.service.message.AddMessageService;

@RestController

public class AddMessageController {

    @Autowired
    private AddMessageService addMessageService;

    @PostMapping("/api/addMessage")
    public Map<String, String> addMessage(@RequestBody Map<String, String> map) {
        String content = map.get("content");
        String writer = map.get("writer");
        String receiver = map.get("receiver");
        Date now = new Date();
        return addMessageService.addMessage(content, writer, receiver, now);
    }
}
