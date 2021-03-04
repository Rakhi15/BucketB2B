package com.oakspro.grocil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class ProductsFragment extends Fragment {

    ArrayList<MainDataSubCat> dataArrayList=new ArrayList<>();
    SubCatAdapter adapter;
    ShimmerFrameLayout shimmerFrameLayoutSubCat;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    NestedScrollView newNest;
    String api_prod_list="https://grocil.in/grocil_android/api/products_list_api.php";
    String categoryid;


    Button backHome;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_products, container, false);
        backHome=root.findViewById(R.id.back_Home);
        newNest=root.findViewById(R.id.new_nest);
        swipeRefreshLayout=root.findViewById(R.id.swipe_refresh);
        recyclerView=root.findViewById(R.id.recyclerViewPro);
        shimmerFrameLayoutSubCat=root.findViewById(R.id.shimmerLayout_subcat);
        Bundle bundle=this.getArguments();
        categoryid=bundle.getString("category");


        getproductlist();

        newNest.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY==v.getChildAt(0).getMeasuredHeight() -v.getMeasuredHeight()){
                    getproductlist();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dataArrayList.clear();
                getproductlist();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity=(AppCompatActivity)v.getContext();
                Fragment myfragment=new ShopFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myfragment).addToBackStack(null).commit();
            }
        });
        return root;
    }

    private void getproductlist() {
        StringRequest request=new StringRequest(Request.Method.POST, api_prod_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.i("RESPONSE: ", response);
                if (!TextUtils.isEmpty(response)){

                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        JSONArray jsonArray=jsonObject.getJSONArray("details");

                        if (status.equals("1")){

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object=jsonArray.getJSONObject(i);

                                // Log.i("test: ", String.valueOf(i));
                                MainDataSubCat data=new MainDataSubCat();

                                data.setProductName(object.getString("p_name"));
                                data.setProductImage(object.getString("pic_address"));
                                data.setProductPrice(object.getString("price"));
                                data.setProductUnit(object.getString("units"));
                                data.setProductID(object.getString("pid"));
                                data.setProductCid(object.getString("cid"));
                                data.setProductStatus(object.getString("pstatus"));
                                data.setProductMisc(object.getString("pmisc"));
                                data.setProductNote(object.getString("pnote"));

                                dataArrayList.add(data);
                            }
                            Log.i("CAT: ", categoryid);
                            adapter=new SubCatAdapter(getContext(), dataArrayList);
                            shimmerFrameLayoutSubCat.stopShimmer();
                            shimmerFrameLayoutSubCat.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);
                        }else{
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "JEx: "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                catData.put("category", categoryid);
                return catData;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}