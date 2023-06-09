package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.*;

public class RegisterActivity extends AppCompatActivity {
    private EditText rgstr_name, rgstr_email, rgstr_password;
    private Button rgstr_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rgstr_name = findViewById(R.id.rgstr_name);
        rgstr_email = findViewById(R.id.rgstr_email);
        rgstr_password = findViewById(R.id.rgstr_password);
        rgstr_button = findViewById(R.id.rgstr_button);

        rgstr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = rgstr_name.getText().toString().trim();
                String email = rgstr_email.getText().toString().trim();
                String password = rgstr_password.getText().toString().trim();

                // perform registration validation here
                // for example, check if all fields are not empty
                if (fullName.isEmpty()) {
                    rgstr_name.setError("Full name is required");
                    rgstr_name.requestFocus();
                }

                else if (email.isEmpty()) {
                    rgstr_email.setError("Email is required");
                    rgstr_email.requestFocus();
                }

                else if (password.isEmpty()) {
                    rgstr_password.setError("Password is required");
                    rgstr_password.requestFocus();
                }

                else {
                    CheckEmail(fullName,email, password);
                }
            }

            private void CheckEmail( String fullName, String email, String password){
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check if csusm email
                        Pattern p = Pattern.compile("\\w+\\d{3}@csusm.edu");
                        Matcher m = p.matcher(email);
                        HashMap<String, Object> userInfo = new HashMap<>();
                        if (m.matches()) {
                            // take @csusm.edu off email string to allow firebase to add
                            int atIndex = email.indexOf('@');
                            String userName = email.substring(0, atIndex);
                            if (!(snapshot.child("Users").child(userName).exists())) {
                                System.out.println(userName);
                                userInfo.put("email", userName);
                                userInfo.put("name", fullName);
                                userInfo.put("password", password);

                                RootRef.child("Users").child(userName).updateChildren(userInfo).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            } else {    // already exists
                                Toast.makeText(RegisterActivity.this, "Email already Registered", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {    // is not csusm email
                            Toast.makeText(RegisterActivity.this, "Needs to be a valid CSUSM email", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}