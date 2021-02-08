package com.oakspro.grocil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private String Name, Email, Mobile, StoreName, Gstno, Address;
    TextView name_t, email_t, mobile_t, storename_t, gstno_t, address_t, h_name;
    private boolean Details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       sharedPreferences=getSharedPreferences("MyUser", MODE_PRIVATE);
       Details=sharedPreferences.getBoolean("loginS", false);

       Name=sharedPreferences.getString("name", "");
       Email=sharedPreferences.getString("email", "");
       Mobile=sharedPreferences.getString("mobile", "");
       Gstno=sharedPreferences.getString("gstin", "");
       Address=sharedPreferences.getString("address", "");
       StoreName=sharedPreferences.getString("store_name", "");

       name_t=findViewById(R.id.pro_name);
       email_t=findViewById(R.id.pro_email);
       mobile_t=findViewById(R.id.pro_mobile);
       storename_t=findViewById(R.id.pro_store_name);
       gstno_t=findViewById(R.id.pro_gst);
       address_t=findViewById(R.id.pro_address);
       h_name=findViewById(R.id.prof_name);

       name_t.setText(Name);
       h_name.setText(Name);
       email_t.setText(Email);
       mobile_t.setText(Mobile);
       gstno_t.setText(Gstno);
       address_t.setText(Address);
       storename_t.setText(StoreName);

    }
}