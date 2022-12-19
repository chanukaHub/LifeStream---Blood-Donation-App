package com.usj.lifestream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usj.lifestream.model.User;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;
    private ImageButton imageButton;
    private Button singOutBtn, saveChangesBtn;
    private TextView email,userType;
    private CircleImageView profileImage;
    private EditText firstNameEditText,lastNameEditText,line1EditText,line2EditText,cityEditText,mobileEditText,selectDateEditText,passwordEditText,confirmPasswordEditText;
    private ProgressBar progressBar;
    private boolean switchState;
    private Switch simpleSwitch;
    private String selected_province, selected_blood,selectedDate;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        relativeLayout = findViewById(R.id.profile_relative_layout);

        progressBar= (ProgressBar)findViewById(R.id.profile_spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        simpleSwitch = (Switch) findViewById(R.id.profile_switch_donor);

        imageButton=findViewById(R.id.profile_back);
        profileImage = findViewById(R.id.profile_view);
        email =findViewById(R.id.profile_email);
        userType = findViewById(R.id.userType);
        firstNameEditText=findViewById(R.id.profile_firstname);
        lastNameEditText=findViewById(R.id.profile_lastname);
        mobileEditText = findViewById(R.id.profile_telephone);
        line1EditText =findViewById(R.id.profile_address_line1);
        line2EditText = findViewById(R.id.profile_address_line2);
        cityEditText =findViewById(R.id.profile_city);

        Spinner staticSpinner1 = (Spinner) findViewById(R.id.profile_province);
        Spinner staticSpinner2 = (Spinner) findViewById(R.id.profile_blood_group);
        selectDateEditText =findViewById(R.id.profile_dob);

        singOutBtn = findViewById(R.id.sign_out_btn);
        saveChangesBtn = findViewById(R.id.save_btn);

        final Calendar calendar =Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter1 = ArrayAdapter.createFromResource(this, R.array.province_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter.createFromResource(this, R.array.blood_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner1.setAdapter(staticAdapter1);
        staticSpinner2.setAdapter(staticAdapter2);

        staticSpinner1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selected_province = String.valueOf(adapterView.getItemAtPosition(i));
                        //Toast.makeText(AccountRegister.this, selected_province, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );
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
                DatePickerDialog dialog = new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
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


        singOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this,Login.class));
                finish();
            }
        });

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        Drawable someImage = getResources().getDrawable(R.drawable.empty_profile_picture);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users");
        userId= user.getUid();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);

                if (userProfile != null){
                    if(userProfile.profileURL != null){
                        Glide.with(ProfileActivity.this).load(userProfile.profileURL).centerCrop().error(someImage).into(profileImage);
                    }else if(user.getPhotoUrl() != null) {
                        Glide.with(ProfileActivity.this).load(user.getPhotoUrl()).into(profileImage);
                    }else{
                        Glide.with(ProfileActivity.this).load(someImage).centerCrop().error(someImage).into(profileImage);
                    }
                    email.setText(userProfile.email);
                    firstNameEditText.setText(userProfile.firstName);
                    lastNameEditText.setText(userProfile.lastName);
                    mobileEditText.setText(userProfile.telephone);
                    line1EditText.setText(userProfile.addressLine1);
                    line2EditText.setText(userProfile.addressLine2);
                    cityEditText.setText(userProfile.town);
                    if (userProfile.district != null) {
                        int spinnerPosition = staticAdapter1.getPosition(userProfile.district);
                        staticSpinner1.setSelection(spinnerPosition);
                    }
                    selectDateEditText.setText(userProfile.dateOfBirth);
                    if (userProfile.bloodGroup != null) {
                        int spinnerPosition = staticAdapter2.getPosition(userProfile.bloodGroup);
                        staticSpinner2.setSelection(spinnerPosition);
                    }

                    updateSwitch(userProfile.isDonor);

                }else {
                    GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);
                    if (signInAccount != null){
                        Glide.with(ProfileActivity.this).load(user.getPhotoUrl()).into(profileImage);
                        email.setText(signInAccount.getEmail());
                        firstNameEditText.setText(signInAccount.getGivenName());
                        lastNameEditText.setText(signInAccount.getFamilyName());
                        updateSwitch(false);
                    }
                }
                relativeLayout.setVisibility(View.VISIBLE);
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

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userType.setText("Donor & Recipient(Both)");
                } else {
                    userType.setText("Only Recipient");
                }
            }
        });
    }
    private void updateSwitch(boolean isDonor) {
        if (isDonor == true){
            simpleSwitch.setChecked(true);
            userType.setText("Donor & Recipient(Both)");
        }else{
            userType.setText("Only Recipient");
        }
    }

    private void updateUser() {
            String sFirstName,sLastName,sEmail,sLine1,sLine2,sCity,sMobile;
            sFirstName = firstNameEditText.getText().toString().trim();
            sLastName = lastNameEditText.getText().toString().trim();
            sEmail = email.getText().toString().trim();
            sLine1 = line1EditText.getText().toString().trim();
            sLine2 = line2EditText.getText().toString().trim();
            sCity = cityEditText.getText().toString().trim();
            sMobile = mobileEditText.getText().toString().trim();
            switchState = simpleSwitch.isChecked();
            selectedDate =selectDateEditText.getText().toString().trim();

            if(sFirstName.isEmpty()){
                firstNameEditText.setError("First Name is required");
                firstNameEditText.requestFocus();
                return;
            }else if(sLastName.isEmpty()){
                lastNameEditText.setError("Last Name is required");
                lastNameEditText.requestFocus();
                return;
            }else if(sLine1.isEmpty()){
                line1EditText.setError("Line 01 is required");
                line1EditText.requestFocus();
                return;
            }else if(sCity.isEmpty()){
                cityEditText.setError("City is required");
                cityEditText.requestFocus();
                return;
            }else if(sMobile.isEmpty()){
                mobileEditText.setError("Mobile is required");
                mobileEditText.requestFocus();
                return;
            }else if(sMobile.length() < 10){
                mobileEditText.setError("Invalid Mobile Number");
                mobileEditText.requestFocus();
                return;
            }else if(sMobile.length() > 12){
                mobileEditText.setError("Invalid Mobile Number");
                mobileEditText.requestFocus();
                return;
            }

        progressBar.setVisibility(View.VISIBLE);
        User user1 = new User(sFirstName, sLastName, sMobile, sEmail, sLine1, sLine2, sCity, selected_province, null, selectedDate, selected_blood, null, null, null, switchState);

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if (task1.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Details has been updated successfully!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(ProfileActivity.this, Login.class));
                            finish();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Fail to update! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}