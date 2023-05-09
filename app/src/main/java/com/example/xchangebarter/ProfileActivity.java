package com.example.xchangebarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private TextView username;

    private Button logout, trade_hist, change_user_info;

    private ImageView prof_home, prof_trade, prof_inventory, prof_profile, prof_pfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        logout.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
            Intent logoutIntent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(logoutIntent);
            finish();
        });

        change_user_info.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Change User Info", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, editUserInfoActivity.class);
            startActivity(intent);
            finish();
        });

        prof_home.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(ProfileActivity.this, Home2Activity.class);
            startActivity(homeIntent);
        });

        prof_trade.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Bell Click", Toast.LENGTH_SHORT).show();
            Intent bellIntent = new Intent(ProfileActivity.this, NotificationActivity.class);
            startActivity(bellIntent);
            finish();
        });

        prof_inventory.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
            Intent invIntent = new Intent(ProfileActivity.this, InventoryActivity.class);
            startActivity(invIntent);
            finish();
        });

        prof_profile.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
            finish();
        });
    }

    private void init(){
        prof_home = findViewById(R.id.prof_home_btn);
        prof_trade = findViewById(R.id.notif_tradeblock_btn);
        prof_inventory = findViewById(R.id.prof_inventory_btn);
        prof_profile = findViewById(R.id.prof_profile_btn);
        logout = findViewById(R.id.prof_logout);
        trade_hist = findViewById(R.id.prof_trade_hist);
        change_user_info = findViewById(R.id.prof_user_info);
        prof_pfp = findViewById(R.id.prof_pfp);
    }
}