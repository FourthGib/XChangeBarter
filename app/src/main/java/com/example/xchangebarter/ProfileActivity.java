package com.example.xchangebarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
// import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    // private TextView username;

    private Button logout;

    private ImageView prof_home, prof_trade, prof_inventory, prof_profile;

    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // get user email for filter and to pass to other activities
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
        }

        init();

        logout.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
            Intent logoutIntent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(logoutIntent);
            finish();
        });

        /**
        * *change_user_info.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Change User Info", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, editUserInfoActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        });
         */

        prof_home.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(ProfileActivity.this, Home2Activity.class);
            homeIntent.putExtra("user", user);
            startActivity(homeIntent);
            finish();
        });

        prof_trade.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Bell Click", Toast.LENGTH_SHORT).show();
            Intent tbIntent = new Intent(ProfileActivity.this, TradeBlockActivity.class);
            tbIntent.putExtra("user", user);
            startActivity(tbIntent);
            finish();
        });

        prof_inventory.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
            Intent invIntent = new Intent(ProfileActivity.this, InventoryActivity.class);
            invIntent.putExtra("user", user);
            startActivity(invIntent);
            finish();
        });

        prof_profile.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
        });
    }

    private void init(){
        logout = (Button) findViewById(R.id.prof_logout);
        prof_home = findViewById(R.id.prof_home_btn);
        prof_trade = findViewById(R.id.tb_tradeblock_btn);
        prof_inventory = findViewById(R.id.prof_inventory_btn);
        prof_profile = findViewById(R.id.prof_logout_btn);
    }
}