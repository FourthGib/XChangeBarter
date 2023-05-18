package com.example.xchangebarter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xchangebarter.Item.Item;
import com.example.xchangebarter.Trade.Trade;

import java.util.ArrayList;

public class tradeRecyclerAdapter extends RecyclerView.Adapter<tradeRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Trade> tradeArrayList;

    public void setFoundSearch(ArrayList<Trade> foundSearch){
        this.tradeArrayList = foundSearch;
        notifyDataSetChanged();
    }

    private tradeRecyclerAdapter.RecyclerViewOnClickListener rvListener;

    public tradeRecyclerAdapter(Context mContext, ArrayList<Trade> tradeArrayList, tradeRecyclerAdapter.RecyclerViewOnClickListener rvListener) {
        this.mContext = mContext;
        this.tradeArrayList = tradeArrayList;
        this.rvListener = rvListener;
    }

    @NonNull
    @Override
    public tradeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_layout,parent,false);
        return new tradeRecyclerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull tradeRecyclerAdapter.ViewHolder holder, int position) {
        //if(itemArrayList.get(position).getName().equals("Itto")) {
        holder.trade_status_view.setText(tradeArrayList.get(position).getStatus());
        String receiving = "For: " + tradeArrayList.get(position).getReceiverItemTitle();
        holder.receive_item_view.setText(receiving);
        String trading = "Trading: " + tradeArrayList.get(position).getInitiatorItemTitle();
        holder.give_item_view.setText(trading);
    }


    @Override
    public int getItemCount() {
        return tradeArrayList.size();//item_count;
    }

    public interface RecyclerViewOnClickListener{
        void onClick(View v, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView give_item_view, receive_item_view, trade_status_view;

        public ViewHolder(@NonNull View tradeView) {
            super(tradeView);

            give_item_view = itemView.findViewById(R.id.give_item);
            receive_item_view = itemView.findViewById(R.id.receive_item);
            trade_status_view = itemView.findViewById(R.id.trade_status);
            tradeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            rvListener.onClick(view, getAdapterPosition());
        }
    }
}