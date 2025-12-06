package bytedance.app.service.user;

import java.util.Map;

public interface ResetPasswordService {

    public Map<String, String> ResetPassword(String userId, String newPassword, String confirmedPassword);

}
