package com.exercise.shoppingcart.models;

import java.io.Serializable;

/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class SellerTab implements Serializable {
    private String seller_name;
    private String feedback_score;
    private String popularity;
    private String feedback_rating_star;
    private String toprated;
    private String storename;
    private String buy_product_at;

    public String getSeller_name() {
        return seller_name;
    }

    public String getFeedback_score() {
        return feedback_score;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getFeedback_rating_star() {
        return feedback_rating_star;
    }

    public String getToprated() {
        return toprated;
    }

    public String getStorename() {
        return storename;
    }

    public String getBuy_product_at() {
        return buy_product_at;
    }
}
