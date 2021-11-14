package com.oakspro.grocil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TransActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
    }

    public void back_home(View view) {
        Intent intent=new Intent(TransActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}