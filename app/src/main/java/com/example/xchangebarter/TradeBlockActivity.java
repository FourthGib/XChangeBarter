package com.example.xchangebarter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Objects;

public class TradeBlockActivity extends AppCompatActivity {

    private DatabaseReference trade_item_ref;

    private ImageView tb_home, tb_tradeblock, tb_inventory, tb_profile;

    RecyclerView ongoing_rv;
    private ArrayList<Trade> ongoingArrayList;
    RecyclerView finished_rv;
    private ArrayList<Trade> finishedArrayList;
    private tradeRecyclerAdapter ra;    // for ongoing
    private tradeRecyclerAdapter fra;   // for finished
    private tradeRecyclerAdapter.RecyclerViewOnClickListener ongoingRVListener;
    private tradeRecyclerAdapter.RecyclerViewOnClickListener finishedRVListener;
    
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_block);
        // get user email for filter and to pass to other activities
        // get trade information in case returned from sent trade or countered trade
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
        }

        init();

        LinearLayoutManager activeLLM = new LinearLayoutManager(this);
        ongoing_rv.setLayoutManager(activeLLM);
        ongoing_rv.setHasFixedSize(true);

        LinearLayoutManager pendingLLM = new LinearLayoutManager(this);
        finished_rv.setLayoutManager(pendingLLM);
        finished_rv.setHasFixedSize(true);

        trade_item_ref = FirebaseDatabase.getInstance().getReference();

        ongoingArrayList = new ArrayList<>();

        Clear();

        GetTrade(ongoing_rv, finished_rv);

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
        setOngoingRVOnClickListener();
        setFinishedOnClickListener();
    }


    private void Clear() {
        if(ongoingArrayList != null){
            ongoingArrayList.clear();
            if(ra!=null){
                ra.notifyDataSetChanged();
            }
        }
        else{
            ongoingArrayList = new ArrayList<>();
        }
        if(finishedArrayList != null){
            finishedArrayList.clear();
            if(fra!=null){
                fra.notifyDataSetChanged();
            }
        }
        else{
            finishedArrayList = new ArrayList<>();
        }
    }

    private void GetTrade(RecyclerView orv, RecyclerView frv){

        Query query = trade_item_ref.child("trades");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Clear();
                for(DataSnapshot snap:snapshot.getChildren()){
                    Trade trade = new Trade();

                    trade.setTradeID(Objects.requireNonNull(snap.child("tradeID").getValue()).toString());
                    trade.setReceiverItem(Objects.requireNonNull(snap.child("receive_item_ID").getValue()).toString());
                    trade.setReceiverItemTitle(Objects.requireNonNull(snap.child("receive_item_title").getValue()).toString());
                    trade.setInitiatorItem(Objects.requireNonNull(snap.child("initiator_item_ID").getValue()).toString());
                    trade.setInitiatorItemTitle(Objects.requireNonNull(snap.child("initiator_item_title").getValue()).toString());
                    trade.setReceiver(Objects.requireNonNull(snap.child("receiver").getValue()).toString());
                    trade.setInitiator(Objects.requireNonNull(snap.child("initiator").getValue()).toString());
                    trade.setStatus(Objects.requireNonNull(snap.child("status").getValue()).toString());
                    trade.setPlace(Objects.requireNonNull(snap.child("place").getValue()).toString());
                    trade.setrCompletion(Objects.requireNonNull(snap.child("rCompletion").getValue()).toString());
                    trade.setiCompletion(Objects.requireNonNull(snap.child("iCompletion").getValue()).toString());
                    if (trade.getiCompletion().equalsIgnoreCase("ongoing") ||
                            trade.getrCompletion().equalsIgnoreCase("ongoing")) {
                        ongoingArrayList.add(trade);
                    } else {
                        finishedArrayList.add(trade);
                    }

                }

                ra = new tradeRecyclerAdapter(getApplicationContext(), ongoingArrayList, ongoingRVListener);
                orv.setAdapter(ra);
                fra = new tradeRecyclerAdapter(getApplicationContext(), finishedArrayList, finishedRVListener);
                frv.setAdapter(fra);

                ra.notifyDataSetChanged();
                fra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setOngoingRVOnClickListener() {
        ongoingRVListener = (v, pos) -> {
            // this click will go to trade activity associated with chosen item
            Intent approvalIntent = new Intent(TradeBlockActivity.this, ApprovalActivity.class);
            //save trade details
            Trade ongoingTrade = ongoingArrayList.get(pos);
            approvalIntent.putExtra("user", user);
            approvalIntent.putExtra("trade", ongoingTrade);
            startActivity(approvalIntent);

        };
    }

    private void setFinishedOnClickListener(){
        finishedRVListener = (v, pos) -> {
            // this click will go to trade activity associated with chosen item
            Intent approvalIntent = new Intent(TradeBlockActivity.this, ApprovalActivity.class);
            //save trade details
            Trade finishedTrade = finishedArrayList.get(pos);
            approvalIntent.putExtra("user", user);
            approvalIntent.putExtra("trade", finishedTrade);
            startActivity(approvalIntent);
        };
    }
    private void init(){
        ongoing_rv = findViewById(R.id.ongoing_rv);
        finished_rv = findViewById(R.id.finished_rv);
        tb_home = findViewById(R.id.tb_home_btn);
        tb_tradeblock = findViewById(R.id.tb_tradeblock_btn);
        tb_inventory = findViewById(R.id.tb_inventory_btn);
        tb_profile = findViewById(R.id.tb_profile_btn);
    }
}