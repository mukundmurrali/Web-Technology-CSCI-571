package com.exercise.shoppingcart.models;

import java.io.Serializable;

/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class ProductDetailsResponse implements Serializable {
    private ProductDetails product_details;
    public ProductDetails getProduct_details() {
        return product_details;
    }
}
