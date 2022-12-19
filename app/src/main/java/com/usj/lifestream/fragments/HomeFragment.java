package com.usj.lifestream.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.usj.lifestream.BloodRequestActivity;
import com.usj.lifestream.Home;
import com.usj.lifestream.Login;
import com.usj.lifestream.ProfileActivity;
import com.usj.lifestream.R;
import com.usj.lifestream.adapters.BloodBankAdapter;
import com.usj.lifestream.adapters.EventSliderAdapter;
import com.usj.lifestream.model.BloodBank;
import com.usj.lifestream.model.Event;
import com.usj.lifestream.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private ProgressBar progressBar;
    ViewPager2 viewPager2;
    List<Event> eventsList;
    private static List<BloodBank> bloodBankList;
    private Handler slideHandler = new Handler();
    private EventSliderAdapter adapter;
    private BloodBankAdapter bloodBankAdapter;
    private static ArrayList<Event> playerList = new ArrayList<Event>();
    boolean isFirstLoad = true;
    private RecyclerView recyclerView;
    RelativeLayout requestBlood_relativeLayout,donateBlood_relativeLayout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        userId= user.getUid();

        if (isFirstLoad){
            progressBar = view.findViewById(R.id.spin_kit3);
            Sprite doubleBounce = new Wave();
            progressBar.setIndeterminateDrawable(doubleBounce);
            progressBar.setVisibility(View.VISIBLE);
            bloodBankList=new ArrayList<>();

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("event").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        if (isFirstLoad){
                            progressBar.setVisibility(View.GONE);
                            isFirstLoad = false;
                        }
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            Event e = d.toObject(Event.class);
                            eventsList.add(e);
                        }
                        for (Event q :eventsList){
                            addToPlayerList(q);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // if we do not get any data or any error we are displaying
                    // a toast message that we do not get any data
                    Toast.makeText(getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                }
            });


            db.collection("BloodBank").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            BloodBank b = d.toObject(BloodBank.class);
                            bloodBankList.add(b);
                        }
                        Collections.reverse(bloodBankList);
                        bloodBankAdapter.notifyDataSetChanged();
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(getContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // if we do not get any data or any error we are displaying
                    // a toast message that we do not get any data
                    Toast.makeText(getContext(), "Fail to get the data.", Toast.LENGTH_SHORT).show();
                }
            });

        }

        requestBlood_relativeLayout =view.findViewById(R.id.request_blood_relativeLayout_btn);

        recyclerView =view.findViewById(R.id.blood_bank_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));

        viewPager2 = view.findViewById(R.id.viewPager2);
        eventsList= new ArrayList<>();

        adapter = new EventSliderAdapter(getActivity(),playerList,viewPager2);
        viewPager2.setAdapter(adapter);

        bloodBankAdapter= new BloodBankAdapter(getActivity(),bloodBankList);
        recyclerView.setAdapter(bloodBankAdapter);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer =new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(0));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(1.00f + r * 0.00f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(slideRunnable);
                slideHandler.postDelayed(slideRunnable,4000);
            }
        });

        requestBlood_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile=snapshot.getValue(User.class);

                        if (userProfile != null) {
                            Intent intent = new Intent(getContext(), BloodRequestActivity.class);
                            startActivity(intent);
                        }else {
                            showErrorDialog();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(slideRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(slideRunnable,4000);
    }

    public boolean addToPlayerList(Event input) {
        if (playerList.size() < 10) {
            playerList.add(input);
            return true;
        } else {
            return false;
        }
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);


        View view = LayoutInflater.from(getContext()).inflate(R.layout.no_user_detail_dialog, getActivity().findViewById(R.id.no_user_details_layout));
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        view.findViewById(R.id.update_details_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
                alertDialog.cancel();
            }
        });
        view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });



    }
}