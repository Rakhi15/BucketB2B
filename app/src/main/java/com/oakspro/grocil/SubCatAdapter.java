package com.oakspro.grocil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.ViewHolder> {

    Context context;
    ArrayList<MainDataSubCat> dataArrayList;



    public SubCatAdapter(Context context, ArrayList<MainDataSubCat> dataArrayList) {
        this.context=context;
        this.dataArrayList=dataArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_items, parent, false);
        return new SubCatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainDataSubCat data=dataArrayList.get(position);

        Shimmer shimmer=new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#F3F3F3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();

        ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        holder.pro_name.setText(data.getProductName());
        holder.pro_price.setText(data.getProductPrice());
        holder.pro_units.setText(data.getProductUnit());

        String cat_img_address="https://grocil.in/grocil_android/api/sub_category_pics/"+data.getProductImage();
        Picasso.get().load(cat_img_address).into(holder.pro_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pro_name, pro_price, pro_units;
        ImageView pro_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pro_image=itemView.findViewById(R.id.cat_item_image);
            pro_price=itemView.findViewById(R.id.cat_item_rate);
            pro_units=itemView.findViewById(R.id.cat_item_quant);
            pro_name=itemView.findViewById(R.id.cat_item_title);
        }
    }
}
