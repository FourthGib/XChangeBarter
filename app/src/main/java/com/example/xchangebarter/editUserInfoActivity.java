package com.example.xchangebarter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity class for editing user information.
 */

public class editUserInfoActivity extends AppCompatActivity {

    private EditText edit_name, edit_email;

    private ImageView edit_pfp;

    private Button edit_save, back_to_prof;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        init();

        back_to_prof.setOnClickListener(v -> {
            Toast.makeText(editUserInfoActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
            Intent toProfileIntent = new Intent(editUserInfoActivity.this, ProfileActivity.class);
            startActivity(toProfileIntent);
            finish();
        });
    }

    private void init(){
        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        edit_pfp = findViewById(R.id.edit_pfp);
        edit_save = findViewById(R.id.edit_save_info);
        back_to_prof = findViewById(R.id.edit_back_to_prof);
    }
}