package com.usj.lifestream.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.usj.lifestream.R;
import com.usj.lifestream.model.BloodRequest;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.BloodRequestViewHolder>{

    private Context mCtx;
    private List<BloodRequest> bloodRequestsList;
    private static final int REQUEST_CALL = 1;

    public BloodRequestAdapter(Context mCtx, List<BloodRequest> bloodRequestsList) {
        this.mCtx = mCtx;
        this.bloodRequestsList = bloodRequestsList;
    }

    @Override
    public BloodRequestAdapter.BloodRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycleview_blood_request_item,parent , false);
        return new BloodRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodRequestAdapter.BloodRequestViewHolder holder, int position) {
        BloodRequest bloodRequest = bloodRequestsList.get(position);
        holder.nameTextView.setText(bloodRequest.patientName);
        holder.placeTextView.setText(bloodRequest.place);
        holder.mobileTextView.setText(bloodRequest.mobile);
        holder.timeTextView.setText(bloodRequest.date+ " "+bloodRequest.time);
        holder.noteTextView.setText(bloodRequest.note);
        holder.bloodGroupTextView.setText(bloodRequest.bloodGroup);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = bloodRequest.mobile;
                if (number.trim().length() > 0) {
                    if (ContextCompat.checkSelfPermission(mCtx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mCtx, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    } else {
                        String dial = "tel:" + number;
                        mCtx.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                } else {
                    Toast.makeText(mCtx, "Phone Number Not Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodRequestsList.size();
    }

    public class BloodRequestViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView,placeTextView,timeTextView,noteTextView,bloodGroupTextView,mobileTextView;
        ImageView imageView;
        public BloodRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.blood_request_name_textView);
            placeTextView = itemView.findViewById(R.id.blood_request_location_textView);
            mobileTextView = itemView.findViewById(R.id.blood_request_phone_textView);
            timeTextView = itemView.findViewById(R.id.blood_request_hours_textView);
            noteTextView =itemView.findViewById(R.id.note_textView);
            bloodGroupTextView = itemView.findViewById(R.id.blood_request_group_textView);
            imageView = itemView.findViewById(R.id.blood_request_call_imageView);
        }
    }
}
