package bytedance.app.service.implement.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import bytedance.app.mapper.UserMapper;
import bytedance.app.pojo.User;
import bytedance.app.service.user.ResetPasswordService;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> ResetPassword(String email, String newPassword, String confirmedPassword) {
        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("email", email)
        );
        Map<String, String> map = new HashMap<>();

        if (newPassword == null || confirmedPassword == null) {
            map.put("message", "密码不能为空");
            map.put("status", "fail");
            return map;
        }

        if (newPassword.length() == 0 || confirmedPassword.length() == 0) {
            map.put("message", "密码不能为空");
            map.put("status", "fail");
            return map;
        }

        if (newPassword.length() > 100 || confirmedPassword.length() > 100) {
            map.put("message", "密码长度不能大于100");
            map.put("status", "fail");
            return map;
        }

        if (!newPassword.equals(confirmedPassword)) {
            map.put("message", "两次输入的密码不一致");
            map.put("status", "fail");
            return map;
        }
        if (newPassword.equals(confirmedPassword)) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userMapper.updateById(user);
            map.put("status", "success");
            map.put("message", "密码修改成功");

            return map;
        }
        map.put("message", "未知错误，请联系管理员");
        map.put("status", "fail");
        return map;
    }
}
