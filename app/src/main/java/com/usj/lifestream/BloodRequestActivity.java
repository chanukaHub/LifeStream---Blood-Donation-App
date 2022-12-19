package com.usj.lifestream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usj.lifestream.model.BloodRequest;
import com.usj.lifestream.model.User;

import java.util.Calendar;

public class BloodRequestActivity extends AppCompatActivity {

    private RadioButton radioButtonMyself, radioButtonOther;
    private EditText editTextPatientName,editTextHospital,editTextTime,editTextNote,selectDateEditText,mobileNumberEditText;
    private boolean switchState;
    private Switch simpleSwitch;
    private String selected_blood,selectedDate;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private Button requestButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);
        progressBar= (ProgressBar)findViewById(R.id.request_blood_spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        simpleSwitch = (Switch) findViewById(R.id.switch_request_blood);

        radioButtonMyself = findViewById(R.id.myself_radio_btn);
        radioButtonOther = findViewById(R.id.other_radio_btn);
        radioButtonMyself.setChecked(true);

        editTextPatientName = findViewById(R.id.request_blood_name);
        editTextHospital = findViewById(R.id.request_blood_place);

        Spinner staticSpinner2 = (Spinner) findViewById(R.id.request_blood_blood_group);
        selectDateEditText =findViewById(R.id.request_blood_date);
        editTextTime = findViewById(R.id.request_blood_time);
        editTextNote = findViewById(R.id.request_blood_note);
        mobileNumberEditText=findViewById(R.id.request_blood_number);
        requestButton = findViewById(R.id.request_blood_btn);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest();
            }
        });

        final Calendar calendar =Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter.createFromResource(this, R.array.blood_array, android.R.layout.simple_spinner_item);
        staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        staticSpinner2.setAdapter(staticAdapter2);

        staticSpinner2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selected_blood = String.valueOf(adapterView.getItemAtPosition(i));
                        //Toast.makeText(AccountRegister.this, selected_province, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );

        selectDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(BloodRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        selectedDate =day+"/"+month+"/"+year;
                        selectDateEditText.setText(selectedDate);
                    }
                },year,month,day);
                dialog.show();
            }
        });

        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        userId= user.getUid();


        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);

                if (userProfile != null){
                    editTextPatientName.setFocusable(false);
                    mobileNumberEditText.setFocusable(false);
                    staticSpinner2.setEnabled(false);
                    editTextPatientName.setText(userProfile.firstName+" "+userProfile.lastName);
                    mobileNumberEditText.setText(userProfile.telephone);

                    if (userProfile.bloodGroup != null) {
                        int spinnerPosition = staticAdapter2.getPosition(userProfile.bloodGroup);
                        staticSpinner2.setSelection(spinnerPosition);
                    }

                    radioButtonMyself.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editTextPatientName.setFocusable(false);
                            mobileNumberEditText.setFocusable(false);
                            staticSpinner2.setEnabled(false);
                            editTextPatientName.setText(userProfile.firstName+" "+userProfile.lastName);
                            mobileNumberEditText.setText(userProfile.telephone);

                            if (userProfile.bloodGroup != null) {
                                int spinnerPosition = staticAdapter2.getPosition(userProfile.bloodGroup);
                                staticSpinner2.setSelection(spinnerPosition);
                            }
                        }
                    });


                }else {
                    Toast.makeText(BloodRequestActivity.this,"Something went wrong! Login Again",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        radioButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextPatientName.setText("");
                mobileNumberEditText.setText("");
                staticSpinner2.setSelection(0);
                editTextPatientName.setFocusableInTouchMode(true);
                mobileNumberEditText.setFocusableInTouchMode(true);
                staticSpinner2.setEnabled(true);
            }
        });

    }

    private void addRequest() {
        String patientName,sMobile,sPlace,sTime;
        patientName = editTextPatientName.getText().toString().trim();
        sMobile = mobileNumberEditText.getText().toString().trim();
        sPlace = editTextHospital.getText().toString().trim();
        switchState = simpleSwitch.isChecked();
        selectedDate =selectDateEditText.getText().toString().trim();
        sTime= editTextTime.getText().toString().trim();

        if(patientName.isEmpty()){
            editTextPatientName.setError("Name is required");
            editTextPatientName.requestFocus();
            return;
        }else if(sPlace.isEmpty()){
            editTextHospital.setError("Hospital is required");
            editTextHospital.requestFocus();
            return;
        }else if(selectedDate.isEmpty()){
            selectDateEditText.setError("Date is required");
            selectDateEditText.requestFocus();
            return;
        }else if(sTime.isEmpty()){
            editTextTime.setError("Time is required");
            editTextTime.requestFocus();
            return;
        }else if(sMobile.isEmpty()){
            mobileNumberEditText.setError("Mobile is required");
            mobileNumberEditText.requestFocus();
            return;
        }else if(sMobile.length() < 10){
            mobileNumberEditText.setError("Invalid Mobile Number");
            mobileNumberEditText.requestFocus();
            return;
        }else if(sMobile.length() > 12){
            mobileNumberEditText.setError("Invalid Mobile Number");
            mobileNumberEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        BloodRequest bloodRequest = new BloodRequest(patientName,sMobile,sPlace,selected_blood,selectedDate,sTime,switchState);

        FirebaseDatabase.getInstance().getReference("bloodRequest").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .push().setValue(bloodRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if (task1.isSuccessful()) {
                            Toast.makeText(BloodRequestActivity.this, "Request added successfully!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(BloodRequestActivity.this, Home.class));
                            finish();
                        } else {
                            Toast.makeText(BloodRequestActivity.this, "Fail! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}