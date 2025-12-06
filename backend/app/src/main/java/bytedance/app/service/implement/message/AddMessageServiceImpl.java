package bytedance.app.service.implement.message;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import bytedance.app.pojo.Message;
import bytedance.app.service.message.AddMessageService;
import bytedance.app.mapper.MessageMapper;

@Service
public class AddMessageServiceImpl implements AddMessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Map<String, String> addMessage(String content, String writer, String receiver, Date date) {
        Message message = new Message(null, content, writer, receiver, date);
        messageMapper.insert(message);
        Map<String, String> map = new HashMap<>();
        map.put("status", "success");
        map.put("message", "发送成功");
        return map;
    }
}
