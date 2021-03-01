package com.oakspro.grocil;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {


    RecyclerView recyclerView;
    NestedScrollView nested;
    ShimmerFrameLayout shimmerFrameLayoutsubcat;
    SwipeRefreshLayout swipeRefreshLayout;
    SubCatAdapter adapter;
    ArrayList<MainDataSubCat> dataArrayList=new ArrayList<>();

    String api_sub_cat="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_profile, container, false);

        nested=root.findViewById(R.id.new_nest);
        recyclerView=root.findViewById(R.id.recyclerViewPro);
        shimmerFrameLayoutsubcat=root.findViewById(R.id.shimmerLayout_cat);
        swipeRefreshLayout=root.findViewById(R.id.swipe_refresh);

        shimmerFrameLayoutsubcat.startShimmer();
        getsubcatlist();

        nested.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY==v.getChildAt(0).getMeasuredHeight() -v.getMeasuredHeight()){
                    getsubcatlist();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dataArrayList.clear();
                getsubcatlist();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return root;
    }

    private void getsubcatlist() {
        StringRequest subcat=new StringRequest(Request.Method.POST, api_sub_cat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!TextUtils.isEmpty(response)) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        JSONArray jsonArray = jsonObject.getJSONArray("details");

                        if (status.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                MainDataSubCat data = new MainDataSubCat();

                                data.setProductID(object.getString("pro_id"));
                                data.setProductName(object.getString("pro_name"));
                                data.setProductPrice(object.getString("pro_price"));
                                data.setProductUnit(object.getString("pro_units"));
                                data.setProductImage(object.getString("pro_image"));

                                dataArrayList.add(data);
                            }
                            adapter = new SubCatAdapter(getContext(), dataArrayList);
                            shimmerFrameLayoutsubcat.stopShimmer();
                            shimmerFrameLayoutsubcat.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> catData=new HashMap<>();
                catData.put("data", "category");
                return catData;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(subcat);
    }
}