package ui;

import java.time.LocalDateTime;

public class Logger {
    public static void log(String username, String action) {
        System.out.println(LocalDateTime.now() + " | 用户 " + username + " 执行了 " + action);
    }
}