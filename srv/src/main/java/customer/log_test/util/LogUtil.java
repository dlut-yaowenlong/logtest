package customer.log_test.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {
    // 日時フォーマット：2026-04-16 14:30:25
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

     /**
     * ログを出力する
     *
     * @param username    ユーザー名
     * @param screenName  画面名
     * @param className   クラス名
     * @param traceId     トレースID
     * @param threadName  出力スレッド名
     * @param logId       ログID
     * @param logLevel    ログレベル（例：INFO / WARN / ERROR）
     * @param message     ログ内容
     */
    public static void log(String username,
                           String screenName,
                           String className,
                           String traceId,
                           String threadName,
                           String logId,
                           String logLevel,
                           String message) {
        String time = LocalDateTime.now().format(FORMATTER);

       String safeUsername = username == null ? "UNKNOWN_USER" : username;
        String safeScreenName = screenName == null ? "UNKNOWN_SCREEN" : screenName;
        String safeClassName = className == null ? "UNKNOWN_CLASS" : className;
        String safeTraceId = traceId == null ? "UNKNOWN_TRACE_ID" : traceId;
        String safeThreadName = threadName == null ? Thread.currentThread().getName() : threadName;
        String safeLogId = logId == null ? "UNKNOWN_LOG_ID" : logId;
        String safeLogLevel = logLevel == null ? "INFO" : logLevel.toUpperCase();
        String safeMessage = message == null ? "" : message;

        String output = String.format(
                "[%s] [%-12s] [%-20s] [%-30s] [%-36s] [%-20s] [%-20s] [%-5s] %s",
                time,
                safeUsername,
                safeScreenName,
                safeClassName,
                safeTraceId,
                safeThreadName,
                safeLogId,
                safeLogLevel,
                safeMessage
        );
        //TOBE：ログサーバーあれば、サーバーに送信する
        System.out.println(output);
    }
}