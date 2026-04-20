package customer.log_test.error;

import com.sap.cds.services.application.ApplicationLifecycleService;
import com.sap.cds.services.application.ErrorResponseEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.request.UserInfo;
import customer.log_test.util.LogUtil;
import customer.log_test.util.TraceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ServiceName(ApplicationLifecycleService.DEFAULT_NAME)
public class GlobalErrorHandler implements EventHandler {

    @Autowired
    private UserInfo userInfo;

    @After(event = ApplicationLifecycleService.EVENT_ERROR_RESPONSE)
    public void onErrorResponse(ErrorResponseEventContext ctx) {
        System.out.println("onErrorResponse started");

        String traceId = TraceUtil.getTraceId();
        String threadName = Thread.currentThread().getName();
        String className = this.getClass().getSimpleName();

        StringBuilder meta = new StringBuilder();
        meta.append("event=").append(ctx.getEvent()).append(" | ");
        meta.append("target=").append(ctx.getTarget()).append(" | ");
        meta.append("service=").append(ctx.getService()).append(" | ");

        meta.append("keys=[");
        boolean first = true;
        for (String key : ctx.keySet()) {
            if (!first) {
                meta.append(", ");
            }
            first = false;
            meta.append(key);
        }
        meta.append("]");

        Throwable ex = ctx.getException();

        if (ex == null) {
            LogUtil.log(
                    userInfo != null ? userInfo.getName() : "system",
                    "GLOBAL",
                    className,
                    traceId,
                    threadName,
                    "GE_T_E00001",
                    "ERROR",
                    meta + " | Exception is null"
            );
            return;
        }

        Throwable root = ex;
        while (root.getCause() != null) {
            root = root.getCause();
        }

        LogUtil.log(
                userInfo != null ? userInfo.getName() : "system",
                "GLOBAL",
                className,
                traceId,
                threadName,
                "GE_T_E00002",
                "ERROR",
                meta + " | ex=" + ex
        );

        if (root != ex) {
            LogUtil.log(
                    userInfo != null ? userInfo.getName() : "system",
                    "GLOBAL",
                    className,
                    traceId,
                    threadName,
                    "GE_T_E00003",
                    "ERROR",
                    meta + " | root=" + root
            );
        }
    }
}