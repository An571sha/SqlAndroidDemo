package com.example.twitterclone;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String email;
    public ArrayList<String> tweet;
    public ArrayList<String> friendsList;

    public User() {}

    public User(String username, String email, ArrayList tweet,ArrayList friendsList) {
        this.username = username;
        this.email = email;
        this.tweet = tweet;
        this.friendsList = friendsList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.username = email;
    }
    public ArrayList<String> getTweet() {
        return tweet;
    }

    public void setTweet(ArrayList<String> tweet) {
        this.tweet = tweet;
    }
    public ArrayList<String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(ArrayList<String> friendsList) {
        this.friendsList = friendsList;
    }











}
