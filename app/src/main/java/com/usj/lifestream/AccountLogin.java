package com.usj.lifestream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountLogin extends AppCompatActivity implements View.OnClickListener{

    private EditText email,password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private Button login;
    private TextView rest_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);

        mAuth = FirebaseAuth.getInstance();

        login=findViewById(R.id.login_btn);
        login.setOnClickListener(this);

        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_Password);

        progressBar = (ProgressBar)findViewById(R.id.spin_kit2);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        rest_password= findViewById(R.id.textView_reset_password);
        rest_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                login();
                break;
            //case R.id.textView_reset_password:
                //startActivity(new Intent(MindMelloAccountLogin.this,ForgotPassword.class));
                //break;
        }

    }

    private void login() {
        String sEmail,sPassword;

        sEmail= email.getText().toString().trim();
        sPassword= password.getText().toString().trim();

        if(sEmail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()){
            email.setError("Please provide valid email!");
            email.requestFocus();
            return;
        } else if(sPassword.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(AccountLogin.this,Home.class));
                    finishAffinity();

                }else {
                    Toast.makeText(AccountLogin.this,"Failed to login!",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}