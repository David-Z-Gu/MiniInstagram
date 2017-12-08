package com.example.davidgu.nds_project4;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisplayMessageActivity extends AppCompatActivity {

    private static final String TAG = "Display>>>>>>>";

    private int GET_FROM_GALLERY = 1;

    private ImageView imageContainer;
    private ProgressBar progressBar;
    private Button uploadButton;
    private Button searchButton;
    private EditText description;

    private CheckBox imagePublic;
    //private TextView downloadUrl;

    //private StorageReference mStorageRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mUserRef = mDatabase.child("user");

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = getIntent().getStringExtra("user_email");

        setContentView(R.layout.activity_display_message);

        imageContainer = findViewById(R.id.image_container);
        uploadButton = (Button)findViewById(R.id.upload_button);
        uploadButton.setOnClickListener (new UploadOnClickListener());
//        galleryButton = (Button)findViewById(R.id.image_gallery);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        description = findViewById(R.id.image_description);
        imagePublic = (CheckBox)findViewById(R.id.image_public);

        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener (new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });
    }



    private class UploadOnClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
//            imageContainer.setDrawingCacheEnabled (true);
//            imageContainer.buildDrawingCache();
//            Bitmap bitmap = imageContainer.getDrawingCache();
            String img_path = "firememes/" + UUID.randomUUID() + ".png";
            StorageReference firememeRef = storage.getReference(img_path);

            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageContainer.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] data = stream.toByteArray();

            UploadTask uploadTask = firememeRef.putBytes (data);
//            StorageMetadata metadata = new StorageMetadata.Builder()
//                    .build();

            progressBar.setVisibility(View.VISIBLE);
            uploadButton.setEnabled(false);

//            final Uri[] img_Url = new Uri[1];
//            addOnSuccessListener(DisplayMessageActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>()
//            final String[] img_Url = new String[1];

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d(TAG, "In onFailure");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "In onSuccess");
                    uploadButton.setEnabled(true);
//                    img_Url[0] = taskSnapshot.getDownloadUrl();
//                    Log.d(TAG, img_Url[0].toString());
//                    img_Url[0] = taskSnapshot.getDownloadUrl().toString();
                    String img_Url = taskSnapshot.getDownloadUrl().toString();

                    int availability;
                    //mDatabase.setValue();
                    if (imagePublic.isChecked()){
                        availability = 1;
                    }
                    else{
                        availability = 0;
                    }

                    String image_description = description.getText().toString();

                    writeNewUser(availability, email, img_Url, image_description);
                }
            });

        }
    }

    public void select_from_gallery(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageContainer.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private void writeNewUser(int availability, String email, String img_Url, String Discription) {

        Log.d(TAG, "In writeNewUser");
        Log.d(TAG, img_Url);
        Log.d(TAG, String.valueOf(availability));
        Log.d(TAG, Discription);

        Map<String, User> users = new HashMap<>();
        users.put("Fani", new User(availability, img_Url, Discription));
        mUserRef.setValue(users);

        progressBar.setVisibility(View.GONE);
    }


    public void sendMessage(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("user_email", email);
        startActivity(intent);
    }
}


//Reference:
//https://www.youtube.com/watch?time_continue=148&v=7puuTDSf3pk
