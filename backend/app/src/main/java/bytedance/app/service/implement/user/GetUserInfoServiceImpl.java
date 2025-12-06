package bytedance.app.service.implement.user;

import java.util.HashMap;
import java.util.Map;

import bytedance.app.mapper.UserMapper;
import bytedance.app.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import bytedance.app.service.user.GetUserInfoService;

@Service
public class GetUserInfoServiceImpl implements GetUserInfoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    // 暂时不允许账号改名，不用考虑拉不到数据
    public Map<String, String> getUserInfo(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("photo", user.getPhoto());
        return map;
    }

}
