package com.internshala.e_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private TextView banner;
    private Button registerUser;
    private EditText editTextFullName,editTextAge,editTextEmail,editTextPassword,editTextPhone;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();

        banner=(TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser=(Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName=(EditText) findViewById(R.id.fullName);
        editTextAge=(EditText) findViewById(R.id.age);
        editTextEmail=(EditText) findViewById(R.id.email);
        editTextPassword=(EditText) findViewById(R.id.password);
        editTextPhone=(EditText) findViewById(R.id.phoneNumber);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;


        }

    }

    private void registerUser() {
        String email= editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();
        String fullName=editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        if(fullName.isEmpty())
        {
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
            return;
        }
        if(age.isEmpty())
        {
            editTextAge.setError("Age name is required");
            editTextAge.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            editTextPassword.setError("Password length must be atleast 6");
            editTextPassword.requestFocus();
            return;
        }
        if(phone.isEmpty() || phone.length()<10)
        {
            editTextPhone.setError("Enter Valid Phone Number");
            editTextPhone.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullName, age, email,phone);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(RegisterUser.this, "Failed to register Try Again", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, "Failed to register ", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}