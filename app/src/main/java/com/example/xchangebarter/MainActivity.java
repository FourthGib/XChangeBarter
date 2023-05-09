package com.example.xchangebarter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xchangebarter.Actor.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText login_email, login_password;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim();
                String password = login_password.getText().toString().trim();

                // perform email and password validation here
                // for example, check if email and password are not empty
                if (email.isEmpty()) {
                    login_email.setError("Email is required");
                    login_email.requestFocus();
                }

                else if (password.isEmpty()) {
                    login_password.setError("Password is required");
                    login_password.requestFocus();
                }
                else {
                    // for simplicity, we will just show a Toast message to indicate success
                    //Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    // if email and password are valid, log the user in
                    // you can use Firebase Authentication or your own backend API here
                    // create an Intent to start the HomePageActivity
                    //Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                    //startActivity(intent);

                    // finish the current activity to prevent the user from going back to the login screen
                    CheckUser(email, password);
                }

            }

            private void CheckUser(String email, String password){
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Users").child(email).exists()) {
                            Users usersData = snapshot.child("Users").child(email).getValue(Users.class);
                            assert usersData != null;
                            if(usersData.getEmail().equals(email)) {
                                if(usersData.getPassword().equals(password)) {
                                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, Home2Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Wrong Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        registerButton.setOnClickListener(v -> {
            // open registration activity or fragment here
            // for example, you can start a new activity using an Intent
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            // finish the current activity to prevent the user from going back to the login screen
            finish();
        });
    }
}