package com.goviami.bullseye.util;

import com.goviami.bullseye.model.Country;

import java.util.Comparator;

/**
 * Created by subbu on 11/2/2014.
 */
public class CountryComparator implements Comparator<Country>{
    @Override
    public int compare(Country c1, Country c2) {
        return c1.getName().compareTo(c2.getName());
    }
}
