package com.oakspro.grocil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    public static TextView totalTx;
    ArrayList<CartData> dataArrayList=new ArrayList<>();
    CartAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    String api_cart_list="https://grocil.in/grocil_android/api/cart_list_api.php";
    Button backHome;
    SharedPreferences sharedPreferences;
    ImageView cartImg;
    TextView emptyTxt;
    SwipeButton enableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //set ids
        sharedPreferences=getSharedPreferences("MyUser", Context.MODE_PRIVATE);

        totalTx=findViewById(R.id.total_value);

        backHome=findViewById(R.id.back_Home);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        recyclerView=findViewById(R.id.recyclerViewCart);
        enableButton = (SwipeButton) findViewById(R.id.swipe_btn);


        cartImg=findViewById(R.id.img_empty);
        emptyTxt=findViewById(R.id.empty_text);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getcartlist();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dataArrayList.clear();
                getcartlist();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                AppCompatActivity appCompatActivity=(AppCompatActivity)v.getContext();
                Fragment myfragment=new ShopFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myfragment).addToBackStack(null).commit();

                 */
                Intent intent=new Intent(CartActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getcartlist() {
        dataArrayList.clear();
        StringRequest request=new StringRequest(Request.Method.POST, api_cart_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.i("RESPONSE: ", response);
                if (!TextUtils.isEmpty(response)){

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        JSONArray jsonArray=jsonObject.getJSONArray("cart");

                        if (status.equals("1")){

                            cartImg.setVisibility(View.GONE);
                            emptyTxt.setVisibility(View.GONE);

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object=jsonArray.getJSONObject(i);

                                // Log.i("test: ", String.valueOf(i));
                                CartData data=new CartData();

                                data.setCart_id(object.getString("cart_id"));
                                data.setProd_id(object.getString("prod_id"));
                                data.setProd_img(object.getString("prod_img"));
                                data.setProd_price(object.getString("prod_price"));
                                data.setProd_title(object.getString("prod_title"));
                                data.setProd_units(object.getString("prod_units"));
                                data.setProd_qty(object.getString("prod_qty"));

                                dataArrayList.add(data);
                            }
                          //  Log.i("CAT: ", categoryid);
                            adapter=new CartAdapter(CartActivity.this, dataArrayList, totalTx, enableButton);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }else{
                            cartImg.setVisibility(View.VISIBLE);
                            emptyTxt.setVisibility(View.VISIBLE);
                            Toast.makeText(CartActivity.this, "No Items Found in Cart", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(CartActivity.this, "JEx: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> catData=new HashMap<>();
                catData.put("userid", sharedPreferences.getString("userid", ""));
                catData.put("data", "cart_list");
                return catData;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}