package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.coronaupdate1.DataModel.CountryData;
import com.example.coronaupdate1.DataModel.DbCountryData;
import com.example.coronaupdate1.DataModel.DbCountryDataInfection;
import com.example.coronaupdate1.DataModel.DbGlobalData;
import com.example.coronaupdate1.DataModel.GlobalData;
import com.example.coronaupdate1.api.RetrofitClient;
import com.example.coronaupdate1.fragments.AboutFragment;
import com.example.coronaupdate1.fragments.CountryFragment;
import com.example.coronaupdate1.fragments.GlobalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    private List<CountryData> countryDataList;
    private GlobalData globalData;
    private CountryData countryData;
    private String formattedDate;
    private String yesterdayDate;
    private String localTime;
    private final String newDayStartingTime = "07:00";      // country api response update at around 7AM (Aprox) (+6 GMT). New data is written to the database as new date

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRootRef1 = FirebaseDatabase.getInstance().getReference("CountryData");   // reference to the CountryData branch used in setting infection data
    DatabaseReference mRootRef2 = FirebaseDatabase.getInstance().getReference();    // another reference to the root to be used for setting infection data


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");


        // getting the current date
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = simpleDateFormat.format(date);
        Log.d(TAG, "onCreate: today calendar date : " + formattedDate );

        // getting date of yesterday
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        yesterdayDate = simpleDateFormat.format(calendar.getTime());
        Log.d(TAG, "onCreate: yesterday calendar date : " +   yesterdayDate);

        // getting time
        DateFormat time = new SimpleDateFormat("HH:mm");
        localTime = time.format(date);
        Log.d(TAG, "onCreate: today calendar local time : " + localTime + " newDayStartingTime : " + newDayStartingTime );


        // called global data method before default screen but waiting for response
        getGlobalData();
        Log.d(TAG, "");

        // global data will be null as it hasn't been instantiated yet it is waiting for a response of the http call
        if(globalData == null)
            Log.d(TAG, "onCreate: globalData is null, has not been instantiated");

        // bottomNavListener connected
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // called all country data method but waiting for response
        getAllCountryData();

        // called bangladesh country data method but waiting for response
        getCountryData();

        // setting infection data, calling call to set the event Listener
        setInfectionRateData();
    }

    public void getGlobalData(){
        Call<GlobalData> call = RetrofitClient.getInstance().getMyApi().getGlobalData();
        call.enqueue(new Callback<GlobalData>() {
            @Override
            public void onResponse(Call<GlobalData> call, Response<GlobalData> response) {

                // records the data received from http call response as plain old java objects (POJO)
                globalData = response.body();

                //default Fragment will be loaded when there is a response on the call of globalData
                loadFragment(new GlobalFragment(globalData));

                Log.d("GlobalData","Active Cases: " + globalData.getActiveCases());
                Log.d("GlobalData","Total Cases: " + globalData.getTotalCases());
                Log.d("GlobalData","Total Deaths: " + globalData.getTotalDeaths());
                Log.d("GlobalData","Total Recovered: " + globalData.getTotalRecovered());
                Log.d("GlobalData","New Cases: " + globalData.getNewCases());
                Log.d("GlobalData","New Recovered: " + globalData.getNewRecovered());

                // parsing data that is to be written on fireBase realtime database
                // need to check if indeed it today date's data as the timezone is +6, we do this by comparing time that is the current less than 5:50 Am.
                // negative means localTime is smaller it means actual date is yesterday, 0 means equal, positive means localtime is greater and actual date is today
                if(localTime.compareTo(newDayStartingTime) < 0){
                    DbGlobalData dbGlobalData = new DbGlobalData(Integer.toString(globalData.getNewCases()), yesterdayDate);

                    Log.d(TAG, "getGlobalData onResponse: is today's data actually today's data ? false");
                    // write to the database
                    setFireBaseDbGlobalData(dbGlobalData, false);
                }
                else{
                    DbGlobalData dbGlobalData = new DbGlobalData(Integer.toString(globalData.getNewCases()), formattedDate);

                    Log.d(TAG, "getGlobalData onResponse: is today's data actually today's data ? true");
                    // write to the database
                    setFireBaseDbGlobalData(dbGlobalData, true);
                }


            }

            @Override
            public void onFailure(Call<GlobalData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllCountryData(){
        Call <List<CountryData>> call = RetrofitClient.getInstance().getMyApi().getAllCountryData();
        call.enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {

                // records the data received from http call response as plain old java objects (POJO)
                countryDataList = response.body();

                Toast.makeText(getApplicationContext(), "Country List Ready", Toast.LENGTH_SHORT).show();

                Log.d(TAG, "getCountryData onResponse : CountryName at index 1 = " + countryDataList.get(1).getCountryName());
                Log.d(TAG, "getCountryData onResponse : CountryDataList size : " + countryDataList.size());

                // parsing data which are to be written on the firebase realtime database
                // need to check if indeed it today date's data as the timezone is +6, we do this by comparing time that is the current less than 5:50 Am.
                // negative means localTime is smaller it means actual date is yesterday, 0 means equal, positive means localtime is greater and actual date is today
                Log.d(TAG, "getCountryData onResponse : localTime Compare to NewStartdayTime : " + localTime.compareTo(newDayStartingTime));
                if(localTime.compareTo(newDayStartingTime) < 0) {
                    for (int i = 0; i < countryDataList.size(); i++) {

                        // calling DbCountryData constructor
                        DbCountryData dbCountryData =
                                new DbCountryData(countryDataList.get(i).getCountryName(),
                                        Integer.toString(countryDataList.get(i).getActiveCases()),
                                        Integer.toString(countryDataList.get(i).getTotalCases()),
                                        Integer.toString(countryDataList.get(i).getNewCases()),
                                        Integer.toString(countryDataList.get(i).getTotalDeaths()),
                                        Integer.toString(countryDataList.get(i).getNewDeaths()),
                                        Integer.toString(countryDataList.get(i).getTotalRecovered()),
                                        Integer.toString(countryDataList.get(i).getNewRecovered()),
                                        Integer.toString(countryDataList.get(i).getTotalTests()),
                                        yesterdayDate);

                        // write to the database
                        setFireBaseDbCountryData(dbCountryData, false);

                    }
                }
                else {
                    for (int i = 0; i < countryDataList.size(); i++) {

                        // calling DbCountryData constructor
                        DbCountryData dbCountryData =
                                new DbCountryData(countryDataList.get(i).getCountryName(),
                                        Integer.toString(countryDataList.get(i).getActiveCases()),
                                        Integer.toString(countryDataList.get(i).getTotalCases()),
                                        Integer.toString(countryDataList.get(i).getNewCases()),
                                        Integer.toString(countryDataList.get(i).getTotalDeaths()),
                                        Integer.toString(countryDataList.get(i).getNewDeaths()),
                                        Integer.toString(countryDataList.get(i).getTotalRecovered()),
                                        Integer.toString(countryDataList.get(i).getNewRecovered()),
                                        Integer.toString(countryDataList.get(i).getTotalTests()),
                                        formattedDate);

                        // write to the database
                        setFireBaseDbCountryData(dbCountryData, true);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCountryData(){
        Call<CountryData> call = RetrofitClient.getInstance().getMyApi().getCountryData("bangladesh");
        call.enqueue(new Callback<CountryData>() {
            @Override
            public void onResponse(Call<CountryData> call, Response<CountryData> response) {

                // records the data received from http call response as plain old java objects (POJO)
                countryData = response.body();

                Log.d("CountryDataQuery ", "NewCases = " + countryData.getNewCases());
            }

            @Override
            public void onFailure(Call<CountryData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        Fragment fragment;

        // when a bottom navigation button is clicked this method is triggered
        // and ascertains which button was click by using reference
        switch (item.getItemId()){
            case R.id.navigation_global:
                Toast.makeText(this, "Global" , Toast.LENGTH_SHORT).show();
                fragment = new GlobalFragment(globalData);  // passing GlobalData to GlobalFragment
                break;

            case R.id.navigation_country:
                // wait until the api call responds and countryDataList is populated
                if(countryDataList == null) {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Toast.makeText(this, "Country" , Toast.LENGTH_SHORT).show();

                // passing countryDataList to CountryFragment
                fragment = new CountryFragment(MainActivity.this, countryDataList);     // using getApplicationContext() caused error when creating the detail screen  but MainActivity.this fixed it
                break;

            case R.id.navigation_about:
                Toast.makeText(this, "About" , Toast.LENGTH_SHORT).show();
                fragment = new AboutFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){

        //switching fragments
        if(fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navigation_container, fragment)
                    .commit();

            return true;
        }
        return false;
    }

    // writing global data on firebase realtime database
    private void setFireBaseDbGlobalData(DbGlobalData dbGlobalData, boolean flag){

        // flag identifies if it is a new date of not. True means new date data and false means yesterday date data
        if(flag){
            mRootRef.child("GlobalData").child(formattedDate).setValue(dbGlobalData);
        }
        else {
            mRootRef.child("GlobalData").child(yesterdayDate).setValue(dbGlobalData);
        }
    }

    // writing country data on firebase realtime database
    private void setFireBaseDbCountryData(DbCountryData dbCountryData, boolean flag){
        // flag identifies if it is a new date of not. True means new date data and false means yesterday date data
        if(flag){
            // as there cannot be '.' in the firebase path
            if(dbCountryData.getCountryName().equals("S. Korea")){
                dbCountryData.setCountryName("South Korea");
                Log.d(TAG, "setFireBaseDbCountryData: south korea");
            }
            else if(dbCountryData.getCountryName().equals("St. Barth")){
                dbCountryData.setCountryName("Saint Barth");
                Log.d(TAG, "setFireBaseDbCountryData: saint barth");
            }

            mRootRef.child("CountryData").child(dbCountryData.getCountryName()).child(formattedDate).setValue(dbCountryData);
        }
        else {
            // as there cannot be '.' in the firebase path
            if(dbCountryData.getCountryName().equals("S. Korea")){
                dbCountryData.setCountryName("South Korea");
                Log.d(TAG, "setFireBaseDbCountryData: south korea");
            }
            else if(dbCountryData.getCountryName().equals("St. Barth")){
                dbCountryData.setCountryName("Saint Barth");
                Log.d(TAG, "setFireBaseDbCountryData: saint barth");
            }

            mRootRef.child("CountryData").child(dbCountryData.getCountryName()).child(yesterdayDate).setValue(dbCountryData);
        }
    }

    private void setInfectionRateData(){

        // retrieving/reading data
        mRootRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.d(TAG, "setInfectionData onDataChange: ");

                // iterating through all the dates and adding the data and calculating infection rate and adding it to the database
                for (DataSnapshot countryDataSnapshot : snapshot.getChildren()){
                    Log.d(TAG, "setInfectionData onDataChange: Country - Name key : " + countryDataSnapshot.getKey());

                    // declaring instance objects
                    DbCountryData yesterdayDbData = null;
                    DbCountryData dbCountryData = null;
                    boolean firstDataFetched = false;

                    for (DataSnapshot dateDataSnapshot : countryDataSnapshot.getChildren()){
                        //Log.d(TAG, "setInfectionData onDataChange: Date - inside key : " + dateDataSnapshot.getKey());
                        // storing yesterday data and finding infection rate
                        if(firstDataFetched) {
                            yesterdayDbData = dbCountryData;
                        }

                        // fetching data
                        dbCountryData = dateDataSnapshot.getValue(DbCountryData.class);
                        //Log.d(TAG, "onDataChange: infection rate db Country data date : " + dbCountryData.getDate());
                        firstDataFetched = true;

                        // after getting the new dbCountryData we compare with yesterdayDbData and calculate the infection rate by finding the number of new tests
                        if(yesterdayDbData != null){

                            DbCountryDataInfection dbCountryDataInfection;
                            String todayDate = dbCountryData.getDate();

                            // finding infection rate
                            // will have to type cast the int variables later
                            int newTests = Integer.parseInt(dbCountryData.getTotalTests()) - Integer.parseInt(yesterdayDbData.getTotalTests());
                            int newCases = Integer.parseInt(dbCountryData.getNewCases());
                            double infectionRate = 0.00;

                            // check for division by 0
                            if(newTests != 0) {
                                infectionRate = ((double) newCases / newTests) * 100;   // ***** TYPE CAST TO DOUBLE ******
                                Log.d(TAG, "onDataChange: inf rate : " + infectionRate + " " + countryDataSnapshot.getKey());
                            }
                            Log.d(TAG, "setInfectionData onDataChange: newTests : " + newTests + " newCases : " + newCases
                                    + " infectionRate : " + String.format("%.2f", infectionRate) + " " + countryDataSnapshot.getKey());

                            // constructing a dbCountryDataInfection object
                            dbCountryDataInfection = new DbCountryDataInfection(String.format("%.2f", infectionRate) , todayDate);

                            // writing data to the branch CountryDataInfection
                            mRootRef2.child("CountryDataInfection").child(dbCountryData.getCountryName())
                                    .child(todayDate).setValue(dbCountryDataInfection);
                        }
                    }

                }

                Log.d(TAG, "onDataChange: country infectionRate data write successful");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.menu_refresh_global){
            finish();
            startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);
    }
}