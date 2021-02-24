package com.oakspro.grocil;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;

public class ShopFragment extends Fragment {

    RecyclerView recyclerView;
    NestedScrollView newNest;
    ShimmerFrameLayout shimmerFrameLayoutCat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView=root.findViewById(R.id.recyclerViewCat);
        newNest=root.findViewById(R.id.new_nest);
        shimmerFrameLayoutCat=root.findViewById(R.id.shimmerLayout_cat);
        shimmerFrameLayoutCat.startShimmer();

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

    }
}