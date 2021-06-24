package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

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

            // getting the current date
            Date date = Calendar.getInstance().getTime();
            Log.d(TAG, "onCreate: current time -> " + date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            // date is formatted as Ex : 28-Dec-2020
            String formattedDate = simpleDateFormat.format(date);

            // Column Chart using anyChart
            AnyChartView anyChartView = findViewById(R.id.any_chart_view);
            anyChartView.setProgressBar(findViewById(R.id.progress_bar));

            Cartesian cartesian = AnyChart.column();

            List<DataEntry> casesData = new ArrayList<>();
            casesData.add(new ValueDataEntry(formattedDate, 80540));
            casesData.add(new ValueDataEntry("24/6/21", 70540));
            casesData.add(new ValueDataEntry("25/6/21", 70540));
            casesData.add(new ValueDataEntry("26/6/21", 8923));

            Column column = cartesian.column(casesData);

            column.tooltip()
                    .titleFormat("{X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            cartesian.animation(true);
            cartesian.title("Daily New Cases");

            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            cartesian.xAxis(0).title("Date");
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