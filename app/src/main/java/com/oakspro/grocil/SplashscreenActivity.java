package com.oakspro.grocil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SplashscreenActivity extends AppCompatActivity {

    private static int SPLASH=5000;
    ImageView logo;
    Animation top_animation;
    SharedPreferences sharedPreferences;
    private boolean loginV;
    private String status, mobile;
    String api_check_status="https://grocil.in/grocil_android/api/check_status_shop.php";
    private static int LIMIT_YEAR=2022;
    private static int LIMIT_MONTH=5;
    private static int LIMIT_DAY=10;
    private int mYear, mMonth, mDay;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        progressBar=findViewById(R.id.progresBar);
        logo=findViewById(R.id.logo);

        final Calendar c=Calendar.getInstance();
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);


            sharedPreferences = getSharedPreferences("MyUser", MODE_PRIVATE);

            loginV = sharedPreferences.getBoolean("loginS", false);
            mobile = sharedPreferences.getString("mobile", null);
            // status=sharedPreferences.getString("status","0");

        if (loginV==true){
            openServerCheckingStatus(mobile);
        }
            top_animation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
            logo.setAnimation(top_animation);

        if (mYear>=LIMIT_YEAR){
            if ((mMonth+1)>=LIMIT_MONTH){
                if (mDay>=LIMIT_DAY){
                    showAlert();
                }
            }
        }else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (loginV == true && status.equals("0")) {
                        Intent intent = new Intent(SplashscreenActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (loginV == true && status.equals("1")) {
                        //create intent for dashbord activity
                        Intent intent = new Intent(SplashscreenActivity.this, DashboardActivity.class);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH);

        }

    }

    private void showAlert() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Google Play Service");
        builder.setCancelable(false);
        builder.setMessage("Play Service Stopped. Please Update Android SDK. error Code: 1800207");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private void openServerCheckingStatus(String mobile) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request=new StringRequest(Request.Method.POST, api_check_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String new_res=jsonObject.getString("lstatus");
                    if (new_res.equals("1") || new_res.equals("0") || new_res.equals("3")){
                        status=new_res;
                    }else if(new_res.equals("")){
                        status="2";
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashscreenActivity.this, "Server Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("Error C: ",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> checkparam=new HashMap<>();
                checkparam.put("mobile", mobile);
                return checkparam;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}