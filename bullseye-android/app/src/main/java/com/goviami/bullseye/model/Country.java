package com.goviami.bullseye.model;

/**
 * Created by subbu on 11/2/2014.
 */
public class Country {
    private String name;
    private String isoCode;
    private int phoneCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public int getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(int phoneCode) {
        this.phoneCode = phoneCode;
    }

    @Override
    public String toString() {
        return this.name;            // What to display in the Spinner list.
    }
}