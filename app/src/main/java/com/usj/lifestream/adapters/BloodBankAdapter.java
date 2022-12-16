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
import static android.Manifest.permission.CALL_PHONE;

import com.usj.lifestream.Home;
import com.usj.lifestream.R;
import com.usj.lifestream.model.BloodBank;

import java.util.List;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.BloodBankViewHolder> {
    private Context mCtx;
    private List<BloodBank> bloodBanksList;
    private static final int REQUEST_CALL = 1;

    public BloodBankAdapter(Context mCtx, List<BloodBank> bloodBanksList) {
        this.mCtx = mCtx;
        this.bloodBanksList = bloodBanksList;
    }

    @NonNull
    @Override
    public BloodBankAdapter.BloodBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycleview_blood_bank_item,parent , false);
        return new BloodBankViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BloodBankAdapter.BloodBankViewHolder holder, int position) {
        BloodBank b = bloodBanksList.get(position);
        holder.textView.setText(b.name);
        holder.locationTextView.setText(b.address);
        holder.telephoneTextView.setText(b.phone);
        holder.hoursTextView.setText(b.hours);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = b.phone;
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
        return bloodBanksList.size();
    }

    public class BloodBankViewHolder extends RecyclerView.ViewHolder {
        TextView textView,locationTextView,telephoneTextView,hoursTextView;
        ImageView imageView;
        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name_textView1);
            locationTextView= itemView.findViewById(R.id.location_textView);
            telephoneTextView= itemView.findViewById(R.id.phone_textView);
            hoursTextView= itemView.findViewById(R.id.hours_textView);
            imageView = itemView.findViewById(R.id.imageViewCall);
        }
    }


}
