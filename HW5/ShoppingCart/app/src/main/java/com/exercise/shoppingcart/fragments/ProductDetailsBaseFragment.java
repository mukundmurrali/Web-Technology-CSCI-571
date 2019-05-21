package com.exercise.shoppingcart.fragments;

import android.support.v4.app.Fragment;

import com.exercise.shoppingcart.models.ProductDetailsResponse;

/**
 * Created by mukundmurrali on 4/16/2019.
 */

public abstract class ProductDetailsBaseFragment extends Fragment {

    protected ProductDetailsResponse productDetailsResponse;

    protected void updateFragmentContent(){

    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse) {
        this.productDetailsResponse = productDetailsResponse;
        updateFragmentContent();
    }

}
