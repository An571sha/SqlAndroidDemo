package com.example.twitterclone;

import java.util.List;

public class User {
    public String username;
    public String email;
    public List tweet;
    public List friendsList;

    public User() {}

    public User(String username, String email, List tweet,List friendsList) {
        this.username = username;
        this.email = email;
        this.tweet =tweet;
        this.friendsList = friendsList;
    }

}
