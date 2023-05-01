package com.example.xchangebarter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class InventoryActivity extends AppCompatActivity {
    private Button back;
    private ImageView inv_home, inv_bell, inv_inventory, inv_profile, inv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        back = (Button) findViewById(R.id.inv_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InventoryActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(InventoryActivity.this, Home2Activity.class);
                startActivity(homeIntent);
                finish();
            }

        });
        init();

        inv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InventoryActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(InventoryActivity.this, Home2Activity.class);
                startActivity(homeIntent);
            }
        });

        inv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InventoryActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
                Intent addIntent = new Intent(InventoryActivity.this, AddItemActivity.class);
                startActivity(addIntent);
            }
        });

        inv_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InventoryActivity.this, "Bell Click", Toast.LENGTH_SHORT).show();
                Intent bellIntent = new Intent(InventoryActivity.this, NotificationActivity.class);
                startActivity(bellIntent);
                finish();
            }
        });

        inv_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InventoryActivity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
                Intent invIntent = new Intent(InventoryActivity.this, InventoryActivity.class);
                startActivity(invIntent);
                finish();
            }
        });

        inv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InventoryActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
                Intent profileIntent = new Intent(InventoryActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                finish();
            }
        });
    }

    private void init(){
        inv_home = findViewById(R.id.inv_home_btn);
        inv_bell = findViewById(R.id.inv_notification_btn);
        inv_inventory = findViewById(R.id.inv_inventory_btn);
        inv_profile = findViewById(R.id.inv_profile_btn);
        inv_add = findViewById(R.id.inv_add_btn);
    }
}