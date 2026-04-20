package customer.log_test.handler;

import com.sap.cds.Result;
import com.sap.cds.services.EventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.request.UserInfo;
import cds.gen.catalogservice.Books_;
import cds.gen.catalogservice.CatalogService_;
import customer.log_test.util.LogUtil;
import customer.log_test.util.TraceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class BooksHandler implements EventHandler {

    @Autowired
    private UserInfo userInfo;

    @Before(event = "*", entity = Books_.CDS_NAME)
    public void beforeBooks(EventContext context) {
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();

        String message = "Books event start"
                + " | event=" + context.getEvent()
                + " | target=" + context.getTarget();

        Object cqn = context.get("cqn");
        if (cqn != null) {
            message += " | cqn=" + cqn;
        }

        LogUtil.log(
                userInfo != null ? userInfo.getName() : "system",
                "log test page",
                className,
                traceId,
                threadName,
                "BK_B_I00001",
                "INFO",
                message
        );
    }

    @After(event = "*", entity = Books_.CDS_NAME)
    public void afterBooks(EventContext context) {
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();

        StringBuilder message = new StringBuilder();
        message.append("Books event end")
               .append(" | event=").append(context.getEvent())
               .append(" | target=").append(context.getTarget());

        Object resultObj = context.get("result");
        if (resultObj != null) {
            message.append(" | result=").append(resultObj);

            if (resultObj instanceof Result result) {
                try {
                    List<Map> books = result.listOf(Map.class);
                    message.append(" | count=").append(books.size());

                    if (!books.isEmpty()) {
                        message.append(" | data=");
                        for (Map book : books) {
                            message.append(book).append(" ; ");
                        }
                    }
                } catch (Exception e) {
                    message.append(" | resultParseError=").append(e.getMessage());
                }
            }
        } else {
            message.append(" | result=null");
        }

        LogUtil.log(
                userInfo != null ? userInfo.getName() : "system",
                "log test page",
                className,
                traceId,
                threadName,
                "BK_B_I00002",
                "INFO",
                message.toString()
        );
    }
}