package bytedance.app.service.user;

import java.util.Map;

public interface GetUserInfoService {

    public Map<String, String> getUserInfo(String username);
}
