package com.example.twitterclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TweetActivity extends AppCompatActivity {
    Button signOutButton;
    Button addFriendbutton;
    Button tweetButton;
    EditText tweet;
    Intent intent;
    String message;
    ListView tweetListView;
    ListView friendsListView;
    static ArrayAdapter<String> tweetListArrayAdapter;
    static ArrayAdapter<String> friendListArrayAdapter;
    static ArrayList<String> tweetArrayList;
    static ArrayList<String> anotherTweetArrayList;
    static ArrayList<String> friendArrayList;
    DatabaseReference userDatabase;
    String key = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        signOutButton = findViewById(R.id.SignOut);
        addFriendbutton = findViewById(R.id.addFriend);
        tweetButton = findViewById(R.id.tweet);
        tweet = findViewById(R.id.addTweet);
        tweetListView = findViewById(R.id.tweetsList);
        friendsListView = findViewById(R.id.freindsList);
        intent = getIntent();
        message = intent.getStringExtra("userNameId");
        userDatabase = FirebaseDatabase.getInstance().getReference();







    }
    public void signOut(View view){
        intent = new Intent(TweetActivity.this,MainActivity.class);
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void addFriend(View view){

        friendListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendArrayList);
        friendsListView.setAdapter(friendListArrayAdapter);

    }
    public void tweetButton(View view){
        DatabaseReference dbReference = userDatabase.child("user").child(message).child("tweet").push();
        key = dbReference.getKey();
        Map<String,Object> map = new HashMap<>();
        map.put(key,tweet.getText().toString());
        dbReference.updateChildren(map);

     /* if (key == null){
          dbReference.push().setValue(anotherTweetArrayList);
      }else{
          dbReference.child(key).removeValue();
          dbReference.push().setValue(anotherTweetArrayList);
      }*/
    }

    public void onChildrenDataChanged(){
        DatabaseReference tweetRef = userDatabase.child("users");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    tweetArrayList.add(ds.child("tweet").getValue(String.class));
                }
                    tweetListArrayAdapter = new ArrayAdapter<String>(TweetActivity.this, android.R.layout.simple_list_item_1, tweetArrayList);
                    tweetListView.setAdapter(friendListArrayAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        tweetRef.addListenerForSingleValueEvent(valueEventListener);
    }
}
