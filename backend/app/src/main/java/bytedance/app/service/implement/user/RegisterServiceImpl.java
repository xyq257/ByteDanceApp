package bytedance.app.service.implement.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import bytedance.app.mapper.UserMapper;
import bytedance.app.pojo.User;
import bytedance.app.service.user.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

    //Autowired会找实现了接口的类注入进来
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword, String email, String photo) {
        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("message", "用户名不能为空");
            map.put("status", "fail");
            return map;
        }
        if (password == null || confirmedPassword == null) {
            map.put("message", "密码不能为空");
            map.put("status", "fail");
            return map;
        }

        username = username.trim();
        if (username.length() == 0) {
            map.put("message", "用户名不能为空");
            map.put("status", "fail");
            return map;
        }

        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("message", "密码不能为空");
            map.put("status", "fail");
            return map;
        }

        if (username.length() > 100) {
            map.put("message", "用户名长度不能大于100");
            map.put("status", "fail");
            return map;
        }

        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("message", "密码长度不能大于100");
            map.put("status", "fail");
            return map;
        }

        if (!password.equals(confirmedPassword)) {
            map.put("message", "两次输入的密码不一致");
            map.put("status", "fail");
            return map;
        }

        QueryWrapper<User> qwName = new QueryWrapper<>();
        qwName.eq("username", username);
        List<User> nameList = userMapper.selectList(qwName);
        if (!nameList.isEmpty()) {
            map.put("message", "用户名已存在");
            map.put("status", "fail");
            return map;
        }
        QueryWrapper<User> qwEmail = new QueryWrapper<>();
        qwEmail.eq("email", email);
        List<User> emailList = userMapper.selectList(qwEmail);  
        if (!emailList.isEmpty()) {
            map.put("message", "该邮箱已被注册，如果忘记密码，请点击找回密码");
            map.put("status", "fail");
            return map;
        }
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword, email, photo);
        userMapper.insert(user);
        map.put("status", "success");
        return map;
    }
}
