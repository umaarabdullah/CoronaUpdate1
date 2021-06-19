package com.example.coronaupdate1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coronaupdate1.DataModel.GlobalData;
import com.example.coronaupdate1.R;

public class GlobalFragment extends Fragment {

    private GlobalData globalData;

    public GlobalFragment(GlobalData globalData){
        this.globalData = globalData;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_global, null);
        return view;
    }
}
