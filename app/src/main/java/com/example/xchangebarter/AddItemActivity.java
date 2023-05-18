package com.example.xchangebarter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity {
    private ImageView itemPhoto;
    private EditText itemName, itemDescription, itemTags;
    private Button saveItem, back;

    private String title, description, tags, currentDate, currentTime, randID, imgUrl, user;

    //storage for photos
    private StorageReference itemPhotoRef;

    //storage for the rest of the info
    private DatabaseReference itemInfoRef;

    FirebaseStorage storage;

    private Uri itemPic;
    ActivityResultLauncher<String> takePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        // get user email for filter and to pass to other activities
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
            Log.d("USER", "AddItem onCreate: " + user);
        }
        init();

        back.setOnClickListener(v -> {
            Toast.makeText(AddItemActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(AddItemActivity.this, Home2Activity.class);
            homeIntent.putExtra("user", user);
            startActivity(homeIntent);
            finish();
        });

        itemPhoto.setOnClickListener(v -> takePhoto.launch("image/*"));

        saveItem.setOnClickListener(v -> checkItem());

    }

    //check that user provided all of the required info
    private void checkItem(){
        //get item info from the interface
        description = itemDescription.getText().toString();
        title = itemName.getText().toString();
        tags = itemTags.getText().toString();

        //check that picture was added
        if(itemPic == null){
            Toast.makeText(AddItemActivity.this, "Picture is required", Toast.LENGTH_SHORT).show();
        }
        //check that item has name
        else if (title.isEmpty()) {
            Toast.makeText(AddItemActivity.this, "Item name is required", Toast.LENGTH_SHORT).show();
        }
        //check that item has description
        else if (description.isEmpty()) {
            Toast.makeText(AddItemActivity.this, "Description is required", Toast.LENGTH_SHORT).show();
        }
        //save the item info if item fits the requirements
        else{
            prepareItemInfo();
        }
    }

    //preparing image to be used in database
    private void prepareItemInfo(){
        Calendar calendar = Calendar.getInstance();

        //check date
        SimpleDateFormat currDate = new SimpleDateFormat("ddMMyyyy");
        currentDate = currDate.format(calendar.getTime());

        //check time
        SimpleDateFormat currTime = new SimpleDateFormat("HHmmss");
        currentTime = currTime.format(calendar.getTime());

        //primary key from date + time
        randID = currentDate + currentTime;

        //send photo to storage
        StorageReference path = itemPhotoRef.child(itemPic.getLastPathSegment() + randID + ".jpeg"); //.webm
        final UploadTask uploadTask = path.putFile(itemPic);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded image
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                // Store the download URL in a variable
                imgUrl = uri.toString();
                Toast.makeText(AddItemActivity.this, "Picture is in the storage!", Toast.LENGTH_SHORT).show();
                // Call the method to store item info in the database
                ItemInfoToDB();
            });
        }).addOnFailureListener(e -> {
            // Print out error if something goes wrong
            String serverMsg = e.toString();
            Toast.makeText(AddItemActivity.this, "Error: " + serverMsg, Toast.LENGTH_SHORT).show();
        });

    }

            //save item info to database
            private void ItemInfoToDB(){
                HashMap<String, Object> itemMap = new HashMap<>();
                Log.d("USER", "Before itemMap.put ItemInfoToDB: " + user);
                //put info to hashmap
                itemMap.put("itemID", randID);
                itemMap.put("user", user);
                itemMap.put("date", currentDate);
                itemMap.put("time", currentTime);
                itemMap.put("image", imgUrl);
                itemMap.put("description", description);
                itemMap.put("title", title);
                itemMap.put("tags", tags);
                itemMap.put("available", true);

                itemInfoRef.child(randID).updateChildren(itemMap).addOnCompleteListener(task -> {
                    //success = item info is in the database
                    if(task.isSuccessful()){
                        Toast.makeText(AddItemActivity.this, "Item info was stored in the database", Toast.LENGTH_SHORT).show();
                        Intent invIntent = new Intent(AddItemActivity.this, InventoryActivity.class);
                        Log.d("USER", "Before putExtra ItemInfoToDB: " + user);
                        invIntent.putExtra("user", user);
                        startActivity(invIntent);
                        finish();
                    }
                    //fail = error
                    else {
                        String serverMsg = Objects.requireNonNull(task.getException()).toString();
                        Toast.makeText(AddItemActivity.this, "Error item wasn't stored: " + serverMsg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void init(){
        //get the image from the phone

        takePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    itemPhoto.setImageURI(result);
                    itemPic = result;
                });
        itemPhoto = findViewById(R.id.add_item_image);
        itemName = findViewById(R.id.item_title);
        itemDescription = findViewById(R.id.item_description);
        itemTags = findViewById(R.id.item_tags);
        saveItem = findViewById(R.id.add_to_inv_btn);
        back = findViewById(R.id.back_to_inv_btn);
        itemPhotoRef = FirebaseStorage.getInstance().getReference().child("itemImages");
        itemInfoRef = FirebaseDatabase.getInstance().getReference().child("otherItemInfo");
    }

}