package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

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

public class Home2Activity extends AppCompatActivity {

    private SearchView home_search;

    RecyclerView rv;

    private DatabaseReference inv_item_ref;
    private StorageReference inv_img_ref;

    private ArrayList<Item> itemArrayList;
    private Context mContext;

    private invRecyclerAdapter ra;
    private invRecyclerAdapter.RecyclerViewOnClickListener rvListener;
    private ImageView home, trade, inventory, profile;


    private String user;
    private Trade newTrade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        // get user email for filter and to pass to other activities
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            Log.d("USER", "Home2 onCreate: " + user);
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

        //search
        home_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        home.setOnClickListener(v -> {
            Toast.makeText(Home2Activity.this, "Home Click", Toast.LENGTH_SHORT).show();
            //Intent homeIntent = new Intent(Home2Activity.this, Home2Activity.class);
            //startActivity(homeIntent);
        });

        trade.setOnClickListener(v -> {
            Toast.makeText(Home2Activity.this, "Trade Click", Toast.LENGTH_SHORT).show();
            Intent tradeBlockIntent = new Intent(Home2Activity.this, TradeBlockActivity.class);
            tradeBlockIntent.putExtra("user", user);
            startActivity(tradeBlockIntent);
            finish();
        });

        inventory.setOnClickListener(v -> {
            Toast.makeText(Home2Activity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
            Intent invIntent = new Intent(Home2Activity.this, InventoryActivity.class);
            invIntent.putExtra("user", user);
            startActivity(invIntent);
            finish();
        });

        profile.setOnClickListener(v -> {
            Toast.makeText(Home2Activity.this, "Profile Click", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(Home2Activity.this, ProfileActivity.class);
            profileIntent.putExtra("user", user);
            startActivity(profileIntent);
            finish();
        });
        setRVOnClickListener();
    }

    private void searchList(String text) {
        ArrayList<Item> foundSearch = new ArrayList<>();
        for(Item item: itemArrayList){
            if(item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getTags().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())){
                foundSearch.add(item);
            }
        }
        if (foundSearch.isEmpty()){
            Toast.makeText(Home2Activity.this, "No items found", Toast.LENGTH_SHORT).show();
        }
        else {
            ra.setFoundSearch(foundSearch);
        }
    }

    private void GetItem(){

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
                    // only add items to list that do not belong to user
                    if (!Objects.equals(item.getUser(), user)) {
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
        //setRVOnClickListener();
    }

    private void setRVOnClickListener() {
        rvListener = new invRecyclerAdapter.RecyclerViewOnClickListener() {
            @Override
            public void onClick(View v, int pos) {
                // this click will go to fresh trade activity with chosen item
                Toast.makeText(Home2Activity.this, "Click success", Toast.LENGTH_SHORT).show();

                Intent tradeIntent = new Intent(Home2Activity.this, TradeActivity.class);
                //save trade details
                String otherUser = itemArrayList.get(pos).getUser();
                String itemID = itemArrayList.get(pos).getID();
                newTrade = new Trade(itemID, user, otherUser, true, false);
                tradeIntent.putExtra("user", user);
                tradeIntent.putExtra("trade", newTrade);
                //set trade ID of item to associate it with new trade
                //TODO: pull item with itemID and set the tradeID to the tradeID of newTrade
                startActivity(tradeIntent);
                finish();

            }
        };
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
        //search
        home_search = findViewById(R.id.home_search);
        home_search.clearFocus();

        rv = findViewById(R.id.home_rv);
        home = findViewById(R.id.home_btn);
        trade = findViewById(R.id.tradeblock_btn);
        inventory = findViewById(R.id.inventory_btn);
        profile = findViewById(R.id.profile_btn);
    }
}