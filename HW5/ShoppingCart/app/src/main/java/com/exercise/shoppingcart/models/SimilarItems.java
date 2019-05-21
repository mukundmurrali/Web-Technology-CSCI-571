package com.exercise.shoppingcart.models;

import java.io.Serializable;

/**
 * Created by mukundmurrali on 4/15/2019.
 */

public class SimilarItems implements Serializable {
    private String photo;
    private String title;
    private String price;
    private String shipping_cost;
    private String days_left;
    private String view_item_url;

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public String getDays_left() {
        return days_left;
    }

    public String getView_item_url() {
        return view_item_url;
    }

}
