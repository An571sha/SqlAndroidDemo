package com.example.twitterclone;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String username;
    public String email;
    public ArrayList<String> friendsListByUserId;

    public User() {}

    public User(String username, String email,ArrayList friendsListByUserId) {
        this.username = username;
        this.email = email;
        this.friendsListByUserId = friendsListByUserId;
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

    public ArrayList<String> getFriendsList() {
        return friendsListByUserId;
    }

    public void setFriendsList(ArrayList<String> friendsListByUserId) {
        this.friendsListByUserId = friendsListByUserId;
    }











}
