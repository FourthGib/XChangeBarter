package com.example.xchangebarter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchangebarter.Item.Item;
import com.example.xchangebarter.Trade.Trade;
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

    private DatabaseReference inv_item_ref;
    private StorageReference inv_img_ref;
    private ArrayList<Item> itemArrayList;
    private ImageView tb_home, tb_tradeblock, tb_inventory, tb_profile;

    private RecyclerView active_rv;
    private RecyclerView pending_rv;
    private invRecyclerAdapter ra;
    private invRecyclerAdapter.RecyclerViewOnClickListener rvListener;
    
    private String user;
    private Trade trade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_block);
        // get user email for filter and to pass to other activities
        // get trade information in case returned from sent trade or countered trade
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            trade = extras.getParcelable("trade");
        }

        init();

        LinearLayoutManager activeLLM = new LinearLayoutManager(this);
        active_rv.setLayoutManager(activeLLM);
        active_rv.setHasFixedSize(true);

        LinearLayoutManager pendingLLM = new LinearLayoutManager(this);
        pending_rv.setLayoutManager(pendingLLM);
        pending_rv.setHasFixedSize(true);

        inv_item_ref = FirebaseDatabase.getInstance().getReference();
        inv_img_ref = FirebaseStorage.getInstance().getReference();

        itemArrayList = new ArrayList<>();

        Clear();

        ActiveGetItem(active_rv);
        PendingGetItem(pending_rv);



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

    private void Clear() {
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

    private void ActiveGetItem(RecyclerView rv){

        Query query = inv_item_ref.child("otherItemInfo");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Item item = new Item();
                    item.setID(Objects.requireNonNull(snap.child("itemID").getValue()).toString());
                    item.setImgUrl(Objects.requireNonNull(snap.child("image").getValue()).toString());
                    item.setName(Objects.requireNonNull(snap.child("title").getValue()).toString());
                    item.setDescription(Objects.requireNonNull(snap.child("description").getValue()).toString());
                    item.setTags(Objects.requireNonNull(snap.child("tags").getValue()).toString());
                    item.setUser(Objects.requireNonNull(snap.child("user").getValue()).toString());
                    item.setTradeID(Objects.requireNonNull(snap.child("tradeID").getValue()).toString());
                    // TODO:only add items to list that user has been sent for trade

                }

                ra = new invRecyclerAdapter(getApplicationContext(), itemArrayList, rvListener);
                rv.setAdapter(ra);
                setActiveRVOnClickListener();
                ra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setActiveRVOnClickListener() {
        rvListener = new invRecyclerAdapter.RecyclerViewOnClickListener() {
            @Override
            public void onClick(View v, int pos) {
                // this click will go to trade activity associated with chosen item
                Intent tradeIntent = new Intent(TradeBlockActivity.this, TradeActivity.class);
                //save trade details
                String otherUser = itemArrayList.get(pos).getUser();
                String itemID = itemArrayList.get(pos).getID();
                trade = new Trade(itemID, user, otherUser, true, false);
                tradeIntent.putExtra("user", user);
                tradeIntent.putExtra("trade", trade);
                startActivity(tradeIntent);

            }
        };
    }

    private void PendingGetItem(RecyclerView rv){

        Query query = inv_item_ref.child("otherItemInfo");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Item item = new Item();
                    item.setID(Objects.requireNonNull(snap.child("itemID").getValue()).toString());
                    item.setImgUrl(Objects.requireNonNull(snap.child("image").getValue()).toString());
                    item.setName(Objects.requireNonNull(snap.child("title").getValue()).toString());
                    item.setDescription(Objects.requireNonNull(snap.child("description").getValue()).toString());
                    item.setTags(Objects.requireNonNull(snap.child("tags").getValue()).toString());
                    item.setUser(Objects.requireNonNull(snap.child("user").getValue()).toString());
                    //TODO:only add items to list that the user has sent out for trade

                }

                ra = new invRecyclerAdapter(getApplicationContext(), itemArrayList, rvListener);
                rv.setAdapter(ra);
                setPendingRVOnClickListener();
                ra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setPendingRVOnClickListener() {
        rvListener = new invRecyclerAdapter.RecyclerViewOnClickListener() {
            @Override
            public void onClick(View v, int pos) {
                // this click will go to trade activity associated with chosen item
                Intent tradeIntent = new Intent(TradeBlockActivity.this, TradeActivity.class);
                //save trade details
                String otherUser = itemArrayList.get(pos).getUser();
                String itemID = itemArrayList.get(pos).getID();
                trade = new Trade(itemID, user, otherUser, true, false);
                tradeIntent.putExtra("user", user);
                tradeIntent.putExtra("trade", trade);
                startActivity(tradeIntent);

            }
        };
    }


    private void init(){
        tb_home = findViewById(R.id.tb_home_btn);
        tb_tradeblock = findViewById(R.id.tb_tradeblock_btn);
        tb_inventory = findViewById(R.id.tb_inventory_btn);
        tb_profile = findViewById(R.id.tb_profile_btn);
    }
}