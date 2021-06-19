package com.example.coronaupdate1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

    private List<CountryData> countryDataList;
    private GlobalData globalData;
    private CountryData countryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //default Fragment
        loadFragment(new GlobalFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getGlobalData();

        getAllCountryData();

        getCountryData();

    }

    public void getGlobalData(){
        Call<GlobalData> call = RetrofitClient.getInstance().getMyApi().getGlobalData();
        call.enqueue(new Callback<GlobalData>() {
            @Override
            public void onResponse(Call<GlobalData> call, Response<GlobalData> response) {
                globalData = response.body();

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
    }

    public void getAllCountryData(){
        Call <List<CountryData>> call = RetrofitClient.getInstance().getMyApi().getAllCountryData();
        call.enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                countryDataList = response.body();

                Log.d("Mutton", "CountryName = " + countryDataList.get(1).getCountryName());
                /*for (int i=0; i<countryDataList.size(); i++){
                    Log.i("Mutton", "CountryName = " + countryDataList.get(i).getCountryName());
                }*/
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

                Log.i("Sheek", "NewCases = " + countryData.getNewCases());
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

        switch (item.getItemId()){
            case R.id.navigation_global:
                fragment = new GlobalFragment();
                break;

            case R.id.navigation_country:
                fragment = new CountryFragment(getApplicationContext(), countryDataList);
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