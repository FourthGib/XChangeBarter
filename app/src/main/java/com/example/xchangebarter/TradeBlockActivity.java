package com.example.xchangebarter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class TradeBlockActivity extends AppCompatActivity {

    private Button back;
    private ImageView tb_home, tb_tradeblock, tb_inventory, tb_profile;

    private RecyclerView active_rv;
    private RecyclerView pending_rv;
    private DatabaseReference tb_item_ref;
    private StorageReference tb_img_ref;

    private ArrayList<Item> itemArrayList;
    private Context mContext;

    private invRecyclerAdapter tb_ra;

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
        back = (Button) findViewById(R.id.notif_back);

        back.setOnClickListener(v -> {
            Toast.makeText(TradeBlockActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(TradeBlockActivity.this, Home2Activity.class);
            startActivity(homeIntent);
            finish();
        });
        init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        active_rv.setLayoutManager(layoutManager);
        active_rv.setHasFixedSize(true);

        tb_item_ref = FirebaseDatabase.getInstance().getReference();
        tb_img_ref = FirebaseStorage.getInstance().getReference();

        itemArrayList = new ArrayList<>();

        Clear();

        GetItem();

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

    private void Clear(){
        if(itemArrayList != null){
            itemArrayList.clear();
            if(tb_ra!=null){
                tb_ra.notifyDataSetChanged();
            }
        }
        else{
            itemArrayList = new ArrayList<>();
        }
    }

    private void GetItem(){

        Query query = tb_item_ref.child("otherItemInfo");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Item item = new Item();
                    item.setImgUrl(Objects.requireNonNull(snap.child("image").getValue()).toString());
                    item.setName(Objects.requireNonNull(snap.child("title").getValue()).toString());
                    item.setDescription(Objects.requireNonNull(snap.child("description").getValue()).toString());
                    item.setTags(Objects.requireNonNull(snap.child("tags").getValue()).toString());
                    // TODO: Filter only the items that are being traded
                    /*if (Objects.equals(item.getUser(), user)){
                        itemArrayList.add(item);
                    }*/
                    itemArrayList.add(item);
                }

                tb_ra = new invRecyclerAdapter(getApplicationContext(), itemArrayList);
                active_rv.setAdapter(tb_ra);
                tb_ra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}