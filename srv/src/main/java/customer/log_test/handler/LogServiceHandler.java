package customer.log_test.handler;

import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;

import cds.gen.catalogservice.WriteLogContext;
import customer.log_test.http.HttpRequestClient;

import org.springframework.beans.factory.annotation.Autowired;
// import com.sap.cds.services.cds.CdsService;
import org.springframework.stereotype.Component;
import cds.gen.catalogservice.CatalogService_;
import java.util.Map;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class LogServiceHandler implements EventHandler {
  @Autowired
  private HttpRequestClient httpRequestClient;

  @On(event = "writeLog")
  public String onWriteLog(WriteLogContext context) {
    System.out.println("onWriteLog start");
    String objectType = context.getObjectType();
    String objectId = context.getObjectId();
    String operation = context.getOperation();
    String detail = context.getDetail();

    System.out.println("writeLog called");
    System.out.println("objectType = " + objectType);
    System.out.println("objectId   = " + objectId);
    System.out.println("operation  = " + operation);
    System.out.println("detail     = " + detail);

    String jsonBody = """
        {
          "objectType": "%s",
          "objectId": "%s",
          "operation": "%s",
          "detail": "%s"
        }
        """.formatted(objectType, objectId, operation, detail);

    String result = httpRequestClient.postJson(
        "https://webhook.site/518642da-44b7-4df4-a4f8-c3b9625539a3",
        jsonBody);

    System.out.println("result: " + result);

    return "OK";
  }
}