package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.Stroke;
import com.example.coronaupdate1.DataModel.DbCountryData;
import com.example.coronaupdate1.utility.StringNumber;
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
        setContentView(R.layout.activity_graph_country_modelling);
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


            // referencing the correct branch in the database. I.e on the basis of countryName whose detail screen was clicked
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

                        // fetching data
                        DbCountryData dbCountryData = dateDataSnapshot.getValue(DbCountryData.class);

                        // adding to the list
                        selectedCountryData.add(dbCountryData);

                        Log.d(TAG, "onDataChange: countryName " + "dbCountryData.getCountryName()");
                        Log.d(TAG, "onDataChange: childrenCount " + dateDataSnapshot.getChildrenCount());
                        Log.d(TAG, "onDataChange: Key " + dateDataSnapshot.getKey());
                    }

                    Log.d(TAG, "onCreate: retrieved data successfully");

                    // after data is fetched several charts are drawn
                    setNewCasesColumnChart();
                    setNewDeathsColumnChart();
                    setPieChart();
                    setLineChartDailyCasesDeathsRecovered();
                    setLineChartTotalTestsCases();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }

    private void setNewCasesColumnChart(){

        // Column Chart using anyChart
        AnyChartView anyChartView = findViewById(R.id.any_chart_view_c_new_cases);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);   // very important line of code for multiple charts
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_c_new_cases));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> casesData = new ArrayList<>();

        // X and Y axis data assigned
        // (Experimental thought) sometime column chart fails to render if cases are below 20 (assumption) or near 0 or 0
        for (int i=0; i<selectedCountryData.size(); i++){

            int cases = Integer.parseInt(selectedCountryData.get(i).getNewCases());

            casesData.add(new ValueDataEntry(selectedCountryData.get(i).getDate(), cases));
        }

        Column column = cartesian.column(casesData);
        column.color("#b2beb5");    // setting column bar color

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        // prettifying the chart title
        cartesian.animation(true);
        cartesian.title("Daily New Cases - " + countryName);
        cartesian.title().fontColor("#000000");
        cartesian.title().fontOpacity(10);
        cartesian.title().fontStyle("bold");

        cartesian.yScale().minimum(0d);

        // prettifying the xAxis labels, individual values of x
        cartesian.xAxis(0).labels().fontOpacity(10);
        cartesian.xAxis(0).labels().fontStyle("bold");
        cartesian.xAxis(0).labels().fontColor("#000000");

        // prettifying the yAxis labels, individual values of y
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.yAxis(0).labels().fontOpacity(10);
        cartesian.yAxis(0).labels().fontStyle("bold"); // prettifying
        cartesian.yAxis(0).labels().fontColor("#000000");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        // prettifying the xAxis title
        cartesian.xAxis(0).title("Dates");
        cartesian.xAxis(0).title().fontOpacity(10);
        cartesian.xAxis(0).title().fontColor("#000000");
        cartesian.xAxis(0).title().fontStyle("bold");

        // prettifying the yAxis title
        cartesian.yAxis(0).title("Cases");
        cartesian.yAxis(0).title().fontOpacity(10);
        cartesian.yAxis(0).title().fontColor("#000000");
        cartesian.yAxis(0).title().fontStyle("bold");

        anyChartView.setChart(cartesian);
    }

    private void setNewDeathsColumnChart(){

        // Column Chart using anyChart
        AnyChartView anyChartView = findViewById(R.id.any_chart_view_c_new_deaths);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);   // very important line of code for multiple charts
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_c_new_deaths));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> deathsData = new ArrayList<>();

        // X and Y axis data assigned
        // (Experimental thought) sometime column chart fails to render if cases are below 20 (assumption) or near 0 or 0
        for (int i=0; i<selectedCountryData.size(); i++){

            int deaths = Integer.parseInt(selectedCountryData.get(i).getNewDeaths());

            deathsData.add(new ValueDataEntry(selectedCountryData.get(i).getDate(), deaths));
        }

        Column column = cartesian.column(deathsData);
        column.color("#ae0700");    // setting column bar color

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        // prettifying the chart title
        cartesian.animation(true);
        cartesian.title("Daily New Deaths - " + countryName);
        cartesian.title().fontColor("#000000");
        cartesian.title().fontOpacity(10);
        cartesian.title().fontStyle("bold");

        cartesian.yScale().minimum(0d);

        // prettifying the xAxis labels, individual values of x
        cartesian.xAxis(0).labels().fontOpacity(10);
        cartesian.xAxis(0).labels().fontStyle("bold");
        cartesian.xAxis(0).labels().fontColor("#000000");

        // prettifying the yAxis labels, individual values of y
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.yAxis(0).labels().fontOpacity(10);
        cartesian.yAxis(0).labels().fontStyle("bold"); // prettifying
        cartesian.yAxis(0).labels().fontColor("#000000");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        // prettifying the xAxis title
        cartesian.xAxis(0).title("Dates");
        cartesian.xAxis(0).title().fontOpacity(10);
        cartesian.xAxis(0).title().fontColor("#000000");
        cartesian.xAxis(0).title().fontStyle("bold");

        // prettifying the yAxis title
        cartesian.yAxis(0).title("Deaths");
        cartesian.yAxis(0).title().fontOpacity(10);
        cartesian.yAxis(0).title().fontColor("#000000");
        cartesian.yAxis(0).title().fontStyle("bold");

        anyChartView.setChart(cartesian);
    }

    private void setPieChart(){

        // pie chart using anyChart library
        AnyChartView anyChartView = findViewById(R.id.any_chart_view_c_pie_chart);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);   // very important line for multiple charts
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_c_pie_chart));

        Pie pie = AnyChart.pie();

        // setting a click listener to trigger a toast when the pie chart is clicked
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(GraphModellingActivity.this, event.getData().get("x") + ":" +
                        event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        // last index is the latest data
        int listSize = selectedCountryData.size();

        // adding data
        List<DataEntry> lastestCountryCasesData = new ArrayList<>();
        lastestCountryCasesData.add(new ValueDataEntry("Active", Integer.parseInt(selectedCountryData.get(listSize-1).getActiveCases())));
        lastestCountryCasesData.add(new ValueDataEntry("Death", Integer.parseInt(selectedCountryData.get(listSize-1).getTotalDeaths())));
        lastestCountryCasesData.add(new ValueDataEntry("Recovered", Integer.parseInt(selectedCountryData.get(listSize-1).getTotalRecovered())));

        pie.data(lastestCountryCasesData);

        // formatting total cases
        StringNumber stringNumber = new StringNumber();
        String totalCasesFormatted = stringNumber.bigNumberFormatting(selectedCountryData.get(listSize-1).getTotalCases());

        // prettifying the pie chart title
        pie.title("Distribution of Total Cases : " + countryName + " : " + totalCasesFormatted +
                " Date-" + selectedCountryData.get(listSize-1).getDate());
        pie.title().fontColor("#000000");
        pie.title().fontOpacity(10);
        pie.title().fontStyle("bold");

        // prettifying the labels outside the pie chart the numbers in %
        pie.labels().position("outside");
        pie.labels().fontColor("#000000");
        pie.labels().fontOpacity(10);
        pie.labels().fontStyle("bold");

        // prettifying the Active, Death, Recovered legend text
        pie.legend().fontColor("#000000");
        pie.legend().fontOpacity(10);
        pie.legend().fontStyle("bold");

        // prettifying the legend title text Possible Outcomes
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Possible Outcomes")
                .padding(0d, 0d, 10d, 0d);
        pie.legend().title().fontOpacity(10);
        pie.legend().title().fontStyle("bold");
        pie.legend().title().fontColor("#000000");

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);

    }

    private void setLineChartDailyCasesDeathsRecovered(){

        // Line Chart using anyChart
        AnyChartView anyChartView = findViewById(R.id.any_chart_view_c_case_death_recovered_curve);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);   // very important line of code for multiple charts
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_c_case_death_recov_curve));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        // prettifying chart tile
        cartesian.title("Trends of Daily Cases, Deaths and Recovered of " + countryName);
        cartesian.title().fontOpacity(10);
        cartesian.title().fontStyle("bold");
        cartesian.title().fontColor("#000000");

        // prettifying yAxis tile
        cartesian.yAxis(0).title("Frequency (Tally)");
        cartesian.yAxis(0).title().fontOpacity(10);
        cartesian.yAxis(0).title().fontStyle("bold");
        cartesian.yAxis(0).title().fontColor("#000000");

        // prettifying yAxis labels
        cartesian.yAxis(0).labels().fontOpacity(10);
        cartesian.yAxis(0).labels().fontStyle("bold");
        cartesian.yAxis(0).labels().fontColor("#000000");

        // prettifying xAxis tile
        cartesian.xAxis(0).title("Date");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        cartesian.xAxis(0).title().fontOpacity(10);
        cartesian.xAxis(0).title().fontStyle("bold");
        cartesian.xAxis(0).title().fontColor("#000000");

        // prettifying xAxis labels
        cartesian.xAxis(0).labels().fontOpacity(10);
        cartesian.xAxis(0).labels().fontStyle("bold");
        cartesian.xAxis(0).labels().fontColor("#000000");

        List<DataEntry> seriesData = new ArrayList<>();

        // assigning data
        for (int i=0; i<selectedCountryData.size(); i++){

            String date = selectedCountryData.get(i).getDate();
            int newCases = Integer.parseInt(selectedCountryData.get(i).getNewCases());
            int newDeaths = Integer.parseInt(selectedCountryData.get(i).getNewDeaths());
            int newRecovered = Integer.parseInt(selectedCountryData.get(i).getNewRecovered());

            seriesData.add(new CustomDataEntry(date, newCases, newDeaths, newRecovered));
        }

        Set set = Set.instantiate();
        set.data(seriesData);

        Mapping series1CasesMapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2DeathsMapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3RecoveredMapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1CasesMapping);
        series1.name("New Infected Case");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        // setting color (YELLOW) for the line and corresponding Check box
        series1.stroke("#C68E17");

        Line series2 = cartesian.line(series2DeathsMapping);
        series2.name("Death");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        // setting color (RED) for the line and corresponding Check box
        series2.stroke("#C11B17");

        Line series3 = cartesian.line(series3RecoveredMapping);
        series3.name("Recovered");
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        // setting color (GREEN) for the line and corresponding Check box
        series3.stroke("#347C2C");

        // prettifying the legend texts New Infected Case, Death, Recovered
        cartesian.legend().enabled(true);
        cartesian.legend().fontColor("#000000");
        cartesian.legend().fontOpacity(10);
        cartesian.legend().fontStyle("bold");
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);

    }

    private void setLineChartTotalTestsCases(){

        // Line Chart using anyChart
        AnyChartView anyChartView = findViewById(R.id.any_chart_view_c_tests_cases_curve);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);   // very important line of code for multiple charts
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_c_tests_cases_curve));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        // prettifying chart tile
        cartesian.title("Infection/Positivity Rate - Line Trend " + countryName);
        cartesian.title().fontOpacity(10);
        cartesian.title().fontStyle("bold");
        cartesian.title().fontColor("#000000");

        // prettifying yAxis tile
        cartesian.yAxis(0).title("Infection Rate (%)");
        cartesian.yAxis(0).title().fontOpacity(10);
        cartesian.yAxis(0).title().fontStyle("bold");
        cartesian.yAxis(0).title().fontColor("#000000");

        // prettifying yAxis labels
        cartesian.yAxis(0).labels().fontOpacity(10);
        cartesian.yAxis(0).labels().fontStyle("bold");
        cartesian.yAxis(0).labels().fontColor("#000000");

        // prettifying xAxis tile
        cartesian.xAxis(0).title("Date");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        cartesian.xAxis(0).title().fontOpacity(10);
        cartesian.xAxis(0).title().fontStyle("bold");
        cartesian.xAxis(0).title().fontColor("#000000");

        // prettifying xAxis labels
        cartesian.xAxis(0).labels().fontOpacity(10);
        cartesian.xAxis(0).labels().fontStyle("bold");
        cartesian.xAxis(0).labels().fontColor("#000000");

        List<DataEntry> seriesData = new ArrayList<>();

        // assigning data
        for (int i=0; i<selectedCountryData.size(); i++){

            String date = selectedCountryData.get(i).getDate();
            int totalTests = Integer.parseInt(selectedCountryData.get(i).getTotalTests());
            int totalCases = Integer.parseInt(selectedCountryData.get(i).getTotalCases());

            seriesData.add(new CustomDataEntryTc(date, totalTests, totalCases));
        }

        Set set = Set.instantiate();
        set.data(seriesData);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Infection Rate");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        // setting color (YELLOW) for the line and corresponding Check box
        series1.stroke("#BAB86C");

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Daily Tests");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        // setting color (RED) for the line and corresponding Check box
        series2.stroke("#7B68EE");

        // prettifying the legend texts New Infected Case, Death, Recovered
        cartesian.legend().enabled(true);
        cartesian.legend().fontColor("#000000");
        cartesian.legend().fontOpacity(10);
        cartesian.legend().fontStyle("bold");
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);

    }

    private class CustomDataEntryTc extends ValueDataEntry{

        CustomDataEntryTc(String x, Number value, Number value2){
            super(x, value);
            setValue("value2", value2);
        }
    }

    private class CustomDataEntry extends ValueDataEntry{

        CustomDataEntry(String x, Number value, Number value2, Number value3){
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }
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