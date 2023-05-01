package com.example.xchangebarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Home2Activity extends AppCompatActivity {

    private ImageView home, bell, inventory, profile;

    private Button exitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        exitBtn = (Button) findViewById(R.id.button);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(Home2Activity.this, MainActivity.class);
                startActivity(logout);
            }
        });
        init();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home2Activity.this, "Home Click", Toast.LENGTH_SHORT).show();
                //Intent homeIntent = new Intent(Home2Activity.this, Home2Activity.class);
                //startActivity(homeIntent);
            }
        });

        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home2Activity.this, "Bell Click", Toast.LENGTH_SHORT).show();
                Intent bellIntent = new Intent(Home2Activity.this, NotificationActivity.class);
                startActivity(bellIntent);
                finish();
            }
        });

        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home2Activity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
                Intent invIntent = new Intent(Home2Activity.this, InventoryActivity.class);
                startActivity(invIntent);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home2Activity.this, "Profile Click", Toast.LENGTH_SHORT).show();
                Intent profileIntent = new Intent(Home2Activity.this, ProfileActivity.class);
                startActivity(profileIntent);
                finish();
            }
        });
    }

    private void init(){
        home = findViewById(R.id.home_btn);
        bell = findViewById(R.id.notification_btn);
        inventory = findViewById(R.id.inventory_btn);
        profile = findViewById(R.id.profile_btn);
    }
}