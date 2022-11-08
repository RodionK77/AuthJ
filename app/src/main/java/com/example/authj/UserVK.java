package com.example.authj;

public class UserVK {
    private String first_name, last_name, bdate, avatar_url;

    public UserVK(String first_name, String last_name, String bdate, String avatar_url) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.bdate = bdate;
        this.avatar_url = avatar_url;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getBdate() {
        return bdate;
    }

    public String getAvatar_url() {
        return avatar_url;
    }
}
