package com.example.xchangebarter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TradeBlockActivity extends AppCompatActivity {

    private Button back;

    private ImageView notif_home, notif_bell, notif_inventory, notif_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_block);
        back = (Button) findViewById(R.id.notif_back);

        back.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(TradeBlockActivity.this, Home2Activity.class);
            startActivity(homeIntent);
            finish();
        });
        init();

        notif_home.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(TradeBlockActivity.this, Home2Activity.class);
            startActivity(homeIntent);
        });

        notif_bell.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Bell Click", Toast.LENGTH_SHORT).show();
            Intent bellIntent = new Intent(TradeBlockActivity.this, TradeBlockActivity.class);
            startActivity(bellIntent);
            finish();
        });

        notif_inventory.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
            Intent invIntent = new Intent(TradeBlockActivity.this, InventoryActivity.class);
            startActivity(invIntent);
            finish();
        });

        notif_profile.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(TradeBlockActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
            finish();
        });
    }

    private void init(){
        notif_home = findViewById(R.id.notif_home_btn);
        notif_bell = findViewById(R.id.notif_tradeblock_btn);
        notif_inventory = findViewById(R.id.notif_inventory_btn);
        notif_profile = findViewById(R.id.notif_profile_btn);
    }
}