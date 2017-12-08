package com.example.davidgu.nds_project4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class DisplayMessageActivity extends AppCompatActivity {

    private View imageContainer;
    private ProgressBar progressBar;
    private Button uploadButton;
    private TextView description;

    private CheckBox imagePublic;
    //private TextView downloadUrl;

    //private StorageReference mStorageRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        email = getIntent().getStringExtra("user_email");


        setContentView(R.layout.activity_display_message);
        imageContainer = findViewById(R.id.image_container);
        uploadButton = (Button)findViewById(R.id.upload_button);
        uploadButton.setOnClickListener (new UploadOnClickListener());
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        description = findViewById(R.id.image_description);
        imagePublic = (CheckBox)findViewById(R.id.image_public);

        //downloadUrl = (TextView) findViewById(R.id.download_url);



//        setContentView(R.layout.activity_display_message);
//
//        // Get the Intent that started this activity and extract the string
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
//
//        // Capture the layout's TextView and set the string as its text
//        TextView textView = findViewById(R.id.textView);
//        textView.setText(message);
    }



    private class UploadOnClickListener implements View.OnClickListener {
        @Override
        public void onClick (View view) {
            imageContainer.setDrawingCacheEnabled (true);
            imageContainer.buildDrawingCache();
            Bitmap bitmap = imageContainer.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress (Bitmap.CompressFormat.PNG, 100, baos);
            imageContainer.setDrawingCacheEnabled (false);
            byte[] data = baos.toByteArray();

            String image_description = description.getText().toString();

            String img_path = "firememes/" + UUID.randomUUID() + ".png";
            StorageReference firememeRef = storage.getReference(img_path);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .build();



            progressBar.setVisibility(View.VISIBLE);
            uploadButton.setEnabled(false);

            final String[] img_Url = new String[1];

            UploadTask uploadTask = firememeRef.putBytes (data, metadata);
            uploadTask.addOnSuccessListener(DisplayMessageActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    uploadButton.setEnabled(true);

                    img_Url[0] = taskSnapshot.getDownloadUrl().toString();

//

//                    Url url = taskSnapshot.getDownloadUrl();
//                    downloadUrl.setText(url.toString());
//                    downloadUrl.setVisibility(View.VISIBLE);
                }
            });

            int availability;

            //mDatabase.setValue();
            if (imagePublic.isChecked()){
                availability = 1;
            }
            else{
                availability = 0;
            }

            writeNewUser(availability, email, img_Url[0], image_description);
        }
    }

    private void writeNewUser(int availability, String email, String img_Url, String Discription) {
        mDatabase.child("users").setValue(email);
        mDatabase.child("users").child(email).child("img_Url").setValue(img_Url);
        mDatabase.child("users").child(email).child("img_Url").child(img_Url).child("available").setValue(availability);
        mDatabase.child("users").child(email).child("img_Url").child(img_Url).child("discription").setValue(Discription);
    }
}


//Reference:
//https://www.youtube.com/watch?time_continue=148&v=7puuTDSf3pk
