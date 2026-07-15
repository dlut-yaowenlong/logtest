package customer.log_test.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class ScreenUtil {

    private static final String REFERER_HEADER = "Referer";
    private static final String UNKNOWN_SCREEN = "unknown";
    private static final String BOOKS_SCREEN = "書籍画面";

    private ScreenUtil() {
    }

    public static String getScreenName() {
        HttpServletRequest request = getCurrentRequest();

        if (request == null) {
            return UNKNOWN_SCREEN;
        }

        String referer = request.getHeader(REFERER_HEADER);
        String name = request.getHeader("X-Screen-Name");
        System.out.println("ScreenUtil.getScreenName() referer=" + referer + ", name=" + name);
        if (referer == null || referer.isBlank()) {
            return UNKNOWN_SCREEN;
        }

        if (referer.toLowerCase().contains("books")) {
            return BOOKS_SCREEN;
        }

        return UNKNOWN_SCREEN;
    }

    private static HttpServletRequest getCurrentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }

        return null;
    }
}