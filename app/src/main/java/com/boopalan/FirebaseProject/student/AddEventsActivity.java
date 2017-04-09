package com.boopalan.FirebaseProject.student;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.boopalan.FirebaseProject.R;
import com.boopalan.FirebaseProject.activity.MainActivity;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.boopalan.FirebaseProject.activity.*;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.boopalan.FirebaseProject.model.*;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Boopalan on 24-Feb-2017.
 */
public class AddEventsActivity extends BaseActivity {

    private Button b1;
    private EditText eventName;
    private EditText date1;
    private EditText venue;
    private EditText time;
    SimpleDateFormat formatter;
    int year, month, day,mHour,mMinute;

    private static final String TAG = "AddEventsActivity";
    private static final String REQUIRED = "Required";
    public String imageEncoded;
    private ImageView imageHolder;
    private final int requestCode = 20;
    CoordinatorLayout coordinatorLayoutLogin;


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_events_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        coordinatorLayoutLogin = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutLogin);


        eventName = (EditText) findViewById(R.id.field_title);
        date1 = (EditText) findViewById(R.id.field_body);
        venue = (EditText) findViewById(R.id.venue);
        time = (EditText) findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventsActivity.this,new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.show(AddEventsActivity.this.getSupportFragmentManager(), "Calendar");
                cdp.setDateRange(minDate, null);
                cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String dateInString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            Date date = formatter.parse(dateInString);

                            date1.setText(formatter.format(date));
                        } catch (Exception ex) {
                            date1.setText(ex.getMessage());
                        }
                    }
                });
            }
        });


        findViewById(R.id.fab_submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();

            }
        });

    }

    private void submitPost() {

        final String event1 = eventName.getText().toString();
        final String date2 = date1.getText().toString();
        final String venue1 = venue.getText().toString();
        final String time1 = time.getText().toString();



        if (TextUtils.isEmpty(event1)) {
            eventName.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(date2)) {
            date1.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(venue1)) {
            venue.setError(REQUIRED);
            return;
        }
        if (TextUtils.isEmpty(time1)) {
            time.setError(REQUIRED);
            return;
        }
        final String userId = getUid();
        Log.d(TAG,userId);
        mDatabase.child("users/students").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(AddEventsActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.username, event1, date2,venue1,time1);
                        }

                        // Finish this Activity, back to the stream
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    private void writeNewPost(String userId, String username, String event1, String date1,String venue1,String time1) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, event1, date1,venue1,time1);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/students/" + key, postValues);
//        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        Toast.makeText(getApplicationContext(),"Events Added Successfully",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AddEventsActivity.this,StudentDetailActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, StudentLogin.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
