package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


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
                    return;
                }

                else if (email.isEmpty()) {
                    rgstr_email.setError("Email is required");
                    rgstr_email.requestFocus();
                    return;
                }

                else if (password.isEmpty()) {
                    rgstr_password.setError("Password is required");
                    rgstr_password.requestFocus();
                    return;
                }

                else {
                    // if all fields are valid, register the user
                    // you can use Firebase Authentication or your own backend API here
                    // for simplicity, we will just show a Toast message to indicate success
                    /*
                    Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                     */

                    CheckEmail(fullName,email, password);

                }
            }

            private void CheckEmail( String fullName, String email, String password){
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!(snapshot.child("Users").child(email).exists())){
                            HashMap<String, Object> userInfo = new HashMap<>();
                            userInfo.put("email",email);
                            userInfo.put("name",fullName);
                            userInfo.put("password",password);

                            RootRef.child("Users").child(email).updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Email already Registered", Toast.LENGTH_SHORT).show();

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