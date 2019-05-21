package com.exercise.shoppingcart.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exercise.shoppingcart.R;
import com.exercise.shoppingcart.adapters.SearchResultsRecylerViewAdapter;
import com.exercise.shoppingcart.layoutManagers.SearchResultsItemDecoration;
import com.exercise.shoppingcart.models.SearchQuery;
import com.exercise.shoppingcart.models.SearchResponseItem;
import com.exercise.shoppingcart.utils.AppConstants;
import com.exercise.shoppingcart.utils.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mukundmurrali on 4/11/2019.
 */

public class SearchResultsFragment extends Fragment implements SearchResultsRecylerViewAdapter.ItemListener {

    private ProgressBar progressBar;
    private LinearLayout linearLayoutProgress;
    private LinearLayout linearLayoutResults;
    private LinearLayout linearLayoutNoResults;
    private RecyclerView recyclerViewSearchResults;
    private TextView textViewResultCount;
    private TextView textViewResultKeyword;

    private SearchQuery searchQuery;
    private ArrayList<SearchResponseItem> searchResults;
    private ArrayList<String> wishListItemIds;
    private SearchResultsRecylerViewAdapter searchResultsRecylerViewAdapter;

    private ISearchResultsFragmentListener searchResultsFragmentListener;

    public interface ISearchResultsFragmentListener {
        public void OnItemSelected(SearchResponseItem searchResponseItem);
    }

    public SearchResultsFragment(){

    }

    @SuppressLint("ValidFragment")
    public SearchResultsFragment(ISearchResultsFragmentListener listener){
        this.searchResultsFragmentListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_searchresults, container, false);
        progressBar = view.findViewById(R.id.progressBar_searchProducts);
        linearLayoutProgress = view.findViewById(R.id.linearlayout_progress);
        linearLayoutResults = view.findViewById(R.id.linearlayout_resultcount);
        linearLayoutNoResults = view.findViewById(R.id.linearlayout_noresults);
        recyclerViewSearchResults = view.findViewById(R.id.recyclerView_searchResults);
        textViewResultCount = view.findViewById(R.id.textview_resultcount);
        textViewResultKeyword = view.findViewById(R.id.textview_resultkeyword);

        searchResults = new ArrayList<>();
        wishListItemIds = new ArrayList<>();
        searchResultsRecylerViewAdapter = new SearchResultsRecylerViewAdapter(this.getActivity(),searchResults,wishListItemIds,this);
        recyclerViewSearchResults.setAdapter(searchResultsRecylerViewAdapter);
        GridLayoutManager manager = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerViewSearchResults.setLayoutManager(manager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_spacing);
        recyclerViewSearchResults.addItemDecoration(new SearchResultsItemDecoration(spacingInPixels));

        searchQuery = (SearchQuery)getArguments().getSerializable("searchQuery");

        ProductSearchTask productSearchTask = new ProductSearchTask();
        productSearchTask.execute(searchQuery);

        return view;
    }

    @Override
    public void onResume() {
        updateWishlistInAdapter();
        super.onResume();
    }

    @Override
    public void onItemClick(SearchResponseItem item) {
        this.searchResultsFragmentListener.OnItemSelected(item);
    }

    @Override
    public void onItemWishlisted(SearchResponseItem item) {
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getSharedPreferenceManager(this.getActivity().getApplicationContext());
        sharedPreferenceManager.addItemToWishlist(item);
        updateWishlistInAdapter();
    }

    @Override
    public void onItemRemovedFromWishlist(SearchResponseItem item) {
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getSharedPreferenceManager(this.getActivity().getApplicationContext());
        sharedPreferenceManager.removeItemFromWishlist(item);
        updateWishlistInAdapter();
    }

    private void updateWishlistInAdapter(){
        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getSharedPreferenceManager(this.getActivity().getApplicationContext());
        final ArrayList<SearchResponseItem> wishlistedItems = sharedPreferenceManager.getWishlistedItems();
        if(wishlistedItems != null) {
            wishListItemIds.clear();
            Iterator<SearchResponseItem> iterator = wishlistedItems.iterator();
            while (iterator.hasNext()) {
                wishListItemIds.add(iterator.next().getItemId());
            }
            searchResultsRecylerViewAdapter.updateWishlistItemIds(wishListItemIds);
        }
    }

    public class ProductSearchTask extends AsyncTask<SearchQuery, Void, String>{

        private OkHttpClient okHttpClient = new OkHttpClient();
        private String testUrl = "http://10.0.2.2:8081/searchResults?product_name=iphone&category=58058&new=New&used=Used&dis=zip&miles=10&zipcode=90007";

        private String getSearchURLFromSearchQuery(SearchQuery searchQuery){
            String url = AppConstants.BASE_URL + AppConstants.SEARCH_ENDPOINT;
            if(searchQuery.getCategory() != -1){
                url += String.format("?category=%s",searchQuery.getCategory());
            }
            if(searchQuery.getKeyword() != null){
                url += String.format("&product_name=%s",searchQuery.getKeyword());
            }
            if(searchQuery.isNew()){
                url += String.format("&new=New");
            }
            if(searchQuery.isUsed()){
                url += String.format("&used=Used");
            }
            if(searchQuery.isUnspecified()){
                url += String.format("&unspecified=Unspecified");
            }
            if(searchQuery.isLocalPickup()){
                url += String.format("&localpickup=Local+Pickup");
            }
            if(searchQuery.isFreeShipping()){
                url += String.format("&freeshipping=Free+Shipping");
            }
            if(searchQuery.isNearby()){
                url+="&enablenearbysearch=true";
                if(searchQuery.getMiles() != -1){
                    url += String.format("&miles=%s",searchQuery.getMiles());
                }
                if(searchQuery.getDistance() != null){
                    if(searchQuery.getDistance().equals("zip")){
                        url += String.format("&dis=zip&zipcode=%s",searchQuery.getZipCode());
                    } else if(searchQuery.getDistance().equals("here")){
                        url += String.format("&dis=here&zipcode_here=%s",searchQuery.getZipCode());
                    }
                }
            }
            return url;
        }

        @Override
        protected void onPreExecute() {
            linearLayoutProgress.setVisibility(View.VISIBLE);
            linearLayoutNoResults.setVisibility(View.GONE);
            linearLayoutResults.setVisibility(View.GONE);
            recyclerViewSearchResults.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String responseString) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<SearchResponseItem>>(){}.getType();
            ArrayList<SearchResponseItem> searchResults = gson.fromJson(responseString,listType);

            linearLayoutProgress.setVisibility(View.GONE);

            if(searchResults == null) {
                Toast.makeText(getActivity(), String.format("Possible network error in fetching search results"), Toast.LENGTH_LONG).show();
                linearLayoutNoResults.setVisibility(View.VISIBLE);
            } else if(searchResults.size() == 0){
                linearLayoutNoResults.setVisibility(View.VISIBLE);
            } else {
                linearLayoutNoResults.setVisibility(View.GONE);
                linearLayoutResults.setVisibility(View.VISIBLE);

                textViewResultCount.setText(searchResults.size() + " ");
                textViewResultKeyword.setText(searchQuery.getKeyword());

                recyclerViewSearchResults.setVisibility(View.VISIBLE);
                searchResultsRecylerViewAdapter.updateSearchResults(searchResults);
                updateWishlistInAdapter();
            }

        }

        @Override
        protected String doInBackground(SearchQuery... searchQueries) {
            Request.Builder builder = new Request.Builder();
            String url = getSearchURLFromSearchQuery(searchQueries[0]);
            builder.url(url);
            Request request = builder.build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
            }
            return null;
        }
    }
}
