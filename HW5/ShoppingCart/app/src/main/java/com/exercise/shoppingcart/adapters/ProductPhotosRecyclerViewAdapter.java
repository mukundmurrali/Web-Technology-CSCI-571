package com.exercise.shoppingcart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exercise.shoppingcart.R;

import java.util.ArrayList;

/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class ProductPhotosRecyclerViewAdapter extends RecyclerView.Adapter {

    ArrayList<String> mPhotos;
    Context mContext;

    public ProductPhotosRecyclerViewAdapter(Context context, ArrayList<String> photos){
        mContext = context;
        mPhotos = photos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_photoitem, viewGroup, false);
        return new ProductPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ProductPhotosViewHolder)viewHolder).setItem(mPhotos.get(position));
        Glide.with(mContext)
                .load(mPhotos.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ic_image_black_24dp)
                .into(((ProductPhotosViewHolder)viewHolder).getImageView());
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void updatePhotos(ArrayList<String> photos){
        mPhotos = photos;
        notifyDataSetChanged();
    }

    public class ProductPhotosViewHolder extends RecyclerView.ViewHolder {

        private String item;
        private ImageView imageView;

        public ProductPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recyclerView_photoItem);
        }

        public void setItem(String item) {
            this.item = item;
        }

        public ImageView getImageView(){
            return this.imageView;
        }


    }
}
