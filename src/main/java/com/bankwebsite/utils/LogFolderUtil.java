package com.bankwebsite.utils;

import java.io.File;

public class LogFolderUtil {
    public static void createLogFolder() {
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }
}
