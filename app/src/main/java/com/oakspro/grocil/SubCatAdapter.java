package com.oakspro.grocil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.ViewHolder> {

    Context context;
    ArrayList<MainDataSubCat> dataArrayList;
    SharedPreferences sharedPreferences;
    private String cart_api="https://grocil.in/grocil_android/api/cart_api.php";




    public SubCatAdapter(Context context, ArrayList<MainDataSubCat> dataArrayList) {
        this.context=context;
        this.dataArrayList=dataArrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_items, parent, false);
        sharedPreferences= context.getSharedPreferences("MyUser", Context.MODE_PRIVATE);
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
        holder.pro_price.setText("Price: ₹"+data.getProductPrice());
        holder.pro_units.setText("1 Pack Units: "+data.getProductUnit());

        String pro_img_address="https://grocil.in/grocil_android/api/product_pics/"+data.getProductImage();
        Picasso.get().load(pro_img_address).into(holder.pro_image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bt=new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
                View view= LayoutInflater.from(context).inflate(R.layout.bottom_sheet_product_details,null);
                view.findViewById(R.id.addtocart_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show();
                        uploadCart(data.getProductID(), sharedPreferences.getString("userid", "").toString(),data.getProductCid());
                        bt.dismiss();
                    }
                });
                ImageView pimage=view.findViewById(R.id.product_img);
                TextView pname=view.findViewById(R.id.product_name);
                TextView pprice=view.findViewById(R.id.product_price);
                TextView punit=view.findViewById(R.id.product_unit);
                TextView pnote=view.findViewById(R.id.product_note);
                TextView pmisc=view.findViewById(R.id.product_misc);

                Picasso.get().load(pro_img_address).into(pimage);
                pname.setText(data.getProductName());
                pprice.setText("Price: ₹"+data.getProductPrice());
                punit.setText("1 Pack Units: "+data.getProductUnit());
                pnote.setText("Note: "+data.getProductNote());
                pmisc.setText("Misc: "+data.getProductMisc());

                bt.setContentView(view);
                bt.show();

            }
        });
    }

    private void uploadCart(String productPID, String userid, String productCid) {
        StringRequest request=new StringRequest(Request.Method.POST, cart_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    String status=object.getString("jstatus");
                    if (status.equals("1")){
                        Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                    }else if (status.equals("0")){
                        Toast.makeText(context, "Not Added", Toast.LENGTH_SHORT).show();
                    }else if (status.equals("2")){
                        Toast.makeText(context, "Already Added", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> dt=new HashMap<>();
                dt.put("pcid", productCid);
                dt.put("userid", userid);
                dt.put("pid", productPID);
                return dt;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pro_name, pro_price, pro_units;
        ImageView pro_image;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pro_image=itemView.findViewById(R.id.cat_item_image);
            pro_price=itemView.findViewById(R.id.cat_item_rate);
            pro_units=itemView.findViewById(R.id.cat_item_quant);
            pro_name=itemView.findViewById(R.id.cat_item_title);
            cardView=itemView.findViewById(R.id.cardView1);
        }
    }
}
