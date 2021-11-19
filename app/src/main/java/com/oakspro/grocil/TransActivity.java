package com.oakspro.grocil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String api_cart_list="https://grocil.in/grocil_android/api/trans_list_api.php";
    SharedPreferences sharedPreferences;
    ArrayList<TransData> dataArrayList=new ArrayList<>();
    TransAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        sharedPreferences=getSharedPreferences("MyUser", Context.MODE_PRIVATE);
        recyclerView=findViewById(R.id.recyclerViewCart);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getTransList();
    }

    private void getTransList() {
        dataArrayList.clear();
        StringRequest request=new StringRequest(Request.Method.POST, api_cart_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.i("RESPONSE: ", response);
                if (!TextUtils.isEmpty(response)){

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        JSONArray jsonArray=jsonObject.getJSONArray("trans");

                        if (status.equals("1")){

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object=jsonArray.getJSONObject(i);

                                // Log.i("test: ", String.valueOf(i));
                                TransData data=new TransData();

                                data.setTransID(object.getString("trans_id"));
                                data.setDate(object.getString("date"));
                                data.setAmount(Double.parseDouble(object.getString("amount")));
                                data.setTransType(object.getString("type"));
                                data.setReason(object.getString("reason"));

                                dataArrayList.add(data);
                            }
                            //  Log.i("CAT: ", categoryid);
                            adapter=new TransAdapter(TransActivity.this, dataArrayList);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }else{

                            Toast.makeText(TransActivity.this, "No Transactions Found", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TransActivity.this, "JEx: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> catData=new HashMap<>();
                catData.put("userid", sharedPreferences.getString("userid", ""));
                catData.put("data", "trans_list");
                return catData;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void back_home(View view) {
        Intent intent=new Intent(TransActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}