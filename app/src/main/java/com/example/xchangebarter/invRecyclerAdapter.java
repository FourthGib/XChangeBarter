package com.example.xchangebarter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xchangebarter.Item.Item;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class invRecyclerAdapter extends RecyclerView.Adapter<invRecyclerAdapter.ViewHolder> {
    private static final String Tag = "Recycler view";

    private Context mContext;
    private ArrayList<Item> itemArrayList;

    public void setFoundSearch(ArrayList<Item> foundSearch){
        this.itemArrayList = foundSearch;
        notifyDataSetChanged();
    }

    private int item_count = 1;

    private RecyclerViewOnClickListener rvListener;

    public invRecyclerAdapter(Context mContext, ArrayList<Item> itemArrayList, RecyclerViewOnClickListener rvListener) {
        this.mContext = mContext;
        this.itemArrayList = itemArrayList;
        this.rvListener = rvListener;
    }

    @NonNull
    @Override
    public invRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_layout,parent,false);
        return new ViewHolder(view);
    }




    //Work in progress
    //
    ///
    //
    ///
    ///
    ///

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //if(itemArrayList.get(position).getName().equals("Itto")) {
            holder.name_view.setText(itemArrayList.get(position).getName());
            holder.descrip_view.setText(itemArrayList.get(position).getDescription());
            holder.tags_view.setText(itemArrayList.get(position).getTags());

            //Glide for images
            Glide.with(mContext).load(itemArrayList.get(position).getImgUrl()).into(holder.img_view);
            item_count++;
      /*}
        else{
            item_count--;
            holder.name_view.setText("Place Holder Name");
            holder.descrip_view.setText("Place Holder Description");
            holder.tags_view.setText("Please add new Item");
        }

       */

    }


    @Override
    public int getItemCount() {
        return itemArrayList.size();//item_count;
    }

    public interface RecyclerViewOnClickListener{
        void onClick(View v, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_view;
        TextView name_view, descrip_view, tags_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_view = itemView.findViewById(R.id.single_item_img);
            name_view = itemView.findViewById(R.id.single_item_name);
            descrip_view = itemView.findViewById(R.id.single_item_description);
            tags_view = itemView.findViewById(R.id.single_item_tags);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            rvListener.onClick(view, getAdapterPosition());
        }
    }
}
