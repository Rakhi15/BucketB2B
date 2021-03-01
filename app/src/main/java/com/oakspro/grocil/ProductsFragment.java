package com.oakspro.grocil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ProductsFragment extends Fragment {

    Button backHome;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_products, container, false);
        backHome=root.findViewById(R.id.back_Home);
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
}