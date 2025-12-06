package bytedance.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import bytedance.app.pojo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
