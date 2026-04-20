package customer.log_test.handler;

import com.sap.cds.Result;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.sap.cds.services.request.UserInfo;
import cds.gen.catalogservice.Books_;
import cds.gen.catalogservice.CatalogService_;
import customer.log_test.util.LogUtil;
import customer.log_test.util.TraceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class BooksReadHandler implements EventHandler {

    @Autowired
    private UserInfo userInfo;

    private final PersistenceService db;

    public BooksReadHandler(PersistenceService db) {
        this.db = db;
    }

    @After(event = "READ", entity = Books_.CDS_NAME)
    public void onAfterReadBooks(CdsReadEventContext context, Result result) {
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();

        List<Map> books = result.listOf(Map.class);

        StringBuilder sb = new StringBuilder();
        sb.append("Books READ result count=").append(books.size());

        if (!books.isEmpty()) {
            sb.append(" | data=");
            for (Map book : books) {
                sb.append(book).append(" ; ");
            }
        }

        LogUtil.log(
                userInfo.getName(),
                "log test page",
                className,
                traceId,
                threadName,
                "BK_B_I00002",
                "INFO",
                sb.toString());
    }

    // ===== BEFORE =====
    @Before(event = "READ", entity = Books_.CDS_NAME)
    public void beforeReadBooks(CdsReadEventContext context) {
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();

        LogUtil.log(
                userInfo.getName(),
                "log test page",
                className,
                traceId,
                threadName,
                "BK_B_I00001",
                "INFO",
                "READ Books start | cqn=" + context.getCqn());
    }
    
    // @On(event = "READ", entity = Books_.CDS_NAME)
    // public Result onReadBooks(CdsReadEventContext context) {
    // String traceId = TraceUtil.getOrCreateTraceId(context);
    // String threadName = Thread.currentThread().getName();
    // String className = this.getClass().getSimpleName();

    // try {
    // System.out.println(">>> ON READ Books start");
    // Result result = db.run(context.getCqn());
    // System.out.println("<<< ON READ Books success, rowCount=" +
    // result.rowCount());
    // return result;
    // } catch (Exception e) {
    // LogUtil.log(
    // userInfo.getName(),
    // "log test page",
    // className,
    // traceId,
    // threadName,
    // "BK_B_E00002",
    // "ERROR",
    // "READ Books failed: " + e
    // );
    // throw e;
    // }
    // }
}