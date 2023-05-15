package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xchangebarter.Item.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryActivity extends AppCompatActivity {

    RecyclerView rv;

    private DatabaseReference inv_item_ref;
    private StorageReference inv_img_ref;

    private ArrayList<Item> itemArrayList;
    private Context mContext;

    private invRecyclerAdapter ra;
    private invRecyclerAdapter.RecyclerViewOnClickListener rvListener;
    private ImageView inv_home, inv_trade, inv_inventory, inv_profile, inv_add;

    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        // get user email from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            Log.d("USER", "Inventory onCreate: " + user);
        }
        init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        inv_item_ref = FirebaseDatabase.getInstance().getReference();
        inv_img_ref = FirebaseStorage.getInstance().getReference();

        itemArrayList = new ArrayList<>();

        Clear();

        GetItem();


        inv_home.setOnClickListener(v -> {
            Toast.makeText(InventoryActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(InventoryActivity.this, Home2Activity.class);
            homeIntent.putExtra("user", user);
            startActivity(homeIntent);
            finish();
        });

        inv_add.setOnClickListener(v -> {
            Toast.makeText(InventoryActivity.this, "Add Click", Toast.LENGTH_SHORT).show();
            Intent addIntent = new Intent(InventoryActivity.this, AddItemActivity.class);
            addIntent.putExtra("user", user);
            startActivity(addIntent);
            finish();
        });

        inv_trade.setOnClickListener(v -> {
            Toast.makeText(InventoryActivity.this, "Trade Click", Toast.LENGTH_SHORT).show();
            Intent tradeIntent = new Intent(InventoryActivity.this, TradeBlockActivity.class);
            tradeIntent.putExtra("user", user);
            startActivity(tradeIntent);
            finish();
        });

        inv_inventory.setOnClickListener(v -> {
            Toast.makeText(InventoryActivity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
            Intent invIntent = new Intent(InventoryActivity.this, InventoryActivity.class);
            invIntent.putExtra("user", user);
            startActivity(invIntent);
            finish();
        });

        inv_profile.setOnClickListener(v -> {
            Toast.makeText(InventoryActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(InventoryActivity.this, ProfileActivity.class);
            profileIntent.putExtra("user", user);
            startActivity(profileIntent);
            finish();
        });
    }

    private void GetItem(){

        Query query = inv_item_ref.child("otherItemInfo");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Item item = new Item();
                    item.setItemID(Objects.requireNonNull(snap.child("itemID").getValue()).toString());
                    item.setImage(Objects.requireNonNull(snap.child("image").getValue()).toString());
                    item.setTitle(Objects.requireNonNull(snap.child("title").getValue()).toString());
                    item.setDescription(Objects.requireNonNull(snap.child("description").getValue()).toString());
                    item.setTags(Objects.requireNonNull(snap.child("tags").getValue()).toString());
                    item.setUser(Objects.requireNonNull(snap.child("user").getValue()).toString());
                    // only show in inventory if item belongs to user
                    if (Objects.equals(item.getUser(), user)){
                        itemArrayList.add(item);
                    }
                }

                ra = new invRecyclerAdapter(getApplicationContext(), itemArrayList, rvListener);
                rv.setAdapter(ra);
                ra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void Clear(){
        if(itemArrayList != null){
            itemArrayList.clear();
            if(ra!=null){
                ra.notifyDataSetChanged();
            }
        }
        else{
            itemArrayList = new ArrayList<>();
        }
    }

    private void init(){
        rv = findViewById(R.id.inv_rv);

        inv_home = findViewById(R.id.inv_home_btn);
        inv_trade = findViewById(R.id.tb_tradeblock_btn);
        inv_inventory = findViewById(R.id.inv_inventory_btn);
        inv_profile = findViewById(R.id.inv_profile_btn);
        inv_add = findViewById(R.id.inv_add_btn);
    }
}