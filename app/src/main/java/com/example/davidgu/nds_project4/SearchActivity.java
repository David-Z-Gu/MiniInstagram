package com.example.davidgu.nds_project4;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
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

        import java.io.BufferedInputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;

        import java.net.HttpURLConnection;
        import java.net.URI;
        import java.net.URL;
        import java.net.URLEncoder;

        import java.net.URL;
        import java.net.URLConnection;

        import java.util.UUID;


public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity >>>>>>";
    //
//    private FirebaseStorage storage = FirebaseStorage.getInstance();
//    private StorageReference storageRef = storage.getReferenceFromUrl("gs://mockinstagram-7a1fe.appspot.com").child("firememes/0b13a432-312e-4b3e-8207-dbcff85ec2ee.png");

    private DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference();

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

                Bitmap url = getImageBitmap("https://firebasestorage.googleapis.com/v0/b/mockinstagram-7a1fe.appspot.com/o/firememes%2Fd3363923-16c0-4a36-8e83-e7a3f0b4b321.png?alt=media&token=52aa299b-d13e-41f9-9585-42608e4c35f9");
                Log.d(TAG, "got bitmap");
//                InputStream image_strea   m = null;
//                try {
//                    image_stream = getContentResolver().openInputStream(url);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                Bitmap bitmap= BitmapFactory.decodeStream(image_stream );

//                mImageView.setImageBitmap(bitmap);
                //
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(R.layout.activity_search.getContentResolver(), url);
                mImageView.setImageBitmap(url);

//                String description = editText.getText().toString();
//                userDatabase.child("mockinstagram-7a1fe").child("Discription").equalTo(description).addValueEventListener(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        Log.d(TAG, "in onDataChange");
//                        User user = snapshot.getValue(User.class);
//                        Log.d(TAG, user.img_Url);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

            }
        });

    }

    private Bitmap getImageBitmap(String url) {
        Log.d(TAG, "In1");
        Bitmap bm = null;
        try {
            Log.d(TAG, "In2");
            URL aURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) aURL.openConnection();
            Log.d(TAG, "0");
            conn.connect(); 
            Log.d(TAG, "1");
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Log.d(TAG, "2");
            bm = BitmapFactory.decodeStream(bis);
            Log.d(TAG, "3");
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.d(TAG, "In3");
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }
}