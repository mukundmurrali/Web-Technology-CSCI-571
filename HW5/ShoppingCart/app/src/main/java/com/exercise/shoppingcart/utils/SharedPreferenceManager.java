package com.exercise.shoppingcart.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.exercise.shoppingcart.fragments.SearchResultsFragment;
import com.exercise.shoppingcart.models.SearchResponseItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by mukundmurrali on 4/14/2019.
 */

public class SharedPreferenceManager {

    private Context mContext;
    private static SharedPreferenceManager sharedPreferenceManager;

    private SharedPreferenceManager(Context context){
        mContext = context;
    }

    public static SharedPreferenceManager getSharedPreferenceManager(Context context){
        if(sharedPreferenceManager == null){
            sharedPreferenceManager = new SharedPreferenceManager(context);
        }
        return sharedPreferenceManager;
    }

    public ArrayList<SearchResponseItem> getWishlistedItems(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shoppingcart",Context.MODE_PRIVATE);
        Set<String> wishlistStringSet = sharedPreferences.getStringSet("wishlist",null);
        if(wishlistStringSet != null){
            ArrayList<SearchResponseItem> wishlistItems = new ArrayList<SearchResponseItem>();
            Iterator<String> iterator = wishlistStringSet.iterator();
            while(iterator.hasNext()){
                String itemString = iterator.next();
                Gson gson = new Gson();
                SearchResponseItem item = gson.fromJson(itemString,SearchResponseItem.class);
                wishlistItems.add(item);
            }
            return wishlistItems;
        }
        return null;
    }

    public void addItemToWishlist(SearchResponseItem item){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shoppingcart",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        Set<String> wishlistStringSet = sharedPreferences.getStringSet("wishlist",null);
        if(wishlistStringSet == null){
            wishlistStringSet = new HashSet<String>();
        }
        Gson gson = new Gson();
        String itemString = gson.toJson(item, SearchResponseItem.class);
        wishlistStringSet.add(itemString);
        editor.putStringSet("wishlist", wishlistStringSet);
        editor.commit();
    }

    public boolean removeItemFromWishlist(SearchResponseItem item){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("shoppingcart",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        Set<String> wishlistStringSet = sharedPreferences.getStringSet("wishlist",null);
        if(wishlistStringSet != null){
            Gson gson = new Gson();
            String itemString = gson.toJson(item, SearchResponseItem.class);
            wishlistStringSet.remove(itemString);
            editor.putStringSet("wishlist", wishlistStringSet);
            editor.commit();
        }
        return false;
    }
}
