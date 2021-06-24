package com.example.coronaupdate1.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coronaupdate1.DataModel.GlobalData;
import com.example.coronaupdate1.R;

import org.jetbrains.annotations.NotNull;

public class GlobalFragment extends Fragment {

    private static final String TAG = "Global Fragment";
    private GlobalData globalData;

    public GlobalFragment(GlobalData globalData){
        this.globalData = globalData;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_global, null);

        // to make the options (graph button) appear in your Toolbar
        setHasOptionsMenu(true);
        
        TextView globalActiveCasesView = view.findViewById(R.id.global_active_cases);
        globalActiveCasesView.setText(Integer.toString(globalData.getActiveCases()));


        TextView globalTotalCasesView = view.findViewById(R.id.global_total_cases);
        globalActiveCasesView.setText(Integer.toString(globalData.getActiveCases()));


        TextView globalTotalDeathsView = view.findViewById(R.id.global_total_deaths);
        globalActiveCasesView.setText(Integer.toString(globalData.getActiveCases()));


        TextView globalTotalRecooveredView = view.findViewById(R.id.global_total_recovered);
        globalActiveCasesView.setText(Integer.toString(globalData.getActiveCases()));


        TextView globalDailyNewCasesView = view.findViewById(R.id.global_daily_new_cases);
        globalActiveCasesView.setText(Integer.toString(globalData.getActiveCases()));


        TextView globalDailyNewDeathsView = view.findViewById(R.id.global_daily_new_deaths);
        globalActiveCasesView.setText(Integer.toString(globalData.getActiveCases()));


        TextView globalDailyNewRecoveredView = view.findViewById(R.id.global_daily_new_recovered);
        globalActiveCasesView.setText(Integer.toString(globalData.getActiveCases()));
        return view;
    }

    // create action bar button
    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_global_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // handle action bar button activities
    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.graph_button_global){
            Log.d(TAG, "onOptionsItemSelected: Graph button clicked");
            Toast.makeText(getContext(), "Graph Selected" , Toast.LENGTH_SHORT).show();

            return  true;
        }

        return super.onOptionsItemSelected(item);
    }
}
