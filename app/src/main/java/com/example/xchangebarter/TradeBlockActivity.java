package com.example.xchangebarter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TradeBlockActivity extends AppCompatActivity {

    private ImageView tb_home, tb_tradeblock, tb_inventory, tb_profile;

    private RecyclerView active_rv;
    private RecyclerView pending_rv;

    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_block);
        // get user email for filter and to pass to other activities
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
        }

        init();

        tb_home.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(TradeBlockActivity.this, Home2Activity.class);
            homeIntent.putExtra("user", user);
            startActivity(homeIntent);
        });

        tb_tradeblock.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "You are on Trade Block", Toast.LENGTH_SHORT).show();
            /*Intent tbIntent = new Intent(TradeBlockActivity.this, TradeBlockActivity.class);
            startActivity(tbIntent);
            finish();*/
        });

        tb_inventory.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
            Intent invIntent = new Intent(TradeBlockActivity.this, InventoryActivity.class);
            startActivity(invIntent);
            invIntent.putExtra("user", user);
            finish();
        });

        tb_profile.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(TradeBlockActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
            profileIntent.putExtra("user", user);
            finish();
        });
    }

    private void init(){
        tb_home = findViewById(R.id.tb_home_btn);
        tb_tradeblock = findViewById(R.id.tb_tradeblock_btn);
        tb_inventory = findViewById(R.id.tb_inventory_btn);
        tb_profile = findViewById(R.id.tb_profile_btn);
    }
}