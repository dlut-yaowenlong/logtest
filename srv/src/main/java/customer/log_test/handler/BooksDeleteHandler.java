package customer.log_test.handler;

import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.services.cds.CdsDeleteEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.request.UserInfo;
import cds.gen.catalogservice.Books_;
import cds.gen.catalogservice.CatalogService_;
import customer.log_test.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class BooksDeleteHandler implements EventHandler  {

    @Autowired
    private UserInfo userInfo;

    @Before(event = "DELETE", entity = Books_.CDS_NAME)
    public void beforeDeleteBooks(CdsDeleteEventContext context) {
         try {
            CqnAnalyzer analyzer = CqnAnalyzer.create(context.getModel());
            var analysis = analyzer.analyze(context.getCqn());

            Map<String, Object> keys = analysis.targetKeys();
            Object id = keys.get("ID");

            String message = "Deleted record from Books table. ID=" + id;

            LogUtil.log(
                    userInfo.getName(),
                    "log test page",
                    "INFO",
                    message
            );
        } catch (Exception e) {
            LogUtil.log(
                    userInfo.getName(),
                    "log test page",
                    "ERROR",
                    "DELETE key parse failed"
            );
        }
    }
}
