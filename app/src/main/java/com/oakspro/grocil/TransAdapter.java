package com.oakspro.grocil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransAdapter extends RecyclerView.Adapter<TransAdapter.ViewHolder>{

    Context context;
    ArrayList<TransData> dataArrayList;


    public TransAdapter(Context context, ArrayList<TransData> dataArrayList){
        this.context=context;
        this.dataArrayList=dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_item, parent, false);
        return new TransAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransData data=dataArrayList.get(position);

        holder.transTv.setText(data.getTransType()+" - "+data.getReason());
        if (data.getTransType().equals("CREDIT")){
            holder.amountTv.setTextColor(Color.GREEN);
            holder.amountTv.setText("+ ₹"+data.getAmount());
        }else if (data.getTransType().equals("DEBIT")){
            holder.amountTv.setTextColor(Color.RED);
            holder.amountTv.setText("- ₹"+data.getAmount());
        }
        holder.dateTv.setText(data.getDate());
        holder.transIDTv.setText("Trans ID: "+data.getTransID());
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView transTv, amountTv, dateTv, transIDTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            transTv=itemView.findViewById(R.id.type_txt);
            amountTv=itemView.findViewById(R.id.amount);
            dateTv=itemView.findViewById(R.id.date);
            transIDTv=itemView.findViewById(R.id.transid);

        }
    }
}
