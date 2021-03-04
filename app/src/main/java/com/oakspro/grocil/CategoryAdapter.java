package com.oakspro.grocil;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    Context context;
    ArrayList<MainDataCat> dataArrayList;

    public CategoryAdapter(Context context, ArrayList<MainDataCat> dataArrayList){
        this.context=context;
        this.dataArrayList=dataArrayList;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainDataCat data=dataArrayList.get(position);

        Shimmer shimmer=new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#F3F3F3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        holder.catName.setText(data.getCategoryName());
        String cat_img_address="https://grocil.in/grocil_android/api/category_pics/"+data.getCategoryImg();
        Picasso.get().load(cat_img_address).into(holder.catImg);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("category", data.getCategoryId());
                AppCompatActivity appCompatActivity=(AppCompatActivity)v.getContext();
                Fragment myfragment=new ProductsFragment();
                myfragment.setArguments(bundle);
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myfragment).addToBackStack(null).commit();
            }
        });


        /*
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bt=new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                View view= LayoutInflater.from(context).inflate(R.layout.login_bottom_sheet,null);
                bt.setContentView(view);
                bt.show();

            }
        });

         */

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView catName;
        ImageView catImg;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catName=itemView.findViewById(R.id.cat_name);
            catImg=itemView.findViewById(R.id.cat_img);
            cardView=itemView.findViewById(R.id.cardView1);
        }
    }


}
