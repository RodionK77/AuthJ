package com.example.authj;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String BASE_URL = "https://api.github.com";
    public static final String LOGIN_URL = "https://github.com/login/oauth";
    public static final String LOGIN_URL_VK = "https://oauth.vk.com";
    public static final String CLIENT_ID = "384c11f50539b448c4eb";
    public static final String CLIENT_ID_VK = "51470642";
    public static final String REDIRECT_URL_VK = "https://oauth.vk.com/blank.html";
    public static final String CLIENT_SECRET = "c8c19e975a8b5243d1eda87ee435a4f1c17c0006";
    public static final String SCOPE = "admin:repo_hook%20user";
    public static final String SCOPE_VK = "offline";
    public static final String STATE = "rodya";
    public static String TOKEN = "";
    public static String TOKEN_VK = "";
    public static String USER_ID_VK = "";
    public static Map<String, String> HEADERS = new HashMap<>();
    public static Map<String, String> HEADERS_VK = new HashMap<>();

    public static String checkNull(String value){
        if(value.equals("null"))
            return "";
        else return value;
    }
}
