package com.example.coronaupdate1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CountryDetailActivity extends AppCompatActivity {

    private static final String TAG = "CountryDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);
        Log.d(TAG, "onCreate: started");
        
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intent and it's corresponding data");
        if(getIntent().hasExtra("country_name") && getIntent().hasExtra("flag_image")
                && getIntent().hasExtra("active_cases") && getIntent().hasExtra("total_cases")
                && getIntent().hasExtra("new_cases") && getIntent().hasExtra("total_deaths")
                && getIntent().hasExtra("new_deaths") && getIntent().hasExtra("total_recovered")
                && getIntent().hasExtra("new_recovered") && getIntent().hasExtra("total_tests")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String countryName = getIntent().getStringExtra("country_name");
            String flagImage = getIntent().getStringExtra("flag_image");
            String activeCases = getIntent().getStringExtra("active_cases");
            String totalCases = getIntent().getStringExtra("total_cases");
            String newCases = getIntent().getStringExtra("new_cases");
            String totalDeaths = getIntent().getStringExtra("total_deaths");
            String newDeaths = getIntent().getStringExtra("new_deaths");
            String totalRecovered = getIntent().getStringExtra("total_recovered");
            String newRecovered = getIntent().getStringExtra("new_recovered");
            String totalTests = getIntent().getStringExtra("total_tests");
            
            setData(countryName,flagImage,activeCases,totalCases,newCases,totalDeaths,
                    newDeaths,totalRecovered, newRecovered,totalTests);
        }
    }

    private void setData(String countryName, String flagImage, String activeCases, String totalCases, String newCases,
                         String totalDeaths, String newDeaths, String totalRecovered, String newRecovered,
                         String totalTests){

        TextView nameCountryView = findViewById(R.id.c_name_country);
        ImageView imageFlag = findViewById(R.id.c_flag_image);
        TextView casesActiveView = findViewById(R.id.c_active_cases_num);
        TextView casesTotalView = findViewById(R.id.c_total_cases_num);
        TextView casesNewView = findViewById(R.id.c_new_cases_num);
        TextView deathsTotalView = findViewById(R.id.c_total_deaths_num);
        TextView deathsNewView = findViewById(R.id.c_new_deaths_num);
        TextView recoveredTotalView = findViewById(R.id.c_total_recovered_num);
        TextView recoveredNewView = findViewById(R.id.c_new_recovered_num);
        TextView testsTotalView = findViewById(R.id.c_total_tests_num);

        Picasso.with(CountryDetailActivity.this).load(flagImage).into(imageFlag);
        nameCountryView.setText(countryName);
        casesActiveView.setText(activeCases);
        casesTotalView.setText(totalCases);
        casesNewView.setText(newCases);
        deathsTotalView.setText(totalDeaths);
        deathsNewView.setText(newDeaths);
        recoveredTotalView.setText(totalRecovered);
        recoveredNewView.setText(newRecovered);
        testsTotalView.setText(totalTests);

        Log.d(TAG, "setData: data assigned to textViews");
    }

}