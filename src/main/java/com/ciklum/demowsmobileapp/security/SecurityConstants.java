package com.ciklum.demowsmobileapp.security;

import com.ciklum.demowsmobileapp.SpringApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 86400000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SING_UP_URL = "/users";

    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getSecretToken();
    }
}
