package com.example.xchangebarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private Button back;

    private ImageView prof_home, prof_bell, prof_inventory, prof_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        back = (Button) findViewById(R.id.profile_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(ProfileActivity.this, Home2Activity.class);
                startActivity(homeIntent);
                finish();
            }
        });
        init();

        prof_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(ProfileActivity.this, Home2Activity.class);
                startActivity(homeIntent);
            }
        });

        prof_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Bell Click", Toast.LENGTH_SHORT).show();
                Intent bellIntent = new Intent(ProfileActivity.this, NotificationActivity.class);
                startActivity(bellIntent);
                finish();
            }
        });

        prof_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
                Intent invIntent = new Intent(ProfileActivity.this, InventoryActivity.class);
                startActivity(invIntent);
                finish();
            }
        });

        prof_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
                Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                finish();
            }
        });
    }

    private void init(){
        prof_home = findViewById(R.id.prof_home_btn);
        prof_bell = findViewById(R.id.prof_notification_btn);
        prof_inventory = findViewById(R.id.prof_inventory_btn);
        prof_profile = findViewById(R.id.prof_profile_btn);
    }
}