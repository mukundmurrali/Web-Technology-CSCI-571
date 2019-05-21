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

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by mukundmurrali on 4/16/2019.
 */

public class GooglePhotosRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<String> mImages;
    private Context mContext;

    public GooglePhotosRecyclerViewAdapter(Context context, ArrayList<String> images){
        mContext = context;
        mImages = images;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_googlephotoitem, viewGroup, false);
        return new GooglePhotoItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((GooglePhotoItemsViewHolder)viewHolder).setData(mImages.get(position));
        Glide.with(mContext)
                .load(mImages.get(position)).error(R.drawable.ic_image_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((GooglePhotoItemsViewHolder) viewHolder).getImageView());
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public void updatePhotos(ArrayList<String> images){
        mImages = images;
        notifyDataSetChanged();
    }

    public class GooglePhotoItemsViewHolder extends RecyclerView.ViewHolder {

        private String item;

        private ImageView imageView_googlephotoitem;

        public GooglePhotoItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_googlephotoitem = itemView.findViewById(R.id.imageView_googlephotoitem);
        }

        public void setData(String item){
            this.item = item;
        }

        public ImageView getImageView(){
            return this.imageView_googlephotoitem;
        }
    }
}
