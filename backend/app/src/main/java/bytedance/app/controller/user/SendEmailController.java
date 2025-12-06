package bytedance.app.controller.user;

import java.util.Map;

import bytedance.app.service.user.SendEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendEmailController {
    
    @Autowired
    private SendEmailService sendEmailService;

    @PostMapping("/api/sendEmail/")
    public Map<String, String> sendEmail(@RequestBody Map<String, String> map){
        String email = map.get("email");
        return sendEmailService.sendEmail(email);
    }
}
