package com.example.xchangebarter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xchangebarter.databinding.ActivityAddItemBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity {
    private ImageView itemPhoto;
    private EditText itemName, itemDescription, itemTags;
    private Button saveItem, back;

    private String title, description, tags, currentDate, currentTime, randID, imgUrl;

    private StorageReference itemPhotoRef;
    private DatabaseReference itemInfoRef;

    private Uri itemPic;
    ActivityResultLauncher<String> takePhoto;

    //private static final int GALLERYPICK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddItemActivity.this, "Back Click", Toast.LENGTH_SHORT).show();
                Intent homeIntent = new Intent(AddItemActivity.this, Home2Activity.class);
                startActivity(homeIntent);
                finish();
            }

        });

        itemPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto.launch("image/*");
            }
        });

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkItem();
            }
        });

    }

    private void checkItem(){
        description = itemDescription.getText().toString();
        title = itemName.getText().toString();
        tags = itemTags.getText().toString();

        if(itemPic == null){
            Toast.makeText(AddItemActivity.this, "Picture is required", Toast.LENGTH_SHORT).show();
        }
        else if (description.isEmpty()) {
            Toast.makeText(AddItemActivity.this, "Description is required", Toast.LENGTH_SHORT).show();
        }
        else if (title.isEmpty()) {
            Toast.makeText(AddItemActivity.this, "Item name is required", Toast.LENGTH_SHORT).show();
        }
        else{
            saveItemInfo();
        }
    }

    private void saveItemInfo(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currDate = new SimpleDateFormat("dd.MM.yyyy");
        currentDate = currDate.format(calendar.getTime());

        SimpleDateFormat currTime = new SimpleDateFormat("HH:mm:ss");
        currentTime = currTime.format(calendar.getTime());

        randID = currentDate + currentTime;

        StorageReference path = itemPhotoRef.child(itemPic.getLastPathSegment() + randID + ".jpg"); //.webm
        final UploadTask uploadTask = path.putFile(itemPic);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String serverMsg = e.toString();
                Toast.makeText(AddItemActivity.this, "Error: " + serverMsg, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddItemActivity.this, "Picture was saved successfuly", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        imgUrl = path.getDownloadUrl().toString();
                        return path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddItemActivity.this, "Url was saved successfuly", Toast.LENGTH_SHORT).show();

                            ItemInfoToDB();
                        }
                    }
                });
            }

            private void ItemInfoToDB(){
                HashMap<String, Object> itemMap = new HashMap<>();

                itemMap.put("itemID", randID);
                itemMap.put("date", currentDate);
                itemMap.put("time", currentTime);
                itemMap.put("image", imgUrl);
                itemMap.put("description", description);
                itemMap.put("title", title);
                itemMap.put("tags", tags);

                itemInfoRef.child(randID).updateChildren(itemMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddItemActivity.this, "Item info was stored in the databse", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String serverMsg = task.getException().toString();
                            Toast.makeText(AddItemActivity.this, "Error item wasn't stored: " + serverMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void init(){
        takePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        itemPhoto.setImageURI(result);
                    }
        });
        itemPhoto = findViewById(R.id.add_item_image);
        itemName = findViewById(R.id.item_title);
        itemDescription = findViewById(R.id.item_description);
        itemTags = findViewById(R.id.item_tags);
        saveItem = findViewById(R.id.add_to_inv_btn);
        back = findViewById(R.id.back_to_inv_btn);
        itemPhotoRef = FirebaseStorage.getInstance().getReference().child("itemImages");
        itemInfoRef = FirebaseDatabase.getInstance().getReference().child("All Item Info");
    }
}