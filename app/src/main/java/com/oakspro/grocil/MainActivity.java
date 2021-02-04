package com.oakspro.grocil;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends AppCompatActivity {

    Button signinBtn, signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set ids
        signupBtn=findViewById(R.id.signup_btn);
        signinBtn=findViewById(R.id.signin_btn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.login_bottom_sheet);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.show();
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
                EditText nameEd, storeEd, gstEd, emailEd, passwordEd, addressEd;
                TextView gst_result_test;
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
                                otpEd.setVisibility(View.VISIBLE);
                                nextBtn.setText("Verify OTP");
                            }else {
                                Toast.makeText(MainActivity.this, "Please Enter Valid 10 Digit Mobile", Toast.LENGTH_SHORT).show();
                            }
                        }else if(nextBtn.getText().equals("Verify OTP")){
                            String otp_res=otpEd.getText().toString();
                            if (!TextUtils.isEmpty(otp_res) && otp_res.length()==6 && otp_res.equals("445566")){
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
}