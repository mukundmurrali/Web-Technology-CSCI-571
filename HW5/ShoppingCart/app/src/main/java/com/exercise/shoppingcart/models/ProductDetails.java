package com.exercise.shoppingcart.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class ProductDetails implements Serializable{
    private String[] Photo;
    private String Title;
    private String Price;
    private String Location;
    private ReturnPolicy Return_Policy;

    private ShippingTab shipping_info;
    private Map<String, String> ItemSpecifics;
    private SellerTab seller_tab;
    private String facebook_link;
    private SimilarItems[] similar_items;

    private String[] google_photos;

    public String[] getPhoto() {
        return Photo;
    }

    public String getTitle() {
        return Title;
    }

    public String getPrice() {
        return Price;
    }

    public String getLocation() {
        return Location;
    }

    public ReturnPolicy getReturn_Policy() {
        return Return_Policy;
    }

    public Map<String, String> getItemSpecifics() {
        return ItemSpecifics;
    }

    public SellerTab getSeller_tab() {
        return seller_tab;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public SimilarItems[] getSimilar_items() {
        return similar_items;
    }

    public String[] getGoogle_photos() {
        return google_photos;
    }

    public ShippingTab getShippingTab() {
        return shipping_info;
    }

}
