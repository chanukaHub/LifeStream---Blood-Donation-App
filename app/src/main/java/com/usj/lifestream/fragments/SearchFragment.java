package com.usj.lifestream.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usj.lifestream.R;
import com.usj.lifestream.adapters.BloodBankAdapter;
import com.usj.lifestream.adapters.BloodRequestAdapter;
import com.usj.lifestream.model.BloodRequest;
import com.usj.lifestream.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends Fragment {
    private List<BloodRequest> bloodRequestsList;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private BloodRequestAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.spin_kit4);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView =view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        bloodRequestsList =new ArrayList<>();
        adapter= new BloodRequestAdapter(getActivity(),bloodRequestsList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("bloodRequest");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot wallpaperSnapshot:dataSnapshot.getChildren()){
                        //String id =wallpaperSnapshot.getKey();
                        //String url = wallpaperSnapshot.child("url").getValue(String.class);
                        //String category = wallpaperSnapshot.child("category").getValue(String.class);


                        BloodRequest bloodRequest = wallpaperSnapshot.getValue(BloodRequest.class);

                        //Quote q = new Quote(id,url,category);

                        //quotesList.add(q);
                        bloodRequestsList.add(bloodRequest);
                    }
                    Collections.reverse(bloodRequestsList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}