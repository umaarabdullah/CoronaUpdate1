package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class GraphModellingActivity extends AppCompatActivity {

    private static final String TAG = "GraphModellingActivity";
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

        getSupportActionBar().setTitle("Graphs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("country_name") && getIntent().hasExtra("active_cases")
                && getIntent().hasExtra("total_cases") && getIntent().hasExtra("new_cases")
                && getIntent().hasExtra("total_deaths") && getIntent().hasExtra("new_deaths")
                && getIntent().hasExtra("total_recovered") && getIntent().hasExtra("new_recovered")
                && getIntent().hasExtra("total_tests")){

            countryName = getIntent().getStringExtra("country_name");
            activeCases = getIntent().getStringExtra("active_cases");
            totalCases = getIntent().getStringExtra("total_cases");
            newCases = getIntent().getStringExtra("new_cases");
            totalDeaths = getIntent().getStringExtra("total_deaths");
            newDeaths = getIntent().getStringExtra("new_deaths");
            totalRecovered = getIntent().getStringExtra("total_recovered");
            newRecovered = getIntent().getStringExtra("new_recovered");
            totalTests = getIntent().getStringExtra("total_tests");
            Log.d(TAG, "onCreate: intent recieved " + countryName);

        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // making the up button do the same as back button (home) is the id of the up button
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}