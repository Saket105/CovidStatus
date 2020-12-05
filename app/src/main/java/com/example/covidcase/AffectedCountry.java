package com.example.covidcase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covidcase.Adapter.CountryAdapter;
import com.example.covidcase.model.Country;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountry extends AppCompatActivity {
    EditText edt_search;
    ListView listView;
    SimpleArcLoader simpleArcLoader;

    public static List<Country> countries = new ArrayList<>();
    Country countryModel;
    CountryAdapter countryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_country);

        edt_search = findViewById(R.id.edtsearch);
        listView = findViewById(R.id.listView);
        simpleArcLoader = findViewById(R.id.loader_one);

        getSupportActionBar().setTitle("All Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),DetailActivity.class).putExtra("position",position));
                Toast.makeText(AffectedCountry.this, "All Details...", Toast.LENGTH_SHORT).show();
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countryAdapter.getFilter().filter(s);
                countryAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/countries/";

        //  simpleArcLoader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String country_name = jsonObject.getString("country");
                                String country_cases = jsonObject.getString("cases");
                                String country_today_case = jsonObject.getString("todayCases");
                                String country_death = jsonObject.getString("deaths");
                                String country_today_death = jsonObject.getString("todayDeaths");
                                String country_recovered = jsonObject.getString("recovered");
                                String country_active = jsonObject.getString("active");
                                String country_critical = jsonObject.getString("critical");
                                String country_today_recovered = jsonObject.getString("todayRecovered");

                                JSONObject object = jsonObject.getJSONObject("countryInfo");
                                String flagUrl = object.getString("flag");

                                countryModel = new Country(
                                        flagUrl,
                                        country_name,
                                        country_cases,
                                        country_today_case,
                                        country_death,
                                        country_today_death,
                                        country_recovered,
                                        country_today_recovered,
                                        country_active,
                                        country_critical);

                                countries.add(countryModel);
                            }
                            countryAdapter = new CountryAdapter(AffectedCountry.this,countries);
                            listView.setAdapter(countryAdapter);
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);

                Toast.makeText(AffectedCountry.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}