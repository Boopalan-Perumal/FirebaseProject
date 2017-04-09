package com.boopalan.FirebaseProject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.boopalan.FirebaseProject.R;
import com.boopalan.FirebaseProject.hod.AdminActivity;
import com.boopalan.FirebaseProject.staff.StaffLogin;
import com.boopalan.FirebaseProject.student.StudentLogin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register= (Button)findViewById(R.id.register);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.register:
                finish();
                Intent in = new Intent(MainActivity.this,StudentLogin.class);
                startActivity(in);

                break;


        }
    }
}
