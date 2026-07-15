package customer.log_test.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class ScreenUtil {

    
    private static final String UNKNOWN_SCREEN = "unknown";
    private static final String BOOKS_LIST_SCREEN = "書籍一覧画面";
    private static final String BOOKS_DETAIL_SCREEN = "書籍詳細画面";
    private ScreenUtil() {
    }

    public static String getScreenName() {
        HttpServletRequest request = getCurrentRequest();

        if (request == null) {
            return UNKNOWN_SCREEN;
        }

        String name = request.getHeader("X-Screen-Name");
        System.out.println("ScreenUtil.getScreenName() name=" + name);
        if ("bookslistreport".equals(name)) {
            return BOOKS_LIST_SCREEN;
        }
        if ("booksobjectpage".equals(name)) {
            return BOOKS_DETAIL_SCREEN;
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