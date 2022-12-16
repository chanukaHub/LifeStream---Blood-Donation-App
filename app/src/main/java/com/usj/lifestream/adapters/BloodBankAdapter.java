package com.usj.lifestream.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.usj.lifestream.R;
import com.usj.lifestream.model.BloodBank;

import java.util.List;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.BloodBankViewHolder> {
    private Context mCtx;
    private List<BloodBank> bloodBanksList;

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

    }

    @Override
    public int getItemCount() {
        return bloodBanksList.size();
    }

    public class BloodBankViewHolder extends RecyclerView.ViewHolder {
        TextView textView,locationTextView,telephoneTextView,hoursTextView;
        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name_textView1);
            locationTextView= itemView.findViewById(R.id.location_textView);
            telephoneTextView= itemView.findViewById(R.id.phone_textView);
            hoursTextView= itemView.findViewById(R.id.hours_textView);
        }
    }
}
