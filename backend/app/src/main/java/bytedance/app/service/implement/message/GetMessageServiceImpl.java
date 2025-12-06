package bytedance.app.service.implement.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import bytedance.app.consumer.WebSocketServer;
import bytedance.app.mapper.MessageMapper;
import bytedance.app.pojo.Message;
import bytedance.app.service.message.GetMessageService;

@Service
public class GetMessageServiceImpl implements GetMessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public JSONObject getMessages(String receiver) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.ne("writer", receiver)
                .eq("receiver", receiver)
                .eq("'read'", 0);//read是保留字，需要加引号
        JSONObject res = new JSONObject();
        res.put("data", messageMapper.selectList(wrapper));
        res.put("status", "success"); 
        res.put("message", "获取消息成功");
        WebSocketServer.sendMessages(messageMapper.selectList(wrapper),receiver);
        return res;
    }
}