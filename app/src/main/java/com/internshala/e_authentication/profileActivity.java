package com.internshala.e_authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class profileActivity extends AppCompatActivity {
    private FirebaseUser user;

    private DatabaseReference reference;
    private String userId;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(profileActivity.this,MainActivity.class));
            }
        });
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=user.getUid();
        final TextView textViewFullName = (TextView) findViewById(R.id.fullName);
        final TextView textViewEmail = (TextView) findViewById(R.id.emailAddress);
        final TextView textViewAge = (TextView) findViewById(R.id.age);
        final TextView textViewPhone = (TextView) findViewById(R.id.phoneNumber);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile!=null)
                {
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;
                    String phone = userProfile.phone;
                    textViewFullName.setText("Name: " +fullName);
                    textViewEmail.setText("Email Address: "+email);
                    textViewAge.setText("Age: " +age);
                    textViewPhone.setText("Phone Number: " + phone);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profileActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();

            }
        });
    }
}