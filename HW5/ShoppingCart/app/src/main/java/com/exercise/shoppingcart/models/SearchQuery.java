package com.exercise.shoppingcart.models;

import java.io.Serializable;

/**
 * Created by mukundmurrali on 4/10/2019.
 */

public class SearchQuery implements Serializable{
    private String keyword;
    private int category = -1;

    private boolean isNew;
    private boolean isUsed;
    private boolean isUnspecified;

    private boolean isLocalPickup;
    private boolean isFreeShipping;

    private boolean isNearby;
    private int miles = -1;
    private String distance;
    private int zipCode = -1;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isUnspecified() {
        return isUnspecified;
    }

    public void setUnspecified(boolean unspecified) {
        isUnspecified = unspecified;
    }

    public boolean isLocalPickup() {
        return isLocalPickup;
    }

    public void setLocalPickup(boolean localPickup) {
        isLocalPickup = localPickup;
    }

    public boolean isFreeShipping() {
        return isFreeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        isFreeShipping = freeShipping;
    }

    public boolean isNearby() {
        return isNearby;
    }

    public void setNearby(boolean nearby) {
        isNearby = nearby;
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}
