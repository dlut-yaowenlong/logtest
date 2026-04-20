package customer.log_test.util;

import customer.log_test.filter.TraceIdFilter;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class TraceUtil {

    private TraceUtil() {
    }

    public static String getTraceId() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            Object value = attrs.getAttribute(TraceIdFilter.TRACE_ID_ATTR, RequestAttributes.SCOPE_REQUEST);
            if (value instanceof String s && !s.isBlank()) {
                return s;
            }
        }
        return MDC.get("traceId");
    }
}