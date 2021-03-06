package com.oakspro.grocil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    Context context;
    ArrayList<CartData> dataArrayList;
    ArrayList<OrdersData> orderArrayList;
    private String cart_api="https://grocil.in/grocil_android/api/order_api.php";
    SharedPreferences sharedPreferences;
    public double total_price=0;
    TextView totalTv;
    SwipeButton enableButton;

    public CartAdapter(Context context, ArrayList<CartData> dataArrayList, TextView totalTv, SwipeButton enableButton){
        this.context=context;
        this.dataArrayList=dataArrayList;
        this.totalTv=totalTv;
        this.enableButton=enableButton;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        sharedPreferences= context.getSharedPreferences("MyUser", Context.MODE_PRIVATE);


        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                Toast.makeText(context, "Order: " + active, Toast.LENGTH_SHORT).show();

                OrdersData Odata=new OrdersData();


            }
        });


        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartData data=dataArrayList.get(position);

        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double qty_n=Double.parseDouble(holder.qnty.getText().toString());
                qty_n=qty_n-1;
                if (qty_n>1){
                    holder.qnty.setText(String.valueOf(qty_n));
                    Double total_val=Double.parseDouble(holder.qnty.getText().toString())*Double.parseDouble(data.getProd_price().toString());
                    holder.prodTotal.setText("Rs. "+String.valueOf(total_val));
                    total_price=total_price-Double.parseDouble(data.getProd_price());
                    totalTv.setText("Total Rs. : "+String.valueOf(total_price));
                }
            }
        });
        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double qty_n=Double.parseDouble(holder.qnty.getText().toString());
                qty_n=qty_n+1;
                holder.qnty.setText(String.valueOf(qty_n));
                Double total_val=Double.parseDouble(holder.qnty.getText().toString())*Double.parseDouble(data.getProd_price().toString());
                holder.prodTotal.setText("Rs. "+String.valueOf(total_val));
                total_price=total_price+Double.parseDouble(data.getProd_price());
                totalTv.setText("Total Rs. : "+String.valueOf(total_price));

            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCart(data.getProd_id(), sharedPreferences.getString("userid", ""));
            }
        });
        String pro_img_address="https://grocil.in/grocil_android/api/product_pics/"+data.getProd_img();
        Picasso.get().load(pro_img_address).into(holder.prodImg);

        holder.prodTitle.setText(data.getProd_title());
        holder.prodUnit.setText(""+data.getProd_units()+" in 1 Unit, Price: Rs. "+data.getProd_price());
        holder.prodTotal.setText("Rs. "+data.getProd_price());
        holder.qnty.setText(data.getProd_qty());
        calculateTotal(data.getProd_price().toString());


    }

    private void calculateTotal(String price) {
        Double price_i=Double.parseDouble(price);
        total_price=total_price+price_i;
        totalTv.setText("Total Rs. "+String.valueOf(total_price));
    }

    private void removeCart(String prod_id, String userid) {

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView prodImg;
        TextView prodTitle, prodPrice, prodUnit,prodTotal;
        Button plusBtn, minusBtn, deleteBtn;
        EditText qnty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImg=itemView.findViewById(R.id.img_prod);
            prodTitle=itemView.findViewById(R.id.product_name);
            prodUnit=itemView.findViewById(R.id.items);
            plusBtn=itemView.findViewById(R.id.btn2);
            minusBtn= itemView.findViewById(R.id.btn1);
            prodTotal=itemView.findViewById(R.id.stotal);
            deleteBtn=itemView.findViewById(R.id.delete_btn);
            qnty=itemView.findViewById(R.id.quantity_ed);

        }
    }
}
