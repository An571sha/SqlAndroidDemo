package com.example.twitterclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class TweetActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView userIdTextView;
    Button signOutButton;
    Intent intent;
    String message;
    ListView tweetListView;
    ListView friendsListView;
    static ArrayAdapter<String> tweetListArrayAdapter;
    static ArrayAdapter<String> freindLsitArrayAdapter;
    static ArrayList<String> tweetArrayList;
    static ArrayList<String> friendArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        userIdTextView = findViewById(R.id.userId);
        signOutButton = findViewById(R.id.SignOut);
        intent = getIntent();
        message = intent.getStringExtra("userNameId");
        userIdTextView.setText(message);
        tweetListView = findViewById(R.id.tweetsList);
        friendsListView = findViewById(R.id.freindsList);
        freindLsitArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendArrayList);
        friendsListView.setAdapter(freindLsitArrayAdapter);




    }
    public void signOut(View view){
        intent = new Intent(TweetActivity.this,MainActivity.class);
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
