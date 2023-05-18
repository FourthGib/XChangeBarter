package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xchangebarter.Item.Item;
import com.example.xchangebarter.Trade.Trade;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class ApprovalActivity extends AppCompatActivity {
    private DatabaseReference inv_item_ref, received_item_ref, given_item_ref, trade_ref;
    private StorageReference inv_img_ref;

    private Button back, accept, counter, reject;
    private TextView received_item_name, received_item_description, received_item_tags;
    private TextView given_item_name, given_item_description, given_item_tags;
    private ImageView received_item_image, given_item_image;
    private String received_item_imageUrl, given_item_imageUrl;
    Item received_item, given_item;
    private String user;
    private Trade trade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        Bundle extras = getIntent().getExtras();
        // bring in persistent info from previous page
        if (extras != null) {
            user = extras.getString("user");
            Log.d("USER", "Approval onCreate: " + user);
            trade = extras.getParcelable("trade");
            Log.d("O_USER", "Approval onCreate: " + trade.getReceiver());
            Log.d("O_ITEM", "Approval onCreate: " + trade.getReceiverItem());
            Log.d("TRADE_ID", "Approval onCreate: TradeID: " + trade.getTradeID());
        }

        // initialize buttons to correct layout resource
        back = (Button) findViewById(R.id.notif_back);
        accept = (Button) findViewById(R.id.acceptTradeBtn);
        reject = (Button) findViewById(R.id.rejectTradeBtn);
        counter = (Button) findViewById(R.id.counterTradeBtn);
        // Check if viewing a completed trade
        if (trade.getiCompletion().equalsIgnoreCase("complete") && trade.getrCompletion().equalsIgnoreCase("complete")){
            accept.setVisibility(View.INVISIBLE);
            accept.setEnabled(false);
            reject.setVisibility(View.INVISIBLE);
            reject.setEnabled(false);
            counter.setVisibility(View.INVISIBLE);
            counter.setEnabled(false);
        }

        // Check if user is the one who sent trade and if other user has not accepted yet
        if (trade.getInitiator().equalsIgnoreCase(user) && trade.getrCompletion().equalsIgnoreCase("ongoing")){
            // do not allow initiator to accept trade yet
            accept.setEnabled(false);
        }

        back.setOnClickListener(v -> {
            Toast.makeText(ApprovalActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(ApprovalActivity.this, TradeBlockActivity.class);
            homeIntent.putExtra("user", user);
            startActivity(homeIntent);
            finish();
        });

        // initialize all views being used
        init();

        // get references to database
        inv_item_ref = FirebaseDatabase.getInstance().getReference();
        trade_ref = FirebaseDatabase.getInstance().getReference();
        inv_img_ref = FirebaseStorage.getInstance().getReference();

        // get item info from database
        received_item_ref = inv_item_ref.child("otherItemInfo").child(trade.getReceiverItem());
        given_item_ref = inv_item_ref.child("otherItemInfo").child(trade.getInitiatorItem());

        received_item_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // Item exists in the database, retrieve its information
                    received_item = snapshot.getValue(Item.class);
                    if (received_item != null) {
                        received_item_name.setText(received_item.getTitle());
                        received_item_description.setText(received_item.getDescription());
                        received_item_tags.setText(received_item.getTags());
                        received_item_imageUrl = received_item.getImage();
                        Glide.with(getApplicationContext())
                                .load(received_item_imageUrl)
                                .into(received_item_image);


                        //Toast.makeText(ApprovalActivity.this, "Item user: " + received_item.getItemID(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ApprovalActivity.this, "Item is null", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ApprovalActivity.this, "Item not found", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ApprovalActivity.this, "Failed to get item", Toast.LENGTH_LONG).show();
            }
        });

        given_item_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // Item exists in the database, retrieve its information
                    given_item = snapshot.getValue(Item.class);
                    if (given_item != null) {
                        given_item_name.setText(given_item.getTitle());
                        given_item_description.setText(given_item.getDescription());
                        given_item_tags.setText(given_item.getTags());
                        given_item_imageUrl = given_item.getImage();
                        Glide.with(getApplicationContext())
                                .load(given_item_imageUrl)
                                .into(given_item_image);


                        //Toast.makeText(ApprovalActivity.this, "Item user: " + given_item.getItemID(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ApprovalActivity.this, "Item is null", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ApprovalActivity.this, "Item not found", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ApprovalActivity.this, "Failed to get item", Toast.LENGTH_LONG).show();
            }
        });


        // accept the proposed trade -> Trade Block
        accept.setOnClickListener(v -> {
            // check if accepting a counter offer or if trade was already accepted by receiver
            if (trade.isCountered() || trade.getrCompletion().equalsIgnoreCase("complete")){
                trade.setiCompletion("complete");
            } else{
                trade.setrCompletion("complete");
            }
            trade.setIncoming(true);
            trade.setFresh(false);
            // if this fully completes the trade remove items from database
            if (trade.getiCompletion().equalsIgnoreCase("complete") && trade.getrCompletion().equalsIgnoreCase("complete")){
                trade.setStatus("Complete: Accepted by " + trade.getReceiver());
                given_item_ref.child("complete").setValue(true);
                received_item_ref.child("complete").setValue(true);
            } else{
                trade.setStatus("Pending: Accepted by " + trade.getReceiver());
            }
            createTradeResponse();  //updates the database
            Intent tb_intent = new Intent(ApprovalActivity.this, TradeBlockActivity.class);
            tb_intent.putExtra("user", user);
            startActivity(tb_intent);
            finish();
        });

        // reject the proposed trade -> Trade Block
        reject.setOnClickListener(v -> {
            trade.setrCompletion("cancelled");
            trade.setFresh(false);
            trade.setStatus("Cancelled: Rejected by " + trade.getReceiver());
            trade.setRejected(true);
            createTradeResponse();
            //set available back to true for both items
            received_item_ref.child("available").setValue(true);
            given_item_ref.child("available").setValue(true);
            Intent tb_intent = new Intent(ApprovalActivity.this, TradeBlockActivity.class);
            tb_intent.putExtra("user", user);
            startActivity(tb_intent);
            finish();
        });

        // make counter offer -> Trade Activity
        counter.setOnClickListener(v -> {
            trade.setCountered(true);
            trade.setFresh(false);
            trade.setIncoming(true);
            trade.setStatus("Pending: Counter Offer by " + trade.getReceiver());
            // this user becomes new initiator
            trade.setInitiator(trade.getReceiver());
            trade.setReceiverItem(trade.getInitiatorItem());
            trade.setReceiverItemTitle(trade.getInitiatorItemTitle());
            // set previous initiator item as available again
            given_item_ref.child("available").setValue(true);
            // trade response will be the trade created in trade activity
            Intent trade_intent = new Intent(ApprovalActivity.this, TradeActivity.class);
            trade_intent.putExtra("user", user);
            trade_intent.putExtra("trade", trade);
            startActivity(trade_intent);
            finish();
        });
    }

    private void createTradeResponse() {
        String received_item_id = trade.getReceiverItem();
        String received_item_title = trade.getReceiverItemTitle();
        String initiator_item_id = trade.getInitiatorItem();
        String initiator_item_title = trade.getInitiatorItemTitle();
        String trade_ID = trade.getTradeID();
        String trade_initiator = trade.getInitiator();
        String trade_receiver = trade.getReceiver();
        String trade_place = trade.getPlace();
        String trade_status = trade.getStatus();
        String receiver_completion_status = trade.getrCompletion(); //can be ongoing or complete (ongoing by default)
        String initiator_completion_status = trade.getiCompletion();//if both receiver and initiator are complete, trade_status can be set to complete


        //organize data in hash
        HashMap<String, Object> tradeInfo = new HashMap<>();
        tradeInfo.put("tradeID", trade_ID); //unique tradeID
        tradeInfo.put("receive_item_ID", received_item_id); //item given by receiver
        tradeInfo.put("receive_item_title", received_item_title);   //name of item
        tradeInfo.put("initiator_item_ID", initiator_item_id);   //item given by initiator
        tradeInfo.put("initiator_item_title", initiator_item_title);   //name of item
        tradeInfo.put("receiver", trade_receiver);  //person who receives trade
        tradeInfo.put("initiator", trade_initiator); //person who initiates trade
        tradeInfo.put("place", trade_place);    //place where users will trade
        tradeInfo.put("status", trade_status); //current status of the trade: pending, active, offer, cancelled, or complete
        tradeInfo.put("rCompletion", receiver_completion_status); //receiver completion
        tradeInfo.put("iCompletion", initiator_completion_status); //initiator completion
        //send data to data base
        trade_ref.child("trades").child(trade_ID).updateChildren(tradeInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ApprovalActivity.this, "Trade offer was processed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ApprovalActivity.this, "Error: Trade wasn't saved in the database", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void init(){
        received_item_name = findViewById(R.id.received_item_name);
        received_item_description = findViewById(R.id.received_item_description);
        received_item_tags = findViewById(R.id.received_item_tags);
        received_item_image = findViewById(R.id.trade_for_rv);
        given_item_name = findViewById(R.id.give_item_name);
        given_item_description = findViewById(R.id.give_item_description);
        given_item_tags = findViewById(R.id.give_item_tags);
        given_item_image = findViewById(R.id.trade_with_iv);
    }
}