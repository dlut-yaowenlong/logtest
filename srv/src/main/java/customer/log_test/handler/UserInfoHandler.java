package customer.log_test.handler;

import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import org.springframework.stereotype.Component;
import com.sap.cds.services.request.UserInfo;
import cds.gen.catalogservice.CatalogService_;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class UserInfoHandler implements EventHandler {

    @Autowired
    private UserInfo userInfo;

    @On(event = "getUserInfo")
    public Map<String, Object> onGetUserInfo() {
        System.out.println("getUserInfo called");
        Map<String, Object> result = new HashMap<>();
        System.out.println("userInfo" + userInfo);

        result.put("userId", userInfo.getName());
        result.put("userName", userInfo.getName());
        Object email = userInfo.getAdditionalAttributes() != null
                ? userInfo.getAdditionalAttributes().get("email")
                : null;

        result.put("email", email != null ? email.toString() : "");

       
        return result;
    }
}