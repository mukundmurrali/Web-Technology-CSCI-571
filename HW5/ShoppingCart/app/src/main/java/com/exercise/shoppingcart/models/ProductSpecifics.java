package com.exercise.shoppingcart.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mukundmurrali on 4/13/2019.
 */

public class ProductSpecifics implements Serializable {
    @SerializedName("Cosmetic condition")
    private String CosmeticConfition;
    @SerializedName("Original box")
    private String OriginalBox;
    private String Brand;
    private String Model;
    @SerializedName("Storage Capacity")
    private String StorageCapacity;
    private String Color;
    @SerializedName("Model Number")
    private String ModelNumber;
    private String Network;
    private String Connectivity;
    private String Processor;
    @SerializedName("Operating System")
    private String OperatingSystem;
    @SerializedName("Lock Status")
    private String LockStatus;
    @SerializedName("Manufacturer Color")
    private String ManufacturerColor;
    private String Contract;
    @SerializedName("Camera Resolution")
    private String CameraResolution;
    private String MPN;

    public String getCosmeticConfition() {
        return CosmeticConfition;
    }

    public String getOriginalBox() {
        return OriginalBox;
    }

    public String getBrand() {
        return Brand;
    }

    public String getModel() {
        return Model;
    }

    public String getStorageCapacity() {
        return StorageCapacity;
    }

    public String getColor() {
        return Color;
    }

    public String getModelNumber() {
        return ModelNumber;
    }

    public String getNetwork() {
        return Network;
    }

    public String getConnectivity() {
        return Connectivity;
    }

    public String getProcessor() {
        return Processor;
    }

    public String getOperatingSystem() {
        return OperatingSystem;
    }

    public String getLockStatus() {
        return LockStatus;
    }

    public String getManufacturerColor() {
        return ManufacturerColor;
    }

    public String getContract() {
        return Contract;
    }

    public String getCameraResolution() {
        return CameraResolution;
    }

    public String getMPN() {
        return MPN;
    }
}
