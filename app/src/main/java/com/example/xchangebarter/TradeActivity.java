package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.xchangebarter.Trade.Trade;

public class TradeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference inv_item_ref;
    private StorageReference inv_img_ref;

    private ArrayList<Item> itemArrayList;



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

    private String user, giveItemID, place, received_item_path;
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
            trade = extras.getParcelable("trade");
        }
        isChosen = false;
        placeSpin = (Spinner) findViewById(R.id.place_spinner);
        places = new ArrayList<>();
        places.add("Outside Library Floor 1");
        places.add("Starbucks");
        places.add("Outside USU");
        places.add("Outside Health Building");
        places.add("Extended Learning Building");
        places.add("Sports Center");
        places.add("University Commons");
        places.add("Science Building 1");
        places.add("Science Building 2");
        places.add("Viasat Engineering Pavilion");

        ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, places);

        placeSpin.setAdapter(spin_adapter);

        placeSpin.setOnItemSelectedListener(this);

        // TODO: Change receive_item imageview to a single_item_layout for the item from database
        //  associated with ID ReceiveItem in trade

        back = (Button) findViewById(R.id.notif_back);
        send = (Button) findViewById(R.id.sendTradeBtn);
        accept = (Button) findViewById(R.id.acceptTradeBtn);
        reject = (Button) findViewById(R.id.rejectTradeBtn);
        counter = (Button) findViewById(R.id.counterTradeBtn);


        // TODO: based on trade status (fresh, incoming) make correct buttons enabled
        if (trade.isFresh()){   // new trade created by this user
            send.setEnabled(true);
            accept.setEnabled(false);
            reject.setEnabled(false);
            counter.setEnabled(false);
        } else {    // trade is incoming
            send.setEnabled(false);
            accept.setEnabled(true);
            reject.setEnabled(true);
            counter.setEnabled(true);
        }



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

        //received_item_path = "otherItemInfo/"+trade.getReceiveItem();
        received_item_ref = inv_item_ref.child("otherItemInfo").child(trade.getReceiveItem());
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


                        Toast.makeText(TradeActivity.this, "Item user: " + received_item.getItemID(), Toast.LENGTH_LONG).show();
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



        // TODO: set on click listeners for the send, reject, accept, and counter buttons
        // complete trade details and go to trade block to see that trade has been sent
        send.setOnClickListener(v -> {
            if (isChosen) {
                // invert current user and other user viewpoint since this will be received from other user
                trade.setIncoming(true);
                trade.setCurrentUser(trade.getOtherUser());
                trade.setOtherUser(user);
                trade.setGiveItem(trade.getReceiveItem());
                trade.setReceiveItem(giveItemID);
                trade.setPlace(place);
                Toast.makeText(TradeActivity.this, "Send Click", Toast.LENGTH_SHORT).show();
                Intent tbIntent = new Intent(TradeActivity.this, TradeBlockActivity.class);
                tbIntent.putExtra("user", user);
                tbIntent.putExtra("trade", trade);
                startActivity(tbIntent);
            } else {
                Toast.makeText(TradeActivity.this, "Must Choose Item to Give!", Toast.LENGTH_LONG).show();
            }
        });

        // trade is complete add to user's completed trades
        accept.setOnClickListener(v -> {
            trade.setAccepted(true);
            trade.setPlace(place);
            Toast.makeText(TradeActivity.this, "Send Click", Toast.LENGTH_SHORT).show();
            Intent tbIntent = new Intent(TradeActivity.this, TradeBlockActivity.class);
            tbIntent.putExtra("user", user);
            tbIntent.putExtra("trade", trade);
            startActivity(tbIntent);
        });

        // trade is rejected remove from trade block
        reject.setOnClickListener(v -> {
            trade.setRejected(true);
            Toast.makeText(TradeActivity.this, "Send Click", Toast.LENGTH_SHORT).show();
            Intent tbIntent = new Intent(TradeActivity.this, TradeBlockActivity.class);
            tbIntent.putExtra("user", user);
            tbIntent.putExtra("trade", trade);
            startActivity(tbIntent);
        });

        // send counter trade
        counter.setOnClickListener(v -> {
            if (isChosen) {
                // invert current user and other user viewpoint since this will be received from other user
                trade.setIncoming(true);
                trade.setCurrentUser(trade.getOtherUser());
                trade.setOtherUser(user);
                trade.setGiveItem(trade.getReceiveItem());
                trade.setReceiveItem(giveItemID);
                trade.setPlace(place);
                trade.setCountered(true);
                Toast.makeText(TradeActivity.this, "Send Click", Toast.LENGTH_SHORT).show();
                Intent tbIntent = new Intent(TradeActivity.this, TradeBlockActivity.class);
                tbIntent.putExtra("user", user);
                tbIntent.putExtra("trade", trade);
                startActivity(tbIntent);
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
            startActivity(invIntent);
            invIntent.putExtra("user", user);
            finish();
        });

        trade_profile.setOnClickListener(v -> {
            Toast.makeText(TradeActivity.this, "Profile Click", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(TradeActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
            profileIntent.putExtra("user", user);
            finish();
        });
        setRVOnClickListener();
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
                    item.setImage(Objects.requireNonNull(snap.child("image").getValue()).toString());
                    item.setTitle(Objects.requireNonNull(snap.child("title").getValue()).toString());
                    item.setDescription(Objects.requireNonNull(snap.child("description").getValue()).toString());
                    item.setTags(Objects.requireNonNull(snap.child("tags").getValue()).toString());
                    item.setUser(Objects.requireNonNull(snap.child("user").getValue()).toString());
                    // only show in inventory if item belongs to user
                    if (Objects.equals(item.getUser(), user)){
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
        rvListener = new invRecyclerAdapter.RecyclerViewOnClickListener() {
            @Override
            public void onClick(View v, int pos) {
                // this click signifies an item was chosen to give
                giveItemID = itemArrayList.get(pos).getItemID();
                trade.setGiveItem(giveItemID);
                isChosen = true;
                // set trade ID of item to associate it with this trade
                //TODO: get item from database with the itemID of giveItemID and set the tradeID to
                // the tradeID of trade
                Toast.makeText(TradeActivity.this, "Item Selected!", Toast.LENGTH_LONG).show();
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
        tt_rv = findViewById(R.id.trade_to_rv);

        trade_home = findViewById(R.id.trade_home_btn);
        trade_tradeblock = findViewById(R.id.trade_tradeblock_btn);
        trade_inventory = findViewById(R.id.trade_inventory_btn);
        trade_profile = findViewById(R.id.trade_profile_btn);
        otherUser = findViewById(R.id.textViewTradeFor);
        otherUser.append(trade.getOtherUser() + "@csusm.edu");

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
    public void onNothingSelected(AdapterView<?> adapterView) {}
}
