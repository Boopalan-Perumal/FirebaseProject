package com.boopalan.FirebaseProject.student;

/**
 * Created by Boopalan on 24-Feb-2017.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.boopalan.FirebaseProject.activity.MainActivity;
import com.boopalan.FirebaseProject.hod.*;
import com.boopalan.FirebaseProject.activity.BaseActivity;
import com.boopalan.FirebaseProject.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Boopalan on 23-Feb-2017.
 */

public class StudentDetailActivity extends BaseActivity implements View.OnClickListener{
    Button addEvents,SendLocation,UploadCertificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_detail_activity);
        addEvents= (Button)findViewById(R.id.login);
        SendLocation= (Button)findViewById(R.id.register);
        UploadCertificate= (Button)findViewById(R.id.admin);
        addEvents.setOnClickListener(this);
        SendLocation.setOnClickListener(this);
        UploadCertificate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case  R.id.login :
                Intent i = new Intent(StudentDetailActivity.this,AddEventsActivity.class);
                startActivity(i);
                break;
            case R.id.register:
                Intent in = new Intent(StudentDetailActivity.this,SendLocationActivity.class);
                startActivity(in);
                break;
            case R.id.admin:
                Intent intent = new Intent(StudentDetailActivity.this,Upload.class);
                startActivity(intent);
                break;

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
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_exit)
                .setTitle(getResources().getString(R.string.exit))
                .setMessage(getResources().getString(R.string.close))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }

                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }


}
