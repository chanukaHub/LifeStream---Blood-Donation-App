package com.usj.lifestream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.usj.lifestream.fragments.HomeFragment;
import com.usj.lifestream.fragments.EventFragment;
import com.usj.lifestream.fragments.SearchFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private FirebaseUser user;
    BottomNavigationView bottomNavigationView;
    Fragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.menu);

        user= FirebaseAuth.getInstance().getCurrentUser();

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()){
            case R.id.item1:
                //nav.setCheckedItem(R.id.menu_home);
                displayFragment(homeFragment);
                break;
            case R.id.item2:
                fragment = new SearchFragment();
                //nav.setCheckedItem(R.id.menu_categories);
                displayFragment(fragment);
                break;
            case R.id.item3:
                fragment = new EventFragment();
                //nav.setCheckedItem(R.id.menu_favorite);
                displayFragment(fragment);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.menu_two);
        View view = MenuItemCompat.getActionView(menuItem);

        CircleImageView profileImage = view.findViewById(R.id.toolbar_profile_image);

        if (user.getPhotoUrl()==null){
            Glide.with(this).load(this.getResources().getIdentifier("profile", "drawable", this.getPackageName())).into(profileImage);
        }else {
            Glide.with(this).load(user.getPhotoUrl()).into(profileImage);
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this,Login.class));
                finish();
                //startActivity(new Intent(Home.this,ProfileActivity.class));

            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}