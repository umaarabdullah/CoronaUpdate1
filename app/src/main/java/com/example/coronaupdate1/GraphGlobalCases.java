package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.coronaupdate1.utility.StringNumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GraphGlobalCases extends AppCompatActivity {

    private int totalCases;
    private int activeCases;
    private int totalDeaths;
    private int totalRecovered;

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

            StringNumber stringNumber = new StringNumber();
            String totalCasesFormatted = stringNumber.bigNumberFormatting(Integer.toString(totalCases));

            // getting the current date
            // date is formatted as Ex : 28-Dec-2020
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = simpleDateFormat.format(date);


            // pie chart using anyChart library
            AnyChartView anyChartView = findViewById(R.id.any_chart_view_global);
            anyChartView.setProgressBar(findViewById(R.id.progress_bar_global));

            Pie pie = AnyChart.pie();

            // setting a click listener to trigger a toast when the pie chart is clicked
            pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
                @Override
                public void onClick(Event event) {
                    Toast.makeText(GraphGlobalCases.this, event.getData().get("x") + ":" +
                            event.getData().get("value"), Toast.LENGTH_SHORT).show();
                }
            });

            List<DataEntry> globalCasesData = new ArrayList<>();
            globalCasesData.add(new ValueDataEntry("Active Case", activeCases));
            globalCasesData.add(new ValueDataEntry("Death", totalDeaths));
            globalCasesData.add(new ValueDataEntry("Recovered", totalRecovered));

            pie.data(globalCasesData);

            pie.title("Distribution of Total Cases : " + totalCasesFormatted +  " Date-" + formattedDate);

            pie.labels().position("outside");

            pie.legend().title().enabled(true);
            pie.legend().title()
                    .text("Possible Outcomes")
                    .padding(0d, 0d, 10d, 0d);

            pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER);

            anyChartView.setChart(pie);

        }

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