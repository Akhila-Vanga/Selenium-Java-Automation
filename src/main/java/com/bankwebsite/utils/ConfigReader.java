package com.bankwebsite.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties props;

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            props = new Properties();
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load config.properties");
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
