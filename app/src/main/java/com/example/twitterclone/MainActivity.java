package com.example.twitterclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    EditText emailTextField;
    EditText passwordTextField;
    EditText userNameTextField;
    Button signUpWithEmail;
    String email;
    String pass;
    String userName;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference userDatabase;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        signUpWithEmail = (Button) findViewById(R.id.logIn);
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailTextField = (EditText) this.findViewById(R.id.user);
        passwordTextField = (EditText) this.findViewById(R.id.pass);
        userNameTextField = (EditText) this.findViewById(R.id.enterUserName);
        userDatabase = FirebaseDatabase.getInstance().getReference();


    }

    public void signUp(View view) {
        email = emailTextField.getText().toString();
        pass = passwordTextField.getText().toString();
        userName = userNameTextField.getText().toString();
        if (isValidEmail(email) && !userName.isEmpty()) {
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    writeNewUser(mAuth.getUid(),userName,email);
                                    Toast.makeText(MainActivity.this, "User created please sign in",
                                            Toast.LENGTH_SHORT).show();

                                } else {

                                    Log.i(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

        } else {
            Toast.makeText(MainActivity.this, "Please enter a valid email and userName",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void logInWithEmail(View view){
        email = emailTextField.getText().toString();
        pass = passwordTextField.getText().toString();
        if(isValidEmail(email)){
            if(user != null){
                Toast.makeText(MainActivity.this, "User is already signed in",
                        Toast.LENGTH_SHORT).show();
            }else{
                signInWithEmailAndPassword(email,pass);
            }

        }else{
            Toast.makeText(MainActivity.this, "Please enter a valid email",
                    Toast.LENGTH_SHORT).show();
        }
    }



    public void logInFaceBook(View view){
        String id = "https://storied-link-237106.firebaseapp.com/__/auth/handler";
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.i("SignIn","Sign in works");
            } else {
                Log.i("SignIn","Sign in failed");
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void signInWithEmailAndPassword(String email,String pass){
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "sign in error", Toast.LENGTH_SHORT).show();

                } else {
                    user = mAuth.getCurrentUser();
                    Log.i("result", task.getResult().toString());
                    Log.i("UserId",mAuth.getUid());
                    intent = new Intent(MainActivity.this, TweetActivity.class);
                    intent.putExtra("userNameId", mAuth.getUid());
                    startActivity(intent);

                }
            }
        });

    }

    private void writeNewUser(String userId, String name, String email) {
        ArrayList<String> helloList = new ArrayList<String>();
        helloList.add("");
        ArrayList<String> friendList = new ArrayList<String>();
        friendList.add("");
        User user = new User(name,email,friendList);
        userDatabase.child("users").child(userId).setValue(user);
        Tweets tweets = new Tweets(helloList);
        userDatabase.child("tweetsByUserId").child(userId).setValue(tweets);
    }
}
