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

        getGlobalData();
        Log.d(TAG, "called global data method before default screen but waiting for response");
        if(globalData == null)
            Log.d(TAG, "onCreate: globalData is null, has not been instanciated");

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Log.d(TAG, "bottomNavListener connected: ");

        getAllCountryData();
        Log.d(TAG, "called all country data method but waiting for response");

        getCountryData();
        Log.d(TAG, "called bangladesh country data method but waiting for response");
    }

    public void getGlobalData(){
        Call<GlobalData> call = RetrofitClient.getInstance().getMyApi().getGlobalData();
        call.enqueue(new Callback<GlobalData>() {
            @Override
            public void onResponse(Call<GlobalData> call, Response<GlobalData> response) {
                globalData = response.body();

                //default Fragment will be loaded when there is a response on the call of globalData
                loadFragment(new GlobalFragment(globalData));

                Log.d("Beef","Active Cases: " + globalData.getActiveCases());
                Log.d("Beef","Total Cases: " + globalData.getTotalCases());
                Log.d("Beef","Total Deaths: " + globalData.getTotalDeaths());
                Log.d("Beef","Total Recovered: " + globalData.getTotalRecovered());
                Log.d("Beef","New Cases: " + globalData.getNewCases());
                Log.d("Beef","New Recovered: " + globalData.getNewRecovered());
            }

            @Override
            public void onFailure(Call<GlobalData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.d(TAG, "getGlobalData: return true place");
    }

    public void getAllCountryData(){
        Call <List<CountryData>> call = RetrofitClient.getInstance().getMyApi().getAllCountryData();
        call.enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                countryDataList = response.body();

                Log.d("Mutton", "CountryName = " + countryDataList.get(1).getCountryName());
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

                Log.d("Sheek", "NewCases = " + countryData.getNewCases());
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
        Log.d(TAG, "navitemSelected Method ");
        switch (item.getItemId()){
            case R.id.navigation_global:
                fragment = new GlobalFragment(globalData);  // passing GlobalData to GlobalFragment
                break;

            case R.id.navigation_country:
                Log.d(TAG, "country nav button clicked ");

                // passing countryDataList to CountryFragment
                fragment = new CountryFragment(MainActivity.this, countryDataList);     // using getApplicationContext() caused error when creating the detail screen  but MainActivity.this fixed it
                Log.d(TAG, "after using the countryFragment constructor ");
                break;

            case R.id.navigation_about:
                fragment = new AboutFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){
        Log.d(TAG, "inside loadFragment ");
        //switching fragments
        if(fragment != null){
            Log.d(TAG, "in fragment not null before method calls ");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navigation_container, fragment)
                    .commit();
            Log.d(TAG, "in fragment not null after method calls ");
            return true;
        }
        return false;
    }
}