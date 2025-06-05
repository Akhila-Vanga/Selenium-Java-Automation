package com.bankwebsite.utils;
public class EnvConfig {
    private String url;
    private static String username;
    private static String password;

    public EnvConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getUrl() { return url; }
    public static String getUsername() { return username; }
    public static String getPassword() { return password; }
}