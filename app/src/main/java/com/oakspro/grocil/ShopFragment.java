package com.oakspro.grocil;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ShopFragment extends Fragment {

    RecyclerView recyclerView;
    NestedScrollView newNest;
    ShimmerFrameLayout shimmerFrameLayoutCat;
    String api_cat_list="";
    ArrayList<MainDataCat> dataArrayList=new ArrayList<>();
    CategoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView=root.findViewById(R.id.recyclerViewCat);
        newNest=root.findViewById(R.id.new_nest);
        shimmerFrameLayoutCat=root.findViewById(R.id.shimmerLayout_cat);
        shimmerFrameLayoutCat.startShimmer();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        getCategoryList();

        newNest.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY==v.getChildAt(0).getMeasuredHeight() -v.getMeasuredHeight()){
                    getCategoryList();
                }
            }
        });

        return root;
    }

    private void getCategoryList() {
        StringRequest request=new StringRequest(Request.Method.POST, api_cat_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response!=null){


                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("CatStatus");
                        JSONArray jsonArray=jsonObject.getJSONArray("catDetails");

                        if (status.equals("1")){

                            for (int i=0; i<=jsonArray.length(); i++){
                                JSONObject object=jsonArray.getJSONObject(i);

                                MainDataCat data=new MainDataCat();
                                data.setCategoryId(object.getString("cat_id"));
                                data.setCategoryName(object.getString("cat_name"));
                                data.setCategoryImg(object.getString("cat_img"));
                                data.setCategoryStatus(object.getString("cat_status"));

                                dataArrayList.add(data);
                            }
                            adapter=new CategoryAdapter(getContext(), dataArrayList);
                            recyclerView.setVisibility(View.VISIBLE);
                            shimmerFrameLayoutCat.stopShimmer();
                            shimmerFrameLayoutCat.setVisibility(View.GONE);
                            recyclerView.setAdapter(adapter);
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
                catData.put("data", "catList");
                return catData;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }
}