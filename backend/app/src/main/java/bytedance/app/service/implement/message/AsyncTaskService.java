package bytedance.app.service.implement.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import bytedance.app.consumer.WebSocketServer;
import bytedance.app.mapper.UserMapper;
import bytedance.app.pojo.Message;
import bytedance.app.pojo.User;
import bytedance.app.service.message.AddMessageService;

@Service
public class AsyncTaskService {

    @Autowired
    private AddMessageService addMessageService;

    @Autowired
    private UserMapper userMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        addMessageAsync();
    }
    //@PostConstruct会让@Async失效
    @Async
    public void addMessageAsync() {
        while (true) {
            Date date = new Date();
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            String receiver = "wyt";
            userQueryWrapper.ne("username", receiver)
                    .last("ORDER BY RAND() LIMIT 1");
            User user = userMapper.selectOne(userQueryWrapper);
            String writer = user.getUsername();
            try {
                addMessageService.addMessage("我是"+writer, writer, receiver, date);
                List<Message> messages = new ArrayList<>();
                Message message = new Message(null, "我是"+writer, writer, receiver, date);
                messages.add(message);
                if (WebSocketServer.webSocketIsOn) {
                    WebSocketServer.sendMessages(messages, receiver);
                }
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}