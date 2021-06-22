package com.example.coronaupdate1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GraphModellingActivity extends AppCompatActivity {

    private String countryName;
    private String activeCases;
    private String totalCases;
    private String newCases;
    private String totalDeaths;
    private String newDeaths;
    private String totalRecovered;
    private String newRecovered;
    private String totalTests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_modelling);
        getActionBar().setTitle("Graphs");
        /*
        countryName = getIntent().getStringExtra("country_name");
        activeCases = getIntent().getStringExtra("active_cases");
        totalCases = getIntent().getStringExtra("total_cases");
        newCases = getIntent().getStringExtra("new_cases");
        totalDeaths = getIntent().getStringExtra("total_deaths");
        newDeaths = getIntent().getStringExtra("new_deaths");
        totalRecovered = getIntent().getStringExtra("total_recovered");
        newRecovered = getIntent().getStringExtra("new_recovered");
        totalTests = getIntent().getStringExtra("total_tests");*/

        TextView textView = findViewById(R.id.textView);
        textView.setText("activeCases");
    }
}