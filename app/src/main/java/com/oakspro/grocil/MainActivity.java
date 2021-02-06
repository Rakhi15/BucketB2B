package com.oakspro.grocil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button signinBtn, signupBtn;
    String randomOTP=null;
    int otp_flag=0;
    String api_send_otp="";
    String api_login="";
    String api_signup="https://grocil.in/grocil_android/api/signup_api.php";
    EditText nameEd, storeEd, gstEd, emailEd, passwordEd, addressEd;
    TextView gst_result_test;
    ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        // get login details if already logined perviously

        SharedPreferences login = getSharedPreferences("auth", 0);
        String U_name = login.getString("u_name", "");
        String U_pass = login.getString("u_pass", "");

        //checking whether login details exist or not
        //if login details exits it will make auto login
        if (!U_name.isEmpty() && !U_pass.isEmpty()){
           Intent intent=new Intent(MainActivity.this, HomeActivity.class);
           startActivity(intent);
           finish();
        }

        //set ids
        signupBtn=findViewById(R.id.signup_btn);
        signinBtn=findViewById(R.id.signin_btn);

      

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.login_bottom_sheet);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                bottomSheetDialog.show();
                
                
               EditText mobile_no, password;
               Button Login;
               
               mobile_no=bottomSheetDialog.findViewById(R.id.mobile_ed);
               password=bottomSheetDialog.findViewById(R.id.password_ed);
               Login=bottomSheetDialog.findViewById(R.id.login_btn);
               
               Login.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       String mob_ed = mobile_no.getText().toString();
                       String pass_ed= password.getText().toString();
                       
                       if (!TextUtils.isEmpty(mob_ed) && !TextUtils.isEmpty(pass_ed)){
                           if (mob_ed.length()==10) {
                               loginaction(mob_ed, pass_ed);
                           }else {
                               mobile_no.setError("Enter Valid mobile no");
                           }
                       }else {
                           if (TextUtils.isEmpty(mob_ed)) {
                               mobile_no.setError("Enter Mobile no");
                           }else {
                               password.setError("Enter Password");
                           }
                       }                     
                      
                   }
               });
               
               
                
                
                
                
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.signup_bottom_sheet);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                bottomSheetDialog.show();

                EditText mobileEd, otpEd;
                Button nextBtn;

                mobileEd=bottomSheetDialog.findViewById(R.id.mobile_ed);
                otpEd=bottomSheetDialog.findViewById(R.id.otp_ed);
                nextBtn=bottomSheetDialog.findViewById(R.id.nextbtn);

                LinearLayout linearLayout;

                linearLayout=bottomSheetDialog.findViewById(R.id.linear11);
                nameEd=bottomSheetDialog.findViewById(R.id.name_ed);
                storeEd=bottomSheetDialog.findViewById(R.id.store_name_ed);
                gstEd=bottomSheetDialog.findViewById(R.id.gst_ed);
                emailEd=bottomSheetDialog.findViewById(R.id.email_ed);
                passwordEd=bottomSheetDialog.findViewById(R.id.password_ed);
                addressEd=bottomSheetDialog.findViewById(R.id.address_ed);
                gst_result_test=bottomSheetDialog.findViewById(R.id.text_result_gst);


                gst_result_test.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                            if (nextBtn.getText().equals("Next")){
                                String mobile_s=mobileEd.getText().toString();
                                if (mobile_s.length()==10){

                                    sendOTP(mobile_s);
                                    //    if (otp_flag==1){
                                    otpEd.setVisibility(View.VISIBLE);
                                    nextBtn.setText("Verify OTP");
                                    //  }else {
                                    //      Toast.makeText(MainActivity.this, "Mobile Number is Invalid", Toast.LENGTH_SHORT).show();
                                    //  }
                                }else {
                                    Toast.makeText(MainActivity.this, "Please Enter Valid 10 Digit Mobile", Toast.LENGTH_SHORT).show();
                                }
                            }else if(nextBtn.getText().equals("Verify OTP")){
                                String otp_res=otpEd.getText().toString();
                                if (!TextUtils.isEmpty(otp_res) && otp_res.length()==6 && otp_res.equals(randomOTP)){
                                    mobileEd.setEnabled(false);
                                    otpEd.setEnabled(false);
                                    linearLayout.setVisibility(View.VISIBLE);
                                    nextBtn.setText("Register");
                                }else {
                                    Toast.makeText(MainActivity.this, "Please Enter Valid  OTP", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(nextBtn.getText().equals("Register")){
                                String gst_res=gstEd.getText().toString();
                                if (Character.isDigit(gst_res.charAt(0)) && Character.isDigit(gst_res.charAt(1)) && Character.isAlphabetic(gst_res.charAt(2)) && Character.isAlphabetic(gst_res.charAt(3))
                                        && Character.isAlphabetic(gst_res.charAt(4)) && Character.isAlphabetic(gst_res.charAt(5)) && Character.isAlphabetic(gst_res.charAt(6)) && Character.isDigit(gst_res.charAt(7))
                                        && Character.isDigit(gst_res.charAt(8)) && Character.isDigit(gst_res.charAt(9)) && Character.isDigit(gst_res.charAt(10)) && Character.isAlphabetic(gst_res.charAt(11))
                                        && Character.isDigit(gst_res.charAt(12)) && Character.isAlphabetic(gst_res.charAt(13)) && Character.isLetterOrDigit(gst_res.charAt(14))){

                                    gstEd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checked_data, 0);
                                    gst_result_test.setText("GST VALID");
                                    gst_result_test.setTextColor(Color.GREEN);

                                    String name_s=nameEd.getText().toString();
                                    String email_s=emailEd.getText().toString();
                                    String store_name_s=storeEd.getText().toString();
                                    String gstnum_s=gstEd.getText().toString();
                                    String mobile_s=mobileEd.getText().toString();
                                    String address_s=addressEd.getText().toString();
                                    String password_s=passwordEd.getText().toString();

                                    uploadDataRegister(name_s, email_s, store_name_s,gstnum_s, mobile_s,address_s,password_s);

                                }else{
                                    gstEd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cross_mark, 0);
                                    gst_result_test.setText("INVALID GST");
                                    gst_result_test.setTextColor(Color.RED);
                                }
                            }


                    }
                });

            }
        });
    }

    private void uploadDataRegister(String name_s, String email_s, String store_name_s, String gstnum_s, String mobile_s, String address_s, String password_s) {

    progressDialog.show();

    StringRequest request=new StringRequest(Request.Method.POST, api_signup, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status= jsonObject.getString("jstatus");
                if(status.equals("1")){
                    Toast.makeText(MainActivity.this, "Registration successful waiting for activation", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(MainActivity.this, "Check you Internet Connection", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> upload = new HashMap<>();
            upload.put("name", name_s);
            upload.put("mobile", mobile_s);
            upload.put("email", email_s);
            upload.put("store_name", store_name_s);
            upload.put("gstnum", gstnum_s);
            upload.put("address", address_s);
            upload.put("password", password_s);
            return upload;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(request);


    }

    private void loginaction(String mob_ed, String pass_ed) {
        
        
        StringRequest login_req = new StringRequest(Request.Method.POST, api_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                

                
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                    
                    if (status.equals("1")){
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            //to get data from server we can write code
                        }

                        //this to save the username and password and use for auto login
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        SharedPreferences login_details = getSharedPreferences("auth", 0);
                        SharedPreferences.Editor editor = login_details.edit();
                        editor.putString("u_name", mob_ed.toString());
                        editor.putString("u_pass", pass_ed.toString());
                        editor.commit();
                        startActivity(intent);
                        finish();

                    }else {
                        Toast.makeText(MainActivity.this, "Enter Valid username or password", Toast.LENGTH_SHORT).show();
                    }
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Check Internet connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(login_req);
        
    }

    private void sendOTP(String mobile_s) {

        Random random=new Random();
        int randNum=random.nextInt(999999);
        randomOTP=String.format("%6d", randNum);

        Log.i("OTP GEN", randomOTP);

        StringRequest request=new StringRequest(Request.Method.POST, api_send_otp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");

                    if (status.equals("1")){
                        otp_flag=1;
                    }else {
                        otp_flag=0;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // delete below line after otp_send_otp ready
                    otp_flag=1;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> otpmap=new HashMap<>();
                otpmap.put("otp", randomOTP);
                otpmap.put("mobile", mobile_s);
                return otpmap;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}