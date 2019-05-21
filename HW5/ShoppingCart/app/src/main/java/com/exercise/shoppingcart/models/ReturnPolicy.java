package com.exercise.shoppingcart.models;

import java.io.Serializable;

public class ReturnPolicy implements Serializable {

    public String getPolicy() {
        return policy;
    }

    public String getReturns_within() {
        return returns_within;
    }

    public String getRefund_mode() {
        return refund_mode;
    }

    public String getShipped_by() {
        return shipped_by;
    }

    private String policy;
    private String returns_within;
    private String refund_mode;
    private String shipped_by;

}
