package com.example.twitterclone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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
    static ArrayList<String> friendArrayList;
    DatabaseReference userDatabase;
    Handler mHandler;
    String key = null;
    String requiredVal;
    String requiredKey;


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
        mHandler= new Handler();
        onFriendsDataChange();
        tweetListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String data=(String)parent.getItemAtPosition(position);
                createDialog(position,data);
                return true;
            }
        });

    }
    public void signOut(View view){
        intent = new Intent(TweetActivity.this,MainActivity.class);
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void addFriend(View view){

       /* friendListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendArrayList);
        friendsListView.setAdapter(friendListArrayAdapter);*/

    }
    public void tweetButton(View view){
        DatabaseReference dbReference = userDatabase.child("users").child(message).child("tweet").push();
        key = dbReference.getKey();
        Map<String,Object> map = new HashMap<>();
        map.put(key,tweet.getText().toString());
        dbReference.updateChildren(map);
        onTweetDataChanged();

    }

    public void onTweetDataChanged(){
        DatabaseReference tweetRef = userDatabase.child("users").child(message).child("tweet");
        tweetArrayList = new ArrayList<String>();
        tweetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tweetArrayList.clear();
                if(tweetListArrayAdapter !=null) {
                    tweetListArrayAdapter.clear();
                }
                for(DataSnapshot tweets: dataSnapshot.getChildren()){
                    tweetArrayList.add(tweets.getValue().toString().substring(tweets.getValue().toString().lastIndexOf("=")+1));
                }

                Log.i("List",tweetArrayList.toString());
                //---- Add code to make data appear in descending order -----
                tweetListArrayAdapter = new ArrayAdapter<String>(TweetActivity.this, android.R.layout.simple_list_item_1, tweetArrayList);
                tweetListView.setAdapter(tweetListArrayAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onFriendsDataChange(){
        friendArrayList = new ArrayList<>();
        userDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){

                  //  Log.d("List", "User name: " + dataSnapshot.child(message).toString());
                    for(DataSnapshot s : dataSnapshot.getChildren()){
                        s.child("user").getValue();
                        Log.d("ChildList", "User name: " + s.child("user").getValue());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void createDialog(final int position, final String data) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Delete").setMessage("Do you really want to delete this tweet");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("SelectedData", data);
                DatabaseReference ref = userDatabase.child("users").child(message).child("tweet");
                Log.d("database", ref.orderByValue().equalTo(data).toString());
                ref.orderByValue().equalTo(data).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                             requiredVal= snap.getValue(String.class);
                             requiredKey= snap.getKey();
                        }
                            Log.d("Logging The Value",requiredKey + requiredVal);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                tweetArrayList.remove(data);
                tweetListArrayAdapter.notifyDataSetChanged();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }


}
