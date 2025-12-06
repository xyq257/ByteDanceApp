package bytedance.app.service.user;

import java.util.Map;

public interface SendEmailService {
    public Map<String, String> sendEmail(String userId);
}
