package com.postoffice.model;

public class Parcel {

    private Float weight;
    private String postalCode;

    public Parcel(Float weight, String postalCode) {
        this.weight = weight;
        this.postalCode = postalCode;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
