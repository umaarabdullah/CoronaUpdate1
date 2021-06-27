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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private ArrayList<String> countryNamesArrayList = new ArrayList<String>();
    private ArrayList<String> newCasesArrayList = new ArrayList<String>();
    private ArrayList<String> newDeathsArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_modelling);

        // setting the title in the actionBar
        getSupportActionBar().setTitle("Graphs");

        // enabling the up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // checking if the appropriate intent extras were received properly or not
        if(getIntent().hasExtra("country_name") && getIntent().hasExtra("active_cases")
                && getIntent().hasExtra("total_cases") && getIntent().hasExtra("new_cases")
                && getIntent().hasExtra("total_deaths") && getIntent().hasExtra("new_deaths")
                && getIntent().hasExtra("total_recovered") && getIntent().hasExtra("new_recovered")
                && getIntent().hasExtra("total_tests")
                && getIntent().hasExtra("country_name_array_list")
                && getIntent().hasExtra("new_cases_array_list")
                && getIntent().hasExtra("new_deaths_array_list")){

            // getting the data which was passed from the previous activity
            countryName = getIntent().getStringExtra("country_name");
            activeCases = getIntent().getStringExtra("active_cases");
            totalCases = getIntent().getStringExtra("total_cases");
            newCases = getIntent().getStringExtra("new_cases");
            totalDeaths = getIntent().getStringExtra("total_deaths");
            newDeaths = getIntent().getStringExtra("new_deaths");
            totalRecovered = getIntent().getStringExtra("total_recovered");
            newRecovered = getIntent().getStringExtra("new_recovered");
            totalTests = getIntent().getStringExtra("total_tests");

            countryNamesArrayList = getIntent().getStringArrayListExtra("country_name_array_list");
            newCasesArrayList = getIntent().getStringArrayListExtra("new_cases_array_list");
            newDeathsArrayList = getIntent().getStringArrayListExtra("new_deaths_array_list");

            // getting the current date
            // date is formatted as Ex : 28-Dec-2020
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = simpleDateFormat.format(date);


            // Column Chart using anyChart
            AnyChartView anyChartView = findViewById(R.id.any_chart_view_global);
            anyChartView.setProgressBar(findViewById(R.id.progress_bar_global));

            Cartesian cartesian = AnyChart.column();

            List<DataEntry> casesData = new ArrayList<>();
            int countEntries=0;

            // X and Y axis data assigned
            // column chart usually fails to render if cases are below 20 (assumption)
            for (int i=0; i<countryNamesArrayList.size(); i++){

                int cases = Integer.parseInt(newCasesArrayList.get(i));

                if(cases > 20){

                    casesData.add( new ValueDataEntry(countryNamesArrayList.get(i), cases)) ;

                    Log.d(TAG, "onCreate: countryName " + countryNamesArrayList.get(i)
                            + " Cases " + cases );
                }
            }

            Column column = cartesian.column(casesData);

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            cartesian.animation(true);
            cartesian.title("Daily New Cases By Country (Date: " + formattedDate + ")");

            cartesian.yScale().minimum(0d);

            cartesian.xAxis(0).labels().fontSize(5);
            cartesian.xAxis(0).labels().fontStyle("bold");
            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            cartesian.xAxis(0).title("Country");
            cartesian.yAxis(0).title("Cases");

            anyChartView.setChart(cartesian);

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