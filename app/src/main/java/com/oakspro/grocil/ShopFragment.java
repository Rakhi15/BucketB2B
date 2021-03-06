package com.oakspro.grocil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

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
    String api_cat_list="https://grocil.in/grocil_android/api/category_api.php";
    ArrayList<MainDataCat> dataArrayList=new ArrayList<>();
    CategoryAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Button refresh_balance, walletBtn;
    TextView balance_txt;
    SharedPreferences sharedPreferences;

    ArrayList<SliderItem> adsArray=new ArrayList<>();
    SliderView adSlider;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_shop, container, false);
        sharedPreferences= getContext().getSharedPreferences("MyUser", Context.MODE_PRIVATE);

        recyclerView=root.findViewById(R.id.recyclerViewCat);
        newNest=root.findViewById(R.id.new_nest);
        swipeRefreshLayout=root.findViewById(R.id.swipe_refresh);
        shimmerFrameLayoutCat=root.findViewById(R.id.shimmerLayout_cat);
        shimmerFrameLayoutCat.startShimmer();
        refresh_balance=root.findViewById(R.id.top_up_refresh);
        walletBtn=root.findViewById(R.id.wallet_btn);
        balance_txt=root.findViewById(R.id.amount_txt);


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adSlider=root.findViewById(R.id.ads_slider);

        getCategoryList();
/*
        newNest.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY==v.getChildAt(0).getMeasuredHeight() -v.getMeasuredHeight()){
                    getCategoryList();
                }
            }
        });


 */
        walletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), TransActivity.class);
                intent.putExtra("userid", sharedPreferences.getString("userid", ""));
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dataArrayList.clear();
                getCategoryList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private void getCategoryList() {
        StringRequest request=new StringRequest(Request.Method.POST, api_cat_list, new Response.Listener<String>() {
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
                                MainDataCat data=new MainDataCat();

                                data.setCategoryName(object.getString("cat_name"));
                                data.setCategoryImg(object.getString("cat_img"));
                                data.setCategoryId(object.getString("cat_sl"));
                                data.setCategoryStatus(object.getString("cat_status"));

                                dataArrayList.add(data);
                            }
                            adapter=new CategoryAdapter(getContext(), dataArrayList);
                            shimmerFrameLayoutCat.stopShimmer();
                            shimmerFrameLayoutCat.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(adapter);

                            //ads
                            adsArray.clear();
                            JSONArray jsonArray2=jsonObject.getJSONArray("ads");
                            for (int i=0; i<jsonArray2.length(); i++){
                                JSONObject object=jsonArray2.getJSONObject(i);

                                SliderItem sliderItem=new SliderItem();
                                sliderItem.setAd_id(object.getString("ad_id"));
                                sliderItem.setAd_img(object.getString("ad_img"));
                                sliderItem.setAd_link(object.getString("ad_link"));

                                adsArray.add(sliderItem);
                            }
                            SliderImgAdapter adapter = new SliderImgAdapter(getContext(), adsArray);

                            adSlider.setSliderAdapter(adapter);

                            adSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            adSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                            adSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            adSlider.setIndicatorSelectedColor(Color.WHITE);
                            adSlider.setIndicatorUnselectedColor(Color.GRAY);
                            adSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                            adSlider.startAutoCycle();

                            //end of ads



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
                catData.put("data", "category");
                return catData;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }
}