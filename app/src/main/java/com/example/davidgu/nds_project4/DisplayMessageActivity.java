package com.example.davidgu.nds_project4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class DisplayMessageActivity extends AppCompatActivity {

    private View imageContainer;
    private TextView overlayText;
    private ProgressBar progressBar;
    private Button uploadButton;
    private TextView downloadUrl;

    //private StorageReference mStorageRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activty_main);
        imageContainer = findVieByID(R.id.image_container);
        overlayText = (TextView) findViewByID(R.id.overlay_text);
        overlayText.setText("");
        overlayText.setVisibility(View.INVISIBLE);
        EditText textInput = (EditText) findViewById(R.id.text_input);
        textInput.addTextChangedListener (new InputTextWatcher());
        uploadButton = (Button)findViewById(R.id.upload_button);
        uploadButton.setOnClickListener (new UploadOnClickListener());
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        downloadUrl = (TextView) findViewById(R.id.download_url);



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

    private class InputTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
                overlayText.setVisibility((s.length() >0 ? View.VISIBLE: View.INVISIBLE));
                overlayText.setText(s.toString());
        }

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

            String path = "firememes/" + UUID.randomUUID() + ".png";
            StorageReference firememeRef = storage.getReference(path);

            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("text", overlayText.getText().toString())
                    .build();

            progressBar.setVisibility(View.VISIBLE);
            uploadButton.setEnabled(false);

            UploadTask uploadTask = firememeRef.putBytes (data, metadata);
            uploadTask.addOnSuccessListener(MainActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    uploadButton.setEnabled(true);

                    Url url = taskSnapshot.getDownloadUrl();
                    downloadUrl.setText(url.toString());
                    downloadUrl.setVisibility(View.VISIBLE);
                }
            })
        }
    }
}
//Reference:
//https://www.youtube.com/watch?time_continue=148&v=7puuTDSf3pk
