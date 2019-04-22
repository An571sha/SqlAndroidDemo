package com.example.twitterclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    EditText userNameTextField;
    EditText passwordTextField;
    Button signUpWithEmail;
    TextView login;
    String email;
    String pass;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        signUpWithEmail = (Button) findViewById(R.id.logIn);
        login = findViewById(R.id.signUpOrLogIn);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        userNameTextField = (EditText) this.findViewById(R.id.user);
        passwordTextField = (EditText) this.findViewById(R.id.pass);

    }

    public void signUp(View view) {
        email = userNameTextField.getText().toString();
        pass = passwordTextField.getText().toString();
        if (isValidEmail(email)) {
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    user = mAuth.getCurrentUser();
                                } else {

                                    Log.i(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

        } else {
            Toast.makeText(MainActivity.this, "Please enter a valid email",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void logInWithEmail(View view){
        email = userNameTextField.getText().toString();
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
                } else
                    user = mAuth.getCurrentUser();

            }
        });

    }
}
