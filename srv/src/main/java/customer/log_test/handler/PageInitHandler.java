package customer.log_test.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.request.UserInfo;
import cds.gen.catalogservice.CatalogService_;
import cds.gen.catalogservice.OnInitContext;
import customer.log_test.util.LogUtil;
import customer.log_test.util.TraceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class PageInitHandler implements EventHandler {

    @Autowired
    private UserInfo userInfo;

    @On(event = "onInit")
    public String onInitMethod(OnInitContext context) {
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();

        String pageName = context.getPageName();
        String detail = context.getDetail();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(detail);

            String logLevel = jsonNode.path("logLevel").asText("INFO").toUpperCase();
            String message = jsonNode.path("message").asText("");

            String logId;
            switch (logLevel) {
                case "WARN":
                    logId = "PI_B_W00001";
                    break;
                case "ERROR":
                    logId = "PI_B_E00001";
                    break;
                case "INFO":
                default:
                    logId = "PI_B_I00001";
                    break;
            }

            LogUtil.log(
                    userInfo.getName(),
                    pageName,
                    className,
                    traceId,
                    threadName,
                    logId,
                    logLevel,
                    message
            );

        } catch (Exception e) {
            LogUtil.log(
                    userInfo.getName(),
                    pageName,
                    className,
                    traceId,
                    threadName,
                    "PI_B_E00002",
                    "ERROR",
                    "detail parse failed: " + detail + " , error=" + e.getMessage()
            );
        }

        return "OK";
    }
}