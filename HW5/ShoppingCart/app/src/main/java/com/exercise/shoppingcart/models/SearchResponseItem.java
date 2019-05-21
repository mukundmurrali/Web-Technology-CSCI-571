package com.exercise.shoppingcart.models;

import java.io.Serializable;

/**
 * Created by mukundmurrali on 4/12/2019.
 */

public class SearchResponseItem implements Serializable {
    private int index;
    private String itemId;
    private String image;
    private String title;
    private String price;
    private String shipping;

    public String getSubtitle() {
        return subtitle;
    }

    private String subtitle;

    public int getIndex() {
        return index;
    }

    public String getItemId() {
        return itemId;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getShipping() {
        return shipping;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getShippingCost() {
        return shipping_cost;
    }

    public String getCondition() {
        return condition;
    }

    private String zipcode;
    private String sellerName;
    private String shipping_cost;

    private String condition;

}
