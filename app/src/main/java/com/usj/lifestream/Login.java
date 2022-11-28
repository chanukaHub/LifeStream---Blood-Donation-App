package com.usj.lifestream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private TextView register;
    private Button googleButton, lifeStreamButton;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN =123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences= getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStartBoarding", true);
        if (firstStart) {
            firstRun();
        }

        mAuth=FirebaseAuth.getInstance();
        createRequest();

        register =findViewById(R.id.register_text);
        register.setOnClickListener(this);

        lifeStreamButton = findViewById(R.id.btn_signIn);
        lifeStreamButton.setOnClickListener(this);

        googleButton = findViewById(R.id.btn_google_signIn);
        googleButton.setOnClickListener(this);

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user =mAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();
        }
    }


    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(),Home.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



    public void firstRun() {
        Intent intent = new Intent(Login.this, OnBoardingActivity.class);
        startActivity(intent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstStartBoarding", false);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_google_signIn:
                signIn();
                break;
            case R.id.btn_signIn:
                startActivity(new Intent(Login.this,AccountLogin.class));
                break;
            case R.id.register_text:
                startActivity(new Intent(Login.this,AccountRegister.class));
                break;

        }
    }
}