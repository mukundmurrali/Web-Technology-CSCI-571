package com.exercise.shoppingcart.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.adapters.SearchResultsRecylerViewAdapter;
import com.exercise.shoppingcart.layoutManagers.SearchResultsItemDecoration;
import com.exercise.shoppingcart.models.SearchResponseItem;
import com.exercise.shoppingcart.utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by mukundmurrali on 4/9/2019.
 */

public class WishlistFragment extends Fragment implements SearchResultsRecylerViewAdapter.ItemListener {

    private TextView textViewNoWishes;
    private TextView textViewWishlistTotalLabel;
    private TextView textViewWishlistTotalValue;
    private RecyclerView recyclerViewWishlist;

    private SearchResultsRecylerViewAdapter recyclerViewAdapter;
    private ArrayList<SearchResponseItem> wishlistItems;

    private IWishlistFragmentListener wishlistFragmentListener;

    public interface IWishlistFragmentListener {
        void onItemSelectedInWishlist(SearchResponseItem item);
    }

    public WishlistFragment(){

    }

    @SuppressLint("ValidFragment")
    public WishlistFragment(IWishlistFragmentListener listener){
        wishlistFragmentListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        textViewNoWishes = view.findViewById(R.id.textViewNoWishes);
        textViewWishlistTotalLabel = view.findViewById(R.id.textView_totalLabel);
        textViewWishlistTotalValue = view.findViewById(R.id.textView_totalValue);
        recyclerViewWishlist = view.findViewById(R.id.recyclerView_searchResults);

        wishlistItems = SharedPreferenceManager.getSharedPreferenceManager(this.getActivity().getApplicationContext()).getWishlistedItems();
        if(wishlistItems == null){
            wishlistItems = new ArrayList<>();
        }

        recyclerViewAdapter = new SearchResultsRecylerViewAdapter(this.getActivity(), wishlistItems, null, this);
        recyclerViewWishlist.setAdapter(recyclerViewAdapter);
        GridLayoutManager manager = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerViewWishlist.setLayoutManager(manager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_spacing);
        recyclerViewWishlist.addItemDecoration(new SearchResultsItemDecoration(spacingInPixels));

        return view;
    }

    private void updateWishlist(){
        wishlistItems = SharedPreferenceManager.getSharedPreferenceManager(this.getActivity().getApplicationContext()).getWishlistedItems();
        if(wishlistItems == null){
            wishlistItems = new ArrayList<>();
        }
        recyclerViewAdapter.updateSearchResults(wishlistItems);
        if(wishlistItems.size() == 0){
            textViewWishlistTotalLabel.setText("Wishlist total (0 items):");
            textViewWishlistTotalValue.setText("$ 0.00");
            textViewNoWishes.setVisibility(View.VISIBLE);
            recyclerViewWishlist.setVisibility(View.GONE);
        } else {
            String items = "";
            if(wishlistItems.size() == 1){
                items = "(1 item)";
            } else {
                items = "(" + wishlistItems.size() + " items)";
            }
            double sum = 0.00;
            Iterator<SearchResponseItem> iterator = wishlistItems.iterator();
            while(iterator.hasNext()){
                sum += Double.parseDouble(iterator.next().getPrice());
            }
            textViewWishlistTotalLabel.setText(String.format("Wishlist total %s:",items));
            textViewWishlistTotalValue.setText(String.format("$ %s",sum));
            textViewNoWishes.setVisibility(View.GONE);
            recyclerViewWishlist.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWishlist();
    }

    @Override
    public void onItemClick(SearchResponseItem item) {
        wishlistFragmentListener.onItemSelectedInWishlist(item);
    }

    @Override
    public void onItemWishlisted(SearchResponseItem item) {

    }

    @Override
    public void onItemRemovedFromWishlist(SearchResponseItem item) {
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getSharedPreferenceManager(this.getActivity().getApplicationContext());
        sharedPreferenceManager.removeItemFromWishlist(item);
        updateWishlist();
    }
}
