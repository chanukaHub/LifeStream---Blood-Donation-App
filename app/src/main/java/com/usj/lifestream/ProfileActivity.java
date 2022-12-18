package com.usj.lifestream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usj.lifestream.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private ImageButton imageButton;
    private Button singOutBtn, saveChangesBtn;
    TextView email;
    CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageButton=findViewById(R.id.profile_back);
        profileImage = findViewById(R.id.profile_view);
        email =findViewById(R.id.profile_email);
        //final TextView name =findViewById(R.id.profile_name);
        singOutBtn = findViewById(R.id.sign_out_btn);


        singOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,Login.class));
                finish();
            }
        });

        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        userId= user.getUid();

        if (user.getPhotoUrl()==null){
            Drawable someImage = getResources().getDrawable(R.drawable.empty_profile_picture);
            Glide.with(this)
                    .load(someImage)
                    .centerCrop()
                    .error(someImage).into(profileImage);
        }else {
            Glide.with(this).load(user.getPhotoUrl()).into(profileImage);
        }


        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);

                if (userProfile != null){
                    email.setText(userProfile.email);
                    //name.setText(userProfile.name);

//                    if (email.getText().equals("")){
//                        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);
//                        if (signInAccount != null){
//                            email.setText(signInAccount.getEmail());
//                            //name.setText(signInAccount.getDisplayName());
//                        }
//                    }
                }else {
                    GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);
                    if (signInAccount != null){
                        email.setText(signInAccount.getEmail());
                        //name.setText(signInAccount.getDisplayName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}