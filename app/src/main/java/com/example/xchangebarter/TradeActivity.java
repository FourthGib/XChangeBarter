package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xchangebarter.Item.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.example.xchangebarter.Trade.Trade;

public class TradeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference inv_item_ref;
    private StorageReference inv_img_ref;

    private ArrayList<Item> itemArrayList;

    final DatabaseReference trade_ref = FirebaseDatabase.getInstance().getReference();

    private Button back, send, accept, counter, reject;
    private ImageView trade_home, trade_tradeblock, trade_inventory, trade_profile, receive_item;

    private TextView otherUser;

    //received item fields
    private TextView received_item_name, received_item_description, received_item_tags;
    private ImageView received_item_image;
    private String received_item_imageUrl;

    Item received_item;
    private DatabaseReference received_item_ref;
    private Spinner placeSpin;
    private RecyclerView tt_rv;
    private invRecyclerAdapter ra;
    private invRecyclerAdapter.RecyclerViewOnClickListener rvListener;

    private String user, giveItemID, giveItemTitle, place, received_item_path;
    private boolean isChosen;   //boolean to check if item has been selected to give in trade
    ArrayList<String> places;

    private Trade trade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);
        // get user email for filter and to pass to other activities
        // get trade being made
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            Log.d("USER", "Trade onCreate: " + user);
            trade = extras.getParcelable("trade");
            Log.d("O_USER", "Trade onCreate: " + trade.getReceiver());
            Log.d("O_ITEM", "Trade onCreate: " + trade.getReceiverItem());
        }
        isChosen = false;
        placeSpin = (Spinner) findViewById(R.id.place_spinner);
        places = new ArrayList<>();
        places.add("Outside Library Floor 2");
        places.add("Starbucks");
        places.add("Outside USU");
        places.add("Outside Health Building");
        places.add("Extended Learning Building");
        places.add("Sports Center");
        places.add("University Commons");
        places.add("Science Building 1");
        places.add("Science Building 2");
        places.add("Viasat Engineering Pavilion");
        place = places.get(0);  // default to first option

        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, places);

        placeSpin.setAdapter(spin_adapter);

        placeSpin.setOnItemSelectedListener(this);

        back = (Button) findViewById(R.id.notif_back);
        send = (Button) findViewById(R.id.sendTradeBtn);



        send.setEnabled(false); // enable once item is chosen to give



        back.setOnClickListener(v -> {
            Toast.makeText(TradeActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(TradeActivity.this, Home2Activity.class);
            homeIntent.putExtra("user", user);
            startActivity(homeIntent);
            finish();
        });
        init();



        // One Recycler View to choose what to trade to the other user for their item.
        LinearLayoutManager tradeToLLM = new LinearLayoutManager(this);
        tt_rv.setLayoutManager(tradeToLLM);
        tt_rv.setHasFixedSize(true);

        inv_item_ref = FirebaseDatabase.getInstance().getReference();
        inv_img_ref = FirebaseStorage.getInstance().getReference();

        itemArrayList = new ArrayList<>();

        Clear();

        GetItem();

        //get item info from a database
        received_item_ref = inv_item_ref.child("otherItemInfo").child(trade.getReceiverItem());
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


                        //Toast.makeText(TradeActivity.this, "Item user: " + received_item.getItemID(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TradeActivity.this, "Item is null", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TradeActivity.this, "Item not found", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TradeActivity.this, "Failed to get item", Toast.LENGTH_LONG).show();
            }
        });



        // complete trade details and go to trade block to see that trade has been sent
        send.setOnClickListener(v -> {
            if (isChosen) {
                //create pending trade in the database
                createPendingTrade();
                //mark available fields for items as false
                received_item_ref.child("available").setValue(false);
                inv_item_ref.child("otherItemInfo").child(giveItemID).child("available").setValue(false);
                Toast.makeText(TradeActivity.this, "Send Clicked", Toast.LENGTH_SHORT).show();
                Intent tbIntent = new Intent(TradeActivity.this, TradeBlockActivity.class);
                tbIntent.putExtra("user", user);
                startActivity(tbIntent);
                finish();
            } else {
                Toast.makeText(TradeActivity.this, "Must Choose Item to Give!", Toast.LENGTH_LONG).show();
            }
        });



        trade_home.setOnClickListener(v -> {
            Toast.makeText(TradeActivity.this, "Home Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(TradeActivity.this, Home2Activity.class);
            homeIntent.putExtra("user", user);
            startActivity(homeIntent);
            finish();
        });

        trade_tradeblock.setOnClickListener(v -> {
            Toast.makeText(TradeActivity.this, "Trade Block Click", Toast.LENGTH_SHORT).show();
            Intent tbIntent = new Intent(TradeActivity.this, TradeBlockActivity.class);
            tbIntent.putExtra("user", user);
            startActivity(tbIntent);
            finish();
        });

        trade_inventory.setOnClickListener(v -> {
            Toast.makeText(TradeActivity.this, "Inventory Click", Toast.LENGTH_SHORT).show();
            Intent invIntent = new Intent(TradeActivity.this, InventoryActivity.class);
            invIntent.putExtra("user", user);
            startActivity(invIntent);
            finish();
        });

        trade_profile.setOnClickListener(v -> {
            Toast.makeText(TradeActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(TradeActivity.this, ProfileActivity.class);
            profileIntent.putExtra("user", user);
            startActivity(profileIntent);
            finish();
        });
        setRVOnClickListener();

        //just example for updating value in database
        //trade_ref.child("trades").child("15052023142518null").child("status").setValue("active");//trade table -> tradeID -> status for that tradeId
    }

    ///Trade to database
    ///
    ///
    ///
    ////
    ///
    ///
    //
    private void createPendingTrade() {
        String received_item_id = trade.getReceiverItem();
        String received_item_title = trade.getReceiverItemTitle();
        String initiator_item_id = trade.getInitiatorItem();
        String initiator_item_title = trade.getInitiatorItemTitle();
        String trade_ID = trade.getTradeID();
        String trade_initiator = trade.getInitiator();
        String trade_receiver = trade.getReceiver();
        String trade_place = trade.getPlace();
        String trade_status;
        if (trade.isCountered()){
            trade_status = trade.getStatus();
        }else{
            trade_status = "Pending: Sent to " + trade_receiver; //pending is the default, change to one of the following according to trade state: active, offer, cancelled, complete.
        }
        String receiver_completion_status = "ongoing"; //can be ongoing or complete (ongoing by default)
        String initiator_completion_status = "ongoing";//if both receiver and initiator are complete, trade_status can be set to complete


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
                        Toast.makeText(TradeActivity.this, "Trade offer was sent", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(TradeActivity.this, "Error: Trade wasn't saved in the database", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }


    //geting info for received item

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
                    item.setAvailable(Objects.requireNonNull(snap.child("available").getValue()).equals(true));
                    item.setComplete(Objects.requireNonNull(snap.child("complete").getValue()).equals(true));
                    // only show in inventory if item belongs to user
                    if (Objects.equals(item.getUser(), user) && !item.isComplete()){
                        itemArrayList.add(item);
                    }
/*
                    //get info of the received item
                    if (item.getID().contains(trade.getReceiveItem())){
                        Toast.makeText(TradeActivity.this, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
                        received_item = item;
                        received_item_name.setText(received_item.getName());
                    }

 */
                }

                ra = new invRecyclerAdapter(getApplicationContext(), itemArrayList, rvListener);
                tt_rv.setAdapter(ra);
                //setRVOnClickListener();
                ra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setRVOnClickListener() {
        rvListener = (v, pos) -> {
            // this click signifies an item was chosen to give
            giveItemID = itemArrayList.get(pos).getItemID();
            giveItemTitle = itemArrayList.get(pos).getTitle();
            Log.d("ITEM_ID", "Trade:setRVOnClickListener: getGiveItemID: " + giveItemID);
            trade.setInitiatorItem(giveItemID);
            trade.setInitiatorItemTitle(giveItemTitle);
            trade.setTradeID(); //give item + receive item
            trade.setPlace(place);
            isChosen = true;
            send.setEnabled(true);
            Toast.makeText(TradeActivity.this, "Item Selected!", Toast.LENGTH_LONG).show();
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
        tt_rv = findViewById(R.id.trade_to_rv);

        trade_home = findViewById(R.id.trade_home_btn);
        trade_tradeblock = findViewById(R.id.trade_tradeblock_btn);
        trade_inventory = findViewById(R.id.trade_inventory_btn);
        trade_profile = findViewById(R.id.prof_logout_btn);
        otherUser = findViewById(R.id.textViewTradeFor);
        otherUser.append(" from " + trade.getReceiver() + "@csusm.edu");

        received_item_name = findViewById(R.id.received_item_name);
        received_item_description = findViewById(R.id.received_item_description);
        received_item_tags = findViewById(R.id.received_item_tags);
        received_item_image = findViewById(R.id.trade_for_rv);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // get place from spinner
        place = places.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        place = places.get(0); // default to first option
    }
}
