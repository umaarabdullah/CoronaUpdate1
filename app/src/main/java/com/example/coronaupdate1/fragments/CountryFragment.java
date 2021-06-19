package com.example.coronaupdate1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaupdate1.CustomCountryAdapter;
import com.example.coronaupdate1.DataModel.CountryData;
import com.example.coronaupdate1.R;

import java.util.List;

public class CountryFragment extends Fragment {
    private final Context context;
    private final List<CountryData> countryDataList;

    public CountryFragment(Context context, List<CountryData> countryDataList){
        this.context = context;
        this.countryDataList = countryDataList;
        Log.d("countryfragmentActivity", "inside countryFragment constructor ");
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_country, null);
        Log.d("countryfragmentActivity", "after inflater assigned to view");
        // get the reference of the recyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Log.d("countryfragmentActivity", "recyclerView assigned");

        // set linear layout manager for vertical orientation
        Log.d("countryfragmentActivity", "before calling linear layout manager constructor");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        Log.d("countryfragmentActivity", "before calling linear layout manager constructor");
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.d("countryfragmentActivity", "after setting layout manager of recylerview");

        // call the constructor of adapter class to send reference and data to adapter
        // context was passed from main activity via constructor
        Log.d("countryfragmentActivity", "before calling the customCountryAdapter constructor");
        CustomCountryAdapter customCountryAdapter = new CustomCountryAdapter(context, countryDataList);
        Log.d("countryfragmentActivity", "after calling the customCountryAdapter constructor");
        recyclerView.setAdapter(customCountryAdapter); // set the Adapter to RecyclerView
        Log.d("countryfragmentActivity", "after setting customCOuntryAdapter to recyclerView");
        return view;
    }
}
