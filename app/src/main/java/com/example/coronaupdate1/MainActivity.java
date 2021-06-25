package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.coronaupdate1.DataModel.CountryData;
import com.example.coronaupdate1.DataModel.CountryInfo;
import com.example.coronaupdate1.DataModel.GlobalData;
import com.example.coronaupdate1.api.RetrofitClient;
import com.example.coronaupdate1.fragments.AboutFragment;
import com.example.coronaupdate1.fragments.CountryFragment;
import com.example.coronaupdate1.fragments.GlobalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    private List<CountryData> countryDataList;
    private GlobalData globalData;
    private CountryData countryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        // called global data method before default screen but waiting for response
        getGlobalData();
        Log.d(TAG, "");

        // global data will be null as it hasn't been instanciated yet it is waiting for a response of the http call
        if(globalData == null)
            Log.d(TAG, "onCreate: globalData is null, has not been instanciated");

        // bottomNavListener connected
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // called all country data method but waiting for response
        getAllCountryData();

        // called bangladesh country data method but waiting for response
        getCountryData();

    }

    public void getGlobalData(){
        Call<GlobalData> call = RetrofitClient.getInstance().getMyApi().getGlobalData();
        call.enqueue(new Callback<GlobalData>() {
            @Override
            public void onResponse(Call<GlobalData> call, Response<GlobalData> response) {
                globalData = response.body();

                //default Fragment will be loaded when there is a response on the call of globalData
                loadFragment(new GlobalFragment(globalData));

                Log.d("GlobalData","Active Cases: " + globalData.getActiveCases());
                Log.d("GlobalData","Total Cases: " + globalData.getTotalCases());
                Log.d("GlobalData","Total Deaths: " + globalData.getTotalDeaths());
                Log.d("GlobalData","Total Recovered: " + globalData.getTotalRecovered());
                Log.d("GlobalData","New Cases: " + globalData.getNewCases());
                Log.d("GlobalData","New Recovered: " + globalData.getNewRecovered());
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
                countryDataList = response.body();

                Log.d("CountryDataList", "CountryName at index 1 = "
                        + countryDataList.get(1).getCountryName());
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
                countryData = response.body();

                Log.d("CountryDataBD ", "NewCases = " + countryData.getNewCases());
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
}