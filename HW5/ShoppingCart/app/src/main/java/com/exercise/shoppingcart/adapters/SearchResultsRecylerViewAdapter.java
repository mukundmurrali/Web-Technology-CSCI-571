package com.exercise.shoppingcart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.models.SearchResponseItem;

import java.util.ArrayList;

/**
 * Created by mukundmurrali on 4/12/2019.
 */

public class SearchResultsRecylerViewAdapter extends RecyclerView.Adapter {

    ArrayList<SearchResponseItem> mValues;
    ArrayList<String> mWishlistItemIds;
    Context mContext;
    protected ItemListener mListener;

    public SearchResultsRecylerViewAdapter(Context context, ArrayList<SearchResponseItem> values, ArrayList<String> wishlistItemIds, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
        mWishlistItemIds = wishlistItemIds;
    }

    public void updateSearchResults(ArrayList<SearchResponseItem> updatedValues){
        mValues = updatedValues;
        notifyDataSetChanged();
    }

    public void updateWishlistItemIds(ArrayList<String> updateWishlist){
        mWishlistItemIds = updateWishlist;
        notifyDataSetChanged();
    }

    public class SearchResultsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SearchResponseItem item;

        private TextView textView_itemTitle;
        private ImageView imageView_itemImage;
        private ImageView imageView_itemWishlist;
        private TextView textView_zip;
        private TextView textView_shipping;
        private TextView textView_condition;
        private TextView textView_price;

        public SearchResultsViewHolder(View view) {
            super(view);
            textView_itemTitle = view.findViewById(R.id.textview_itemTitle);
            textView_zip = view.findViewById(R.id.textview_itemZip);
            textView_shipping = view.findViewById(R.id.textview_itemShipping);
            textView_condition = view.findViewById(R.id.textview_itemCondition);
            textView_price = view.findViewById(R.id.textview_itemPrice);
            imageView_itemImage = view.findViewById(R.id.imageview_itemImage);
            imageView_itemWishlist = view.findViewById(R.id.imageview_itemWishlist);

            imageView_itemWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lines = textView_itemTitle.getLineCount();
                    String sentence = textView_itemTitle.getText().toString();
                    if (textView_itemTitle.getLayout().getEllipsisCount(lines-1) > 0) {
                        int end = textView_itemTitle.getLayout().getLineEnd(textView_itemTitle.getMaxLines() - 1);
                        sentence = textView_itemTitle.getText().toString().substring(0, end) + "...";
                    }
                    if(mWishlistItemIds == null || mWishlistItemIds.contains(item.getItemId())){
                        Toast toast = Toast.makeText(mContext, String.format("%s was removed from wish list", sentence), Toast.LENGTH_LONG);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(13);
                        toast.show();
                        mListener.onItemRemovedFromWishlist(item);
                    } else {
                        Toast toast = Toast.makeText(mContext, String.format("%s was added to wish list", sentence), Toast.LENGTH_LONG);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(13);
                        toast.show();
                        mListener.onItemWishlisted(item);
                    }
                }
            });
            view.setOnClickListener(this);
        }

        public void setData(SearchResponseItem item) {
            this.item = item;
            textView_itemTitle.setText(item.getTitle());
            textView_zip.setText(String.format("Zip: %s",item.getZipcode()));
            textView_price.setText(String.format("$ %s",item.getPrice()));
            textView_shipping.setText(item.getShippingCost());
            textView_condition.setText(item.getCondition());
            if(mWishlistItemIds == null || mWishlistItemIds.contains(item.getItemId())){
                imageView_itemWishlist.setImageResource(R.drawable.ic_cart_remove);
            } else {
                imageView_itemWishlist.setImageResource(R.drawable.ic_cart_plus);
            }
        }

        public ImageView getImageView(){
            return this.imageView_itemImage;
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, parent, false);
        return new SearchResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((SearchResultsViewHolder)viewHolder).setData(mValues.get(position));
        Glide.with(mContext)
                .load(mValues.get(position).getImage()).error(R.drawable.ic_image_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((SearchResultsViewHolder) viewHolder).getImageView());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(SearchResponseItem item);
        void onItemWishlisted(SearchResponseItem item);
        void onItemRemovedFromWishlist(SearchResponseItem item);
    }
}