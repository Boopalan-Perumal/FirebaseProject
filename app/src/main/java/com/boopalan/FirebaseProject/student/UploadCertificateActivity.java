package com.boopalan.FirebaseProject.student;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.boopalan.FirebaseProject.R;
import com.boopalan.FirebaseProject.activity.BaseActivity;
import com.boopalan.FirebaseProject.activity.MainActivity;
import com.boopalan.FirebaseProject.model.Url;
import com.boopalan.FirebaseProject.model.User;
import com.boopalan.FirebaseProject.model.photo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Boopalan on 24-Feb-2017.
 */

public class UploadCertificateActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UploadCertificateActivity";
    private static final String REQUIRED = "Required";
    private final int requestCode = 20;
    public String imageEncoded;
    EditText uploadCertificate;
    Button submit;
    private Button b1;
    private ImageView imageHolder;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_certificate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uploadCertificate = (EditText) findViewById(R.id.upload);
        imageHolder = (ImageView) findViewById(R.id.captured_photo);
        imageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);

        findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost1();
                Toast.makeText(getApplicationContext(), "Events Added Successfully", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(UploadCertificateActivity.this, StudentDetailActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageHolder.setImageBitmap(bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        submitPost();
        Toast.makeText(getApplicationContext(), "Events Added Successfully", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(UploadCertificateActivity.this, StudentDetailActivity.class);
        startActivity(i);
    }

    private void submitPost() {

        final String event1 = uploadCertificate.getText().toString();


        if (TextUtils.isEmpty(event1)) {
            uploadCertificate.setError(REQUIRED);
            return;
        }

        final String userId = getUid();

        mDatabase.child("users/students").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
//                            Log.d(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(UploadCertificateActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.username, event1);
                        }

                        // Finish this Activity, back to the stream
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void submitPost1() {


        final String userId = getUid();

        mDatabase.child("users/students").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
//                            Log.d(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(UploadCertificateActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost1(userId, user.username, imageEncoded);
                        }

                        // Finish this Activity, back to the stream
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void writeNewPost(String userId, String username, String event1) {
        String key = mDatabase.child("posts").push().getKey();
        Url post = new Url(userId, username, event1);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/url/" + key, postValues);
//        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

    }

    private void writeNewPost1(String userId, String username, String imageEncoded) {
        String key = mDatabase.child("posts").push().getKey();
        photo post = new photo(userId, username, imageEncoded);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/photo/" + key, postValues);
//        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

    }

}
