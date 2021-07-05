package com.example.coronaupdate1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coronaupdate1.GoogleSignInActivity;
import com.example.coronaupdate1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AboutFragment extends Fragment {

    private FirebaseAuth firebaseAuth;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, null);

        firebaseAuth = FirebaseAuth.getInstance();

        Button logOutButton = view.findViewById(R.id.log_out_button);
        logOutButton.setText(firebaseAuth.getCurrentUser().getEmail() + "\nLOGOUT");

        // setting listener to the logout button
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(getContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                startLogInPage();
            }
        });

        return view;
    }

    private void startLogInPage(){

        startActivity(new Intent(getContext(), GoogleSignInActivity.class));
    }

}
