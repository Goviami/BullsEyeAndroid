package com.goviami.bullseye.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.goviami.bullseye.R;
import com.goviami.bullseye.model.Country;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;


/**
 * Created by subbu on 11/2/2014.
 */
public class CountrySpinnerAdapter extends ArrayAdapter<Country> {

    private List<Country> countries;
    private LayoutInflater inflater;
    private Context context;

    public CountrySpinnerAdapter(Context context, int resource, List<Country> countries) {
        super(context, resource, countries);
        this.context = context;
        this.countries = countries;
        inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        Cell cell;
        Country country = countries.get(position);

        if (convertView == null) {
            cell = new Cell();
            cellView = inflater.inflate(R.layout.country_spinner_row, null);
            cell.textView = (TextView) cellView.findViewById(R.id.row_title);
            cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }

        cell.textView.setText(country.getName());

        // Load drawable dynamically from country code
        String drawableName = "flag_"
                + country.getIsoCode().toLowerCase(Locale.ENGLISH);
        cell.imageView.setImageResource(getResId(drawableName));
        return cellView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        Cell cell;
        Country country = countries.get(position);

        if (convertView == null) {
            cell = new Cell();
            cellView = inflater.inflate(R.layout.country_spinner_selected_row, null);
            cell.textView = (TextView) cellView.findViewById(R.id.row_title);
            cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }

        cell.textView.setText(country.getName());

        // Load drawable dynamically from country code
        String drawableName = "flag_"
                + country.getIsoCode().toLowerCase(Locale.ENGLISH);
        cell.imageView.setImageResource(getResId(drawableName));
        return cellView;
    }

    /**
     * The drawable image name has the format "flag_$countryCode". We need to
     * load the drawable dynamically from country code.
     *
     * @param drawableName
     * @return
     */
    private int getResId(String drawableName) {

        try {
            Class<R.drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            Log.e("REGISTER", "Failure to get drawable id.", e);
        }
        return -1;
    }

    /**
     * Holder for the cell
     *
     */
    static class Cell {
        public TextView textView;
        public ImageView imageView;
    }
 }
