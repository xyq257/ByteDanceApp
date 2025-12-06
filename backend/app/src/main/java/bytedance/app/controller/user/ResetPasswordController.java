package bytedance.app.controller.user;

import java.util.Map;

import bytedance.app.service.user.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetPasswordController {
    
    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/api/resetpassword/")
    public Map<String, String> SendCertificationCode(@RequestBody Map<String, String> map){
        String userId = map.get("email");
        String newPassword = map.get("newPassword");
        String confirmedPassword = map.get("confirmedPassword");
        return resetPasswordService.ResetPassword(userId, newPassword, confirmedPassword);
    }
}
