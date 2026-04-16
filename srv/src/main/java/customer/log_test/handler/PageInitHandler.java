package customer.log_test.handler;

import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import customer.log_test.util.LogUtil;
import cds.gen.catalogservice.OnInitContext;
import com.sap.cds.services.request.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
// import com.sap.cds.services.cds.CdsService;
import org.springframework.stereotype.Component;
import cds.gen.catalogservice.CatalogService_;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class PageInitHandler implements EventHandler {

  @Autowired
  private UserInfo userInfo;

  @On(event = "onInit")
  public String onInitMethod(OnInitContext context) {
    // System.out.println("onWriteLog start");
    String pageName = context.getPageName();
    String detail = context.getDetail();

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(detail);

      String logLevel = jsonNode.path("logLevel").asText("INFO");
      String message = jsonNode.path("message").asText("");
      // String screenName = jsonNode.path("screenName").asText("");

      LogUtil.log(userInfo.getName(), pageName,logLevel, message);
    } catch (Exception e) {
      // LogUtil.log(userInfo.getName(), "ERROR", "detail parse failed: " + detail);
      e.printStackTrace();
    }
    return "OK";
  }
}