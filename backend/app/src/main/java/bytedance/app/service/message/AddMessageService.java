package bytedance.app.service.message;

import java.util.Date;
import java.util.Map;

public interface AddMessageService {
    Map<String, String> addMessage(String content, String writer, String receiver, Date date);
}