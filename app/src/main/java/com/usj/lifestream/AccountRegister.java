package com.usj.lifestream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.usj.lifestream.model.User;

import java.util.Calendar;
import java.util.Date;

public class AccountRegister extends AppCompatActivity implements View.OnClickListener{

    private String selected_province, selected_blood,selectedDate;
    private Button registerBtn;
    private EditText firstNameEditText,lastNameEditText,emailEditText,line1EditText,line2EditText,cityEditText,mobileEditText,selectDateEditText,passwordEditText,confirmPasswordEditText;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    Boolean switchState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        Switch simpleSwitch = (Switch) findViewById(R.id.switch_donor);
        switchState = simpleSwitch.isChecked();

        mAuth = FirebaseAuth.getInstance();

        registerBtn=findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);

        firstNameEditText=findViewById(R.id.register_firstname);
        lastNameEditText=findViewById(R.id.register_lastname);
        emailEditText=findViewById(R.id.register_email);
        line1EditText =findViewById(R.id.register_address_line1);
        line2EditText =findViewById(R.id.register_address_line2);
        cityEditText =findViewById(R.id.register_city);
        mobileEditText =findViewById(R.id.register_telephone);
        passwordEditText=findViewById(R.id.register_password);
        confirmPasswordEditText=findViewById(R.id.register_confirm_password);

        Spinner staticSpinner1 = (Spinner) findViewById(R.id.register_province);
        Spinner staticSpinner2 = (Spinner) findViewById(R.id.register_blood_group);
        selectDateEditText =findViewById(R.id.register_dob);

        progressBar= (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

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
                DatePickerDialog dialog = new DatePickerDialog(AccountRegister.this, new DatePickerDialog.OnDateSetListener() {
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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_btn:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String sFirstName,sLastName,sEmail,sLine1,sLine2,sCity,sMobile,sPassword,sConfirmPassword;
        sFirstName = firstNameEditText.getText().toString().trim();
        sLastName = lastNameEditText.getText().toString().trim();
        sEmail = emailEditText.getText().toString().trim();
        sLine1 = line1EditText.getText().toString().trim();
        sLine2 = line2EditText.getText().toString().trim();
        sCity = cityEditText.getText().toString().trim();
        sMobile = mobileEditText.getText().toString().trim();
        sPassword= passwordEditText.getText().toString().trim();
        sConfirmPassword= confirmPasswordEditText.getText().toString().trim();

        if(sFirstName.isEmpty()){
            firstNameEditText.setError("First Name is required");
            firstNameEditText.requestFocus();
            return;
        }else if(sLastName.isEmpty()){
            lastNameEditText.setError("Last Name is required");
            lastNameEditText.requestFocus();
            return;
        }else if(sEmail.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()){
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
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
        } else if(sPassword.isEmpty()){
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }else if(sPassword.length() < 6){
            passwordEditText.setError("Min password length should be 6 characters!");
            passwordEditText.requestFocus();
            return;
        }else if(sConfirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Password is required");
            confirmPasswordEditText.requestFocus();
            return;
        }else if(!sConfirmPassword.equals(sPassword)) {
            confirmPasswordEditText.setError("Passwords are not matched");
            confirmPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user1= new User(sFirstName,sLastName,sMobile,sEmail,sLine1,sLine2,sCity,selected_province,null,selectedDate,selected_blood,null,null,switchState);

                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task1) {
                                    if (task1.isSuccessful()){
                                        Toast.makeText(AccountRegister.this,"User has been registered successfully!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(AccountRegister.this,Login.class));
                                        finish();
                                    }else{
                                        Toast.makeText(AccountRegister.this,"Fail to register! Try again!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }else{
                    Toast.makeText(AccountRegister.this,"Fail to register! Try again!",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}