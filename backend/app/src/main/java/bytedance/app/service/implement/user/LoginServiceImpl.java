package bytedance.app.service.implement.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bytedance.app.pojo.User;
import bytedance.app.service.user.LoginService;
import bytedance.app.utils.JwtUtil;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        Map<String, String> map = new HashMap<>();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();
            String jwt = JwtUtil.createJWT(String.valueOf(user.getId()));
            map.put("status", "success");
            map.put("message", jwt); //输入账号密码，返回token
        } catch (BadCredentialsException e) {
            map.put("status", "fail");
            map.put("message", "密码错误");
        } catch (UsernameNotFoundException e) {
            map.put("status", "fail");
            map.put("message", "用户名不存在");
        }
        return map;
    }
}
