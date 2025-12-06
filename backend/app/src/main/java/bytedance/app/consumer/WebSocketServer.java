package bytedance.app.consumer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import bytedance.app.consumer.utils.JwtAuthentication;
import bytedance.app.mapper.UserMapper;
import bytedance.app.pojo.Message;
import bytedance.app.pojo.User;
import bytedance.app.service.implement.message.AsyncTaskService;
import bytedance.app.service.message.AddMessageService;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>(); //static 属于类而不属于任何实例
    private User user;
    private Session session = null;
    public static UserMapper userMapper;
    public static AddMessageService addMessageService;
    public static AsyncTaskService asyncTaskService;
    public static volatile boolean webSocketIsOn = false;

    @Autowired //WebSocketServer 是一个 @ServerEndpoint 类，由 WebSocket 容器（如 Tomcat）管理生命周期，而不是 Spring 容器，所以Spring 无法直接注入实例变量
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void SetAddMessageService(AddMessageService addMessageService) {
        WebSocketServer.addMessageService = addMessageService;
    }

    public static void sendMessages(List<Message> messages, String receiver) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", receiver);
        User receiverUser = userMapper.selectOne(queryWrapper);
        Integer userId = receiverUser.getId();
        JSONObject resp = new JSONObject();
        resp.put("data", messages);
        users.get(userId).sendMessage(resp.toJSONString());
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        this.session = session;
        System.out.println("WebSocket opened: " + session.getId());
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        String receiver = this.user.getUsername();
        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
        System.out.println("User found: " + receiver);
        webSocketIsOn = true;
    }

    @OnClose
    public void onClose() {
        System.out.println("WebSocket closed: " + session.getId());
        if (this.user != null) {
            users.remove(this.user.getId());
        }
        webSocketIsOn = false;
    }

    @OnMessage
    public void onMessage(String jsonText, Session session) {
        JSONObject msg = JSON.parseObject(jsonText);   // 用 fastjson 解析
        String content = msg.getString("content");
        String writer = msg.getString("writer");
        String receiver = msg.getString("receiver");
        Date now = new Date();
        addMessageService.addMessage(content, writer, receiver, now);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
