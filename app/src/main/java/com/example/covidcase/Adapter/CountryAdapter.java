package com.example.covidcase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.covidcase.AffectedCountry;
import com.example.covidcase.R;
import com.example.covidcase.model.Country;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CountryAdapter extends ArrayAdapter<Country> {

    private Context context;
    private List<Country> countryList;
    private List<Country> countryListFilter;


    public CountryAdapter(Context context, List<Country> countryList) {
        super(context, R.layout.country_design, countryList);

        this.context = context;
        this.countryList = countryList;
        this.countryListFilter = countryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_design,null,true);

        TextView country_name = view.findViewById(R.id.country_name);
        ImageView country_flag = view.findViewById(R.id.imageFlag);

        country_name.setText(countryListFilter.get(position).getCountry());
        Glide.with(context).load(countryListFilter.get(position).getFlag()).into(country_flag);

        return view;
    }

    @Override
    public int getCount() {
        return countryListFilter.size();
    }

    @Nullable
    @Override
    public Country getItem(int position) {
        return countryListFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = countryList.size();
                    filterResults.values = countryList;

                }else{
                    List<Country> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(Country itemsModel:countryList){
                        if(itemsModel.getCountry().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);

                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }


                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                countryListFilter = (List<Country>) results.values;
                AffectedCountry.countries = (List<Country>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}
