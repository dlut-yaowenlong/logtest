package customer.log_test.handler;

import com.sap.cds.Result;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.request.UserInfo;
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
public class CategoriesReadHandler implements EventHandler {

    @Autowired
    private UserInfo userInfo;

    @Before(event = "READ", entity = "CatalogService.Categories")
    public void beforeReadCategories(CdsReadEventContext context) {
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();

        LogUtil.log(
                userInfo != null ? userInfo.getName() : "system",
                "log test page",
                className,
                traceId,
                threadName,
                "CT_B_I00000",
                "INFO",
                "READ Categories start | cqn=" + context.getCqn());
    }

    @After(event = "READ", entity = "CatalogService.Categories")
    public void onReadCategories(CdsReadEventContext context, Result result) {
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();

        List<Map> categories = result.listOf(Map.class);

        String message;
        if (categories == null) {
            message = "Categories read result is null";
        } else {
            StringBuilder sb = new StringBuilder();
            int count = 0;

            for (var row : categories) {
                count++;
                sb.append(row).append(" ; ");
            }

            message = "Categories read count=" + count + " , data=" + sb;
        }

        LogUtil.log(
                userInfo.getName(),
                "log test page",
                className,
                traceId,
                threadName,
                "CT_B_I00001",
                "INFO",
                message);
    }
}