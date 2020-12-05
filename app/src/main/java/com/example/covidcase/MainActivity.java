package com.example.covidcase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView total_cases, total_deaths, critical, today_case, today_death, recovered, active, country;
    SimpleArcLoader simpleArcLoader;
    ScrollView scrollView;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        total_cases = findViewById(R.id.total_cases);
        total_deaths = findViewById(R.id.total_death);
        critical = findViewById(R.id.critical);
        today_case = findViewById(R.id.today_cases);
        today_death = findViewById(R.id.today_death);
        recovered = findViewById(R.id.recovered);
        active = findViewById(R.id.active_cases);
        country = findViewById(R.id.total_country);

        simpleArcLoader = findViewById(R.id.loader);
        scrollView = findViewById(R.id.scrollView);
        pieChart = findViewById(R.id.piechart);

        fetchData();

    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all/";

        simpleArcLoader.start();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            total_cases.setText(jsonObject.getString("cases"));
                            total_deaths.setText(jsonObject.getString("deaths"));
                            critical.setText(jsonObject.getString("critical"));
                            today_case.setText(jsonObject.getString("todayCases"));
                            today_death.setText(jsonObject.getString("todayDeaths"));
                            recovered.setText(jsonObject.getString("recovered"));
                            active.setText(jsonObject.getString("active"));
                            country.setText(jsonObject.getString("affectedCountries"));

                            pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(total_cases.getText().toString()), Color.parseColor("#E60A0A")));
                            pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(recovered.getText().toString()), Color.parseColor("#FFEB3B")));
                            pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(total_deaths.getText().toString()), Color.parseColor("#000000")));
                            pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(active.getText().toString()), Color.parseColor("#4CAF50")));

                            pieChart.startAnimation();

                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void goTrackCountries(View view) {
        startActivity(new Intent(getApplicationContext(),AffectedCountry.class));
    }
}