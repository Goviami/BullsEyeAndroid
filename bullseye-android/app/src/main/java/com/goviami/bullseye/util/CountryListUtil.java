package com.goviami.bullseye.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.goviami.bullseye.R;
import com.goviami.bullseye.model.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subbu on 11/10/2014.
 */
public class CountryListUtil {
    private static List<Country> countriesList;
    public static Country currentCountry;

    public static List<Country> getAllCountries(Context context){
        try{
            // Read the countries string from file and convert to JSON Object.
            String jsonCountriesString = readFileAsString(context);
            JSONArray countryArray = new JSONArray(jsonCountriesString);
            // Get the iso country code
            String simIsoCode = getCurrentCountryCode(context);
            // Iterate through each county (key) and map them to Country.java Object
            JSONObject countryObject;
            Country country;
            countriesList = new ArrayList<Country>();
            for(int i=0; i<countryArray.length(); i++){
                countryObject = countryArray.getJSONObject(i);
                country = new Country();
                country.setName(countryObject.getString("name"));
                country.setIsoCode(countryObject.getString("isoCode"));
                String phoneCode = countryObject.getString("phoneCode");
                phoneCode = phoneCode.substring(0, phoneCode.length()-1);
                country.setPhoneCode(Integer.valueOf(phoneCode));
                countriesList.add(country);
                if(simIsoCode.equalsIgnoreCase(country.getIsoCode())){
                    currentCountry = country;
                }
            }
            //Collections.sort(countriesList, new CountryComparator());
        } catch (IOException e) {
            Log.e("REGISTER", "Unable to read JSON Country String", e);
            e.printStackTrace();
        } catch (JSONException e){
            Log.e("REGISTER","Unable to convert JSON Country String", e);
            e.printStackTrace();
        }

        return countriesList;
    }

    /**
     * Get current Local of the user.
     *
     * @return
     */
    private static String getCurrentCountryCode(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int phoneType = tm.getPhoneType();
        String isoCode = "US";
        switch (phoneType){
            case TelephonyManager.PHONE_TYPE_GSM:
                isoCode = tm.getNetworkCountryIso();
                break;
            case  TelephonyManager.PHONE_TYPE_CDMA:
                isoCode = tm.getNetworkCountryIso();
                break;
            case TelephonyManager.PHONE_TYPE_NONE:
                break;
        }
        if(isoCode == null){
            isoCode = "US";
        }
        return isoCode;
    }

    /**
     * R.string.countries is a json string which is Base64 encoded to avoid
     * special characters in XML. It's Base64 decoded here to get original json.
     *
     * @param context
     * @return
     * @throws java.io.IOException
     */
    private static String readFileAsString(Context context)
            throws java.io.IOException {
        String base64 = context.getResources().getString(R.string.countries);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }
}
