package com.exercise.shoppingcart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.models.SimilarItems;

import java.util.ArrayList;

/**
 * Created by mukundmurrali on 4/16/2019.
 */

public class SimilarItemsRecyclerViewAdapter extends RecyclerView.Adapter {

    ArrayList<SimilarItems> similarItems;
    Context mContext;
    ISimilarItemsRecyclerViewAdapterListener mListener;

    public SimilarItemsRecyclerViewAdapter(Context context, ArrayList<SimilarItems> items, ISimilarItemsRecyclerViewAdapterListener listener){
        mContext = context;
        similarItems = items;
        mListener = listener;
    }

    public interface ISimilarItemsRecyclerViewAdapterListener {
        void onItemClicked(SimilarItems item);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_similaritem, viewGroup, false);
        return new SimilarItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((SimilarItemsViewHolder)viewHolder).setData(similarItems.get(position));
        Glide.with(mContext)
                .load(similarItems.get(position).getPhoto()).error(R.drawable.ic_image_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((SimilarItemsViewHolder) viewHolder).getImageView());
    }

    @Override
    public int getItemCount() {
        return similarItems.size();
    }

    public void updateSimilarItems (ArrayList<SimilarItems> updatedItems){
        similarItems = updatedItems;
        notifyDataSetChanged();
    }

    public class SimilarItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SimilarItems item;

        private ImageView imageView_similarItems;
        private TextView textView_title;
        private TextView textView_shipping;
        private TextView textView_daysleft;
        private TextView textView_price;

        public SimilarItemsViewHolder(@NonNull View view) {
            super(view);
            textView_title = view.findViewById(R.id.textView_title);
            textView_shipping = view.findViewById(R.id.textView_shipping);
            textView_daysleft = view.findViewById(R.id.textView_daysleft);
            textView_price = view.findViewById(R.id.textView_price);
            imageView_similarItems = view.findViewById(R.id.imageView_similarItemImage);
            view.setOnClickListener(this);
        }

        public void setData(SimilarItems item){
            this.item = item;
            if(!(item.getTitle() == null || item.getTitle().equals("N/A"))) {
                textView_title.setText(item.getTitle());
            }
            if(!(item.getShipping_cost() == null || item.getShipping_cost().equals("N/A"))) {
                textView_shipping.setText((Math.round(Float.parseFloat(item.getShipping_cost())) == 0) ? "Free Shipping" : "$ " + item.getShipping_cost());
            }
            if(!(item.getDays_left() == null || item.getDays_left().equals("N/A"))) {
                textView_daysleft.setText(item.getDays_left() + " days left");
            }
            if(!(item.getDays_left() == null || item.getDays_left().equals("N/A"))) {
                textView_price.setText("$" + item.getPrice());
            }
        }

        public ImageView getImageView(){
            return this.imageView_similarItems;
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClicked(item);
        }
    }
}
