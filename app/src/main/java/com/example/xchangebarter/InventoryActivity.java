package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class InventoryActivity extends AppCompatActivity {

    RecyclerView rv;

    private DatabaseReference inv_item_ref;
    private StorageReference inv_img_ref;

    private ArrayList<Item> itemArrayList;
    private Context mContext;

    private invRecyclerAdapter ra;
    private Button back;
    private ImageView inv_home, inv_bell, inv_inventory, inv_profile, inv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        inv_item_ref = FirebaseDatabase.getInstance().getReference();
        inv_img_ref = FirebaseStorage.getInstance().getReference();

        itemArrayList = new ArrayList<>();

        Clear();

        GetItem();


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

    private void GetItem(){

        Query query = inv_item_ref.child("otherItemInfo");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Item item = new Item();
                    item.setImgUrl(snap.child("image").getValue().toString());
                    item.setName(snap.child("title").getValue().toString());
                    item.setDescription(snap.child("description").getValue().toString());
                    item.setTags(snap.child("tags").getValue().toString());

                    itemArrayList.add(item);
                }

                ra = new invRecyclerAdapter(getApplicationContext(), itemArrayList);
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
        inv_bell = findViewById(R.id.inv_notification_btn);
        inv_inventory = findViewById(R.id.inv_inventory_btn);
        inv_profile = findViewById(R.id.inv_profile_btn);
        inv_add = findViewById(R.id.inv_add_btn);
    }
}