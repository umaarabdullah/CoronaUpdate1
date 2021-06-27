package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.coronaupdate1.DataModel.DbGlobalData;
import com.example.coronaupdate1.utility.StringNumber;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphGlobalCases extends AppCompatActivity {

    private static final String TAG = "GraphGlobalCases";
    private int totalCases;
    private int activeCases;
    private int totalDeaths;
    private int totalRecovered;
    private String totalCasesFormatted;
    private String formattedDate;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("GlobalData");
    private List<DbGlobalData> dbGlobalDataList = new ArrayList<DbGlobalData>();    // data by dates

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_global_cases);

        // setting the title in the actionBar
        getSupportActionBar().setTitle("Graphs");

        // enabling the up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // checking if the appropriate intent extras were received properly or not
        if(getIntent().hasExtra("total_cases") && getIntent().hasExtra("active_cases")
        && getIntent().hasExtra("total_deaths") && getIntent().hasExtra("total_recovered")){

            // getting the data which was passed from the previous activity
            totalCases = Integer.parseInt(getIntent().getStringExtra("total_cases"));
            activeCases = Integer.parseInt(getIntent().getStringExtra("active_cases"));
            totalDeaths = Integer.parseInt(getIntent().getStringExtra("total_deaths"));
            totalRecovered = Integer.parseInt(getIntent().getStringExtra("total_recovered"));

            // formatting (giving commas)
            StringNumber stringNumber = new StringNumber();
            totalCasesFormatted = stringNumber.bigNumberFormatting(Integer.toString(totalCases));

            // getting the current date
            // date is formatted as Ex : 28-Dec-2020
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            formattedDate = simpleDateFormat.format(date);

        }
        // set the pie chart
        setPieChart(formattedDate, totalCasesFormatted);
        Log.d(TAG, "onDataChange: set pie chart called");

        // retrieve data from firebase
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                // iterating through dates and adding the data to the List
                for (DataSnapshot dateDataSnapShot : snapshot.getChildren()){
                    DbGlobalData dbGlobalData = dateDataSnapShot.getValue(DbGlobalData.class);

                    dbGlobalDataList.add(dbGlobalData);
                }
                Log.d(TAG, "onDataChange: data retrieved succesfully");

                // drawing the column chart after data had been retrieved
                //setNewCasesColumnChart();
                Log.d(TAG, "onDataChange: column chart created");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });
    }

    private void setPieChart(String formattedDate, String totalCasesFormatted){

        // pie chart using anyChart library
        AnyChartView anyChartView = findViewById(R.id.any_chart_view_new_cases_global);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar_new_cases_global));

        Pie pie = AnyChart.pie();

        // setting a click listener to trigger a toast when the pie chart is clicked
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(GraphGlobalCases.this, event.getData().get("x") + ":" +
                        event.getData().get("value"), Toast.LENGTH_LONG).show();
            }
        });

        List<DataEntry> globalCasesData = new ArrayList<>();
        globalCasesData.add(new ValueDataEntry("Active Case", activeCases));
        globalCasesData.add(new ValueDataEntry("Death", totalDeaths));
        globalCasesData.add(new ValueDataEntry("Recovered", totalRecovered));

        pie.data(globalCasesData);

        pie.title("Distribution of Total Cases : " + totalCasesFormatted +  " Date-" + formattedDate);
        pie.title().fontColor("#000000");
        pie.title().fontOpacity(10);
        pie.title().fontStyle("bold");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Possible Outcomes")
                .padding(0d, 0d, 10d, 0d);
        pie.legend().title().fontStyle("bold");
        pie.legend().title().fontColor("#000000");

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);

    }

    private void setNewCasesColumnChart(){

        // Column Chart using anyChart
        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view_new_cases_global);
        anyChartView1.setProgressBar(findViewById(R.id.progress_bar_new_cases_global));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> casesData = new ArrayList<>();

        // X and Y axis data assigned
        // column chart usually fails to render if cases are below 20 (assumption)
        for (int i=0; i<dbGlobalDataList.size(); i++){

            int cases = Integer.parseInt(dbGlobalDataList.get(i).getNewCases());

            if(cases > 20){
                casesData.add(new ValueDataEntry(dbGlobalDataList.get(i).getDate(), cases));
            }
        }

        Column column = cartesian.column(casesData);
        column.color("#01294b");    // setting column bar color

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Daily New Cases - " + "Global");
        cartesian.title().fontStyle("bold");

        cartesian.yScale().minimum(0d);

        cartesian.xAxis(0).labels().fontSize(10);
        cartesian.xAxis(0).labels().fontStyle("bold");
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Dates");
        cartesian.yAxis(0).title("Cases");

        anyChartView1.setChart(cartesian);
    }

    // setting the up button to do the same as the back button
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