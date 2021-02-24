package com.oakspro.grocil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<MainDataCat> dataCats;

    public CategoryAdapter(Context context, ArrayList<MainDataCat> dataCats){
        this.context=context;
        this.dataCats=dataCats;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainDataCat data=dataCats.get(position);

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
        String cat_img_address="https://grocil.in/android_api/category_img"+data.getCategoryImg();
        Picasso.get().load(cat_img_address).into(holder.catImg);

    }

    @Override
    public int getItemCount() {
        return dataCats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView catName;
        ImageView catImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catName=itemView.findViewById(R.id.cat_name);
            catImg=itemView.findViewById(R.id.cat_img);
        }
    }

}
