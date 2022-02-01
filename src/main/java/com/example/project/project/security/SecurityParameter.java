package com.example.project.project.security;

public class SecurityParameter {
    public static final String LOGIN_URL = "/project/api/login";
    public static final String LOGOUT_URL = "/project/api/logout";
    public static final String SIGNUP_URL = "/project/v1/user/signup";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String SECRET_KEY = "secret";
    public static final int TOKEN_EXPIRATION_TIME = 180000; //3'
    public static final int TOKEN_EXPIRATION_TIME_CACHE = 240000;//4'
}