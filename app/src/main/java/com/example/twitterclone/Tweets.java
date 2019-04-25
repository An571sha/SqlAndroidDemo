package com.example.twitterclone;

import java.util.ArrayList;

public class Tweets {
    public ArrayList<String> tweet;


    public Tweets(ArrayList tweet) {
        this.tweet = tweet;

    }
    public ArrayList<String> getTweet() {
        return tweet;
    }

    public void setTweet(ArrayList<String> tweet) {
        this.tweet = tweet;
    }

}
