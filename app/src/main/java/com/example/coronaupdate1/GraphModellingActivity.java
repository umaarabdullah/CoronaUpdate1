package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.coronaupdate1.DataModel.DbCountryData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GraphModellingActivity extends AppCompatActivity {

    private static final String TAG = "GraphModellingActivity";
    private String countryName;

    private DatabaseReference mRootRef;
    private final List<DbCountryData> selectedCountryData = new ArrayList<DbCountryData>();        // selected country data by dates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_modelling);
        Log.d(TAG, "onCreate: ");

        // setting the title in the actionBar
        getSupportActionBar().setTitle("Graphs");

        // enabling the up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // checking if the appropriate intent extras were received properly or not
        if(getIntent().hasExtra("country_name")){

            // getting the data which was passed from the previous activity
            countryName = getIntent().getStringExtra("country_name");
            Log.d(TAG, "onCreate: got intent");


            // referencing the correct branch in the database. I.e the countryName whose detail screen was clicked
            if(countryName == "S. Korea"){
                mRootRef = FirebaseDatabase.getInstance().getReference().child("CountryData").child("South Korea");
            }
            else if (countryName == "St. Barth"){
                mRootRef = FirebaseDatabase.getInstance().getReference().child("CountryData").child("Saint Barth");
            }
            else{
                mRootRef = FirebaseDatabase.getInstance().getReference().child("CountryData").child(countryName);
            }
            Log.d(TAG, "onCreate: referenced countryName branch correctly");


            // retrieving data from the dataBase
            mRootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    // iterating through dates and adding the data at each date to the list
                    for (DataSnapshot dateDataSnapshot : snapshot.getChildren()){

                        DbCountryData dbCountryData = dateDataSnapshot.getValue(DbCountryData.class);

                        // adding to the list
                        selectedCountryData.add(dbCountryData);

                        Log.d(TAG, "onDataChange: countryName " + "dbCountryData.getCountryName()");
                        Log.d(TAG, "onDataChange: childrenCount " + dateDataSnapshot.getChildrenCount());
                        Log.d(TAG, "onDataChange: Key " + dateDataSnapshot.getKey());
                    }

                    Log.d(TAG, "onCreate: retrieved data successfully");

                    // after data is fetched the column chart is drawn
                    setNewCasesColumnChart();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }

    private void setNewCasesColumnChart(){

        // Column Chart using anyChart
        AnyChartView anyChartView = findViewById(R.id.any_chart_view_country);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_country));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> casesData = new ArrayList<>();

        // X and Y axis data assigned
        // column chart usually fails to render if cases are below 20 (assumption)
        for (int i=0; i<selectedCountryData.size(); i++){

            int cases = Integer.parseInt(selectedCountryData.get(i).getNewCases());

            if(cases > 20){
                casesData.add(new ValueDataEntry(selectedCountryData.get(i).getDate(), cases));
            }
        }

        Column column = cartesian.column(casesData);
        column.color("#FBC02D");    // setting column bar color

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Daily New Cases - " + countryName);
        cartesian.title().fontStyle("bold");

        cartesian.yScale().minimum(0d);

        cartesian.xAxis(0).labels().fontSize(10);
        cartesian.xAxis(0).labels().fontStyle("bold");
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Dates");
        cartesian.yAxis(0).title("Cases");

        anyChartView.setChart(cartesian);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
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