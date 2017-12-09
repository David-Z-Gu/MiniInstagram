package com.example.davidgu.nds_project4;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
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
        import android.widget.TextView;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FileDownloadTask;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageMetadata;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;


        import android.net.Uri;

        import java.io.File;
        import java.io.IOException;
        import java.util.UUID;


public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity >>>>>>";
    //
//    private FirebaseStorage storage = FirebaseStorage.getInstance();
//    private StorageReference storageRef = storage.getReferenceFromUrl("gs://mockinstagram-7a1fe.appspot.com").child("firememes/0b13a432-312e-4b3e-8207-dbcff85ec2ee.png");
    private DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("Fani");
    private String email;

    EditText editText;
    Button search;
    TextView demoValue;
    ImageView mImageView;
    CheckBox publicCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        email = getIntent().getStringExtra("user_email");

        editText = (EditText) findViewById(R.id.etValue);
        demoValue = (TextView) findViewById(R.id.tvValue);
        search = (Button) findViewById(R.id.btnSearch);
        mImageView = (ImageView) findViewById(R.id.image);


        publicCheckbox = findViewById(R.id.checkPublic);


        //database reference pointing to root of database
//        try {
//            final File localFile = File.createTempFile("images", "jpg");
//            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                    mImageView.setImageBitmap(bitmap);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                }
//            });
//        }
//        catch (IOException e ) {}

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "in onClick");
                String description = editText.getText().toString();
                userDatabase.child("img_Url").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.d(TAG, "in onDataChange");
//                        Log.d(TAG, "in onDataChange");
//                        User user = snapshot.getValue(User.class);
//                        Log.d(TAG, user.getUrl());
                        User post = snapshot.getValue(User.class);
                        Log.d(TAG, post.getUrl());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });
            }
        });

    }
}