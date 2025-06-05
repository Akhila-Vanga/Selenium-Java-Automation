package com.bankwebsite.utils;

public class EnvConfigUtil {

    public static EnvConfig getLoginConfig(String env) {
        if (env == null) {
            throw new IllegalArgumentException("Environment cannot be null");
        }

        switch (env.toUpperCase()) {
            case "QA":
                return new EnvConfig(
                    "http://www.qa.guru99.com",
                    "testuser",
                    "Test@123"
                );
            case "PROD":
                return new EnvConfig(
                    "http://www.prod.guru99.com",
                    "testuser",
                    "Test@123"
                );
            case "BASE":
                return new EnvConfig(
                    "http://www.demo.guru99.com/",
                    "mngr624245",
                    "EgyqAna"
                );
            default:
                throw new IllegalArgumentException("Invalid environment: " + env +
                    ". Please use one of: QA, PROD, Base.");
        }
    }
}


