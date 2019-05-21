package com.exercise.shoppingcart.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukundmurrali on 4/24/2019.
 */

public class AutoCompleteTextViewAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> mZipCodes;

    public AutoCompleteTextViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mZipCodes = new ArrayList<>();
    }

    public void setData(List<String> zipcodes){
        mZipCodes.clear();
        mZipCodes.addAll(zipcodes);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mZipCodes.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mZipCodes.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mZipCodes;
                    filterResults.count = mZipCodes.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
