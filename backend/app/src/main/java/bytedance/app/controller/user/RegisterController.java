package bytedance.app.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import bytedance.app.service.user.RegisterService;

@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping("/api/register/")
    public Map<String, String> register(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        String email = map.get("email");
        String photo = map.get("photo");
        return registerService.register(username, password, confirmedPassword, email, photo);
    }
}
