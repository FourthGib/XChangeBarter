package com.example.xchangebarter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    /*
    private ImageView add_item_photo;
    private EditText add_item_name, add_item_description, add_item_tags;
    private Button saveItem, back;
    private Uri item_photo_uri;

    private StorageReference itemPhotoRef = FirebaseStorage.getInstance().getReference();
    private DatabaseReference itemInfoRef = FirebaseDatabase.getInstance().getReference("images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        add_item_photo = findViewById(R.id.add_item_image);
        add_item_name = findViewById(R.id.item_title);
        add_item_description  = findViewById(R.id.item_description);
        add_item_tags  = findViewById(R.id.item_tags);
        saveItem  = findViewById(R.id.add_to_inv_btn);
        back  = findViewById(R.id.back_to_inv_btn);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            item_photo_uri = data.getData();
                            add_item_photo.setImageURI(item_photo_uri);
                        }
                        else {
                            Toast.makeText(AddItemActivity.this, "Image required", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        add_item_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choose_from_gallery = new Intent();
                choose_from_gallery.setAction(Intent.ACTION_GET_CONTENT);
                choose_from_gallery.setType("image/*");
                activityResultLauncher.launch(choose_from_gallery);
            }
        });

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item_photo_uri != null){
                    SendToStorage(item_photo_uri);
                }
                else{
                    Toast.makeText(AddItemActivity.this, "Please add image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SendToStorage(Uri img){
        String description = add_item_description.getText().toString();
        String title = add_item_name.getText().toString();
        String tags = add_item_tags.getText().toString();

        if(img == null){
            Toast.makeText(AddItemActivity.this, "Picture is required", Toast.LENGTH_SHORT).show();
        }
        else if (description.isEmpty()) {
            Toast.makeText(AddItemActivity.this, "Description is required", Toast.LENGTH_SHORT).show();
        }
        else if (title.isEmpty()) {
            Toast.makeText(AddItemActivity.this, "Item name is required", Toast.LENGTH_SHORT).show();
        }
        else{
            final StorageReference img_ref = itemPhotoRef.child(System.currentTimeMillis()+"."+ getFileExtension(img));
            itemPhotoRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    itemPhotoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DataClass dc = new DataClass(uri.toString(),description);
                            String key = itemInfoRef.push().getKey();
                            //itemInfoRef.child(key).setValue(DataClass);
                            Toast.makeText(AddItemActivity.this, "Item was added success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    private String getFileExtension(Uri imgUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imgUri));
    }

    */

















    private ImageView itemPhoto;
    private EditText itemName, itemDescription, itemTags;
    private Button saveItem, back;

    private String title, description, tags, currentDate, currentTime, randID, imgUrl;

    //storage for photos
    private StorageReference itemPhotoRef;

    //storage for the rest of the info
    private DatabaseReference itemInfoRef;

    FirebaseStorage storage;

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

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get the download URL of the uploaded image
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Store the download URL in a variable
                        imgUrl = uri.toString();
                        Toast.makeText(AddItemActivity.this, "Picture is in the storage!", Toast.LENGTH_SHORT).show();
                        // Call the method to store item info in the database
                        ItemInfoToDB();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Print out error if something goes wrong
                String serverMsg = e.toString();
                Toast.makeText(AddItemActivity.this, "Error: " + serverMsg, Toast.LENGTH_SHORT).show();
            }
        });


        /*
        //check that photo was successfuly sent
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //print out error if something goes wrong
                String serverMsg = e.toString();
                Toast.makeText(AddItemActivity.this, "Error: " + serverMsg, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddItemActivity.this, "Picture is in the storage!", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        //get url of the photo (url leads to this picture in the storage)
                        imgUrl = path.getDownloadUrl().toString();
                        return path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AddItemActivity.this, "Url was saved to database", Toast.LENGTH_SHORT).show();

                            ItemInfoToDB();
                        }
                    }
                });

         */
            }

            //save item info to database
            private void ItemInfoToDB(){
                HashMap<String, Object> itemMap = new HashMap<>();

                //put info to hashmap
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
                        //success = item info is in the database
                        if(task.isSuccessful()){
                            Toast.makeText(AddItemActivity.this, "Item info was stored in the databse", Toast.LENGTH_SHORT).show();
                            Intent invIntent = new Intent(AddItemActivity.this, InventoryActivity.class);
                            startActivity(invIntent);
                            finish();
                        }
                        //fail = error
                        else {
                            String serverMsg = task.getException().toString();
                            Toast.makeText(AddItemActivity.this, "Error item wasn't stored: " + serverMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void init(){
        //get the image from the phone

        takePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        itemPhoto.setImageURI(result);
                        itemPic = result;
                    }
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