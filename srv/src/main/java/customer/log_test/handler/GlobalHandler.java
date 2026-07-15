package customer.log_test.handler;

import com.sap.cds.Result;
import com.sap.cds.services.EventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.request.UserInfo;

import cds.gen.catalogservice.CatalogService_;
import customer.log_test.util.LogUtil;
import customer.log_test.util.ScreenUtil;
import customer.log_test.util.TraceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class GlobalHandler implements EventHandler {

    @Autowired
    private UserInfo userInfo;

    private boolean isDraftInternalEvent(EventContext context) {
        String event = context.getEvent();

        return "DRAFT_READ".equals(event)
                || "ACTIVE_READ".equals(event)
                || "DRAFT_CREATE".equals(event)
                // || "DRAFT_PATCH".equals(event) //修正項目確認
                || "DRAFT_CANCEL".equals(event)
                || "DRAFT_GC".equals(event)
                || "draftEdit".equals(event)
                || "draftPrepare".equals(event)
                || "draftActivate".equals(event);
    }

    @Before(event = "*", entity = "*")
    public void beforeAll(EventContext context) {
        if (isDraftInternalEvent(context)) {
            return;
        }
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();
        String screenName = ScreenUtil.getScreenName();
        StringBuilder message = new StringBuilder();
        message.append("Event start")
                .append(" | event=").append(context.getEvent())
                .append(" | target=").append(context.getTarget())
                .append(" | service=").append(context.getService());

        Object cqn = context.get("cqn");
        if (cqn != null) {
            message.append(" | cqn=").append(cqn);
        }

        Object data = context.get("data");
        if (data != null) {
            message.append(" | data=").append(data);
        }

        LogUtil.log(
                userInfo != null ? userInfo.getName() : "system",
                screenName,
                className,
                traceId,
                threadName,
                "GL_B_I00001",
                "INFO",
                message.toString());
    }

    @After(event = "*", entity = "*")
    public void afterAll(EventContext context) {
        if (isDraftInternalEvent(context)) {
            return;
        }
        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();
String screenName = ScreenUtil.getScreenName();
        StringBuilder message = new StringBuilder();
        message.append("Event end")
                .append(" | event=").append(context.getEvent())
                .append(" | target=").append(context.getTarget())
                .append(" | service=").append(context.getService());

        Object resultObj = context.get("result");
        if (resultObj != null) {
            message.append(" | result=").append(resultObj);

            if (resultObj instanceof Result result) {
                try {
                    List<Map> list = result.listOf(Map.class);
                    message.append(" | count=").append(list.size());

                    if (!list.isEmpty()) {
                        message.append(" | data=");
                        for (Map row : list) {
                            message.append(row).append(" ; ");
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
                screenName,
                className,
                traceId,
                threadName,
                "GL_B_I00002",
                "INFO",
                message.toString());
    }
}