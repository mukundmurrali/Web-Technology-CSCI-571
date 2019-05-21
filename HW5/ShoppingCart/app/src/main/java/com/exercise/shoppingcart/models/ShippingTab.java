package com.exercise.shoppingcart.models;

import java.io.Serializable;

public class ShippingTab implements Serializable {

    public String getGlobal_shipping() {
        return global_shipping;
    }

    public String getHandling_time() {
        return handling_time;
    }

    public String getCondition() {
        return condition;
    }

    private String  global_shipping;
    private String handling_time;
    private String condition;
}
