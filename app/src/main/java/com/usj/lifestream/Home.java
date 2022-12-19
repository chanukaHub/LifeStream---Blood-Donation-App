package com.usj.lifestream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usj.lifestream.fragments.HomeFragment;
import com.usj.lifestream.fragments.EventFragment;
import com.usj.lifestream.fragments.SearchFragment;
import com.usj.lifestream.model.User;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigationView;
    private Fragment homeFragment;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(!isConnectingToInternet(this))
        {
            showInternetDialog();
        }

        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        userId= user.getUid();

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.menu);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        homeFragment= new HomeFragment();
        displayFragment(homeFragment);
    }

    private void displayFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_area,fragment)
                .commit();
    }

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = LayoutInflater.from(this).inflate(R.layout.no_internet_dialog, findViewById(R.id.no_internet_layout));
        view.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnectingToInternet(Home.this)) {
                    showInternetDialog();
                } else {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            }
        });

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public static boolean isConnectingToInternet(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()){
            case R.id.item1:
                if(!isConnectingToInternet(this))
                {
                    showInternetDialog();
                }
                //nav.setCheckedItem(R.id.menu_home);
                displayFragment(homeFragment);
                break;
            case R.id.item2:
                if(!isConnectingToInternet(this))
                {
                    showInternetDialog();
                }
                fragment = new SearchFragment();
                //nav.setCheckedItem(R.id.menu_categories);
                displayFragment(fragment);
                break;
            //case R.id.item3:
                //if(!isConnectingToInternet(this))
                //{
                //    showInternetDialog();
                //}
                //fragment = new EventFragment();
                //nav.setCheckedItem(R.id.menu_favorite);
                //displayFragment(fragment);
                //break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.menu_two);
        View view = MenuItemCompat.getActionView(menuItem);

        CircleImageView profileImage = view.findViewById(R.id.toolbar_profile_image);
        TextView textView = view.findViewById(R.id.title);

        Drawable someImage = getResources().getDrawable(R.drawable.profile);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);

                if (userProfile != null) {
                    if (userProfile.profileURL != null) {
                        Glide.with(Home.this).load(userProfile.profileURL).centerCrop().error(someImage).into(profileImage);
                    } else if (user.getPhotoUrl() != null) {
                        Glide.with(Home.this).load(user.getPhotoUrl()).into(profileImage);
                    } else {
                        Glide.with(Home.this).load(someImage).centerCrop().error(someImage).into(profileImage);
                    }
                    textView.setText(userProfile.firstName + " " + userProfile.lastName);
                }else {
                GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(Home.this);
                if (signInAccount != null){
                    Glide.with(Home.this).load(user.getPhotoUrl()).into(profileImage);
                    textView.setText(signInAccount.getDisplayName());
                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,ProfileActivity.class));

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isConnectingToInternet(this))
        {
            showInternetDialog();
        }
    }
}