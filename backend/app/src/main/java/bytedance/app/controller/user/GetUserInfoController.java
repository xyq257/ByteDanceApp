package bytedance.app.controller.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import bytedance.app.service.user.GetUserInfoService;

@RestController

public class GetUserInfoController {

    @Autowired
    private GetUserInfoService getUserInfoService;

    @PostMapping("/api/getUserInfo")
    public Map<String, String> getUserInfo(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        return getUserInfoService.getUserInfo(username);
    }
}
