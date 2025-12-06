package bytedance.app.service.user;

import java.util.Map;

public interface RegisterService {
    public Map<String, String> register(String username, String password, String confirmedPassword, String email, String photo);}