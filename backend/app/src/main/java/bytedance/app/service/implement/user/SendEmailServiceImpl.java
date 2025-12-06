package bytedance.app.service.implement.user;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import bytedance.app.service.user.SendEmailService;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender sender;

    @Override
    public Map<String, String> sendEmail(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        int code = ThreadLocalRandom.current().nextInt(1000, 10000);
        String content = String.valueOf(code);
        msg.setFrom("484424969@qq.com");
        msg.setTo(email);
        msg.setSubject("ByteApp 验证码");
        msg.setText("您的验证码为：" + content + ",有效期为5分钟。");
        Map<String, String> map = new HashMap<>();
        try {
            sender.send(msg);
        } catch (MailException e) {
            map.put("status", "fail");
            return map;
        }
        map.put("status", "success");
        map.put("code", content);
        return map;
    }
}
