package com.example.coronaupdate1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coronaupdate1.DataModel.CountryData;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomCountryAdapter extends RecyclerView.Adapter<CustomCountryAdapter.MyViewHolder> {

    private final Context context;
    private final List<CountryData> countryDataList;

    public CustomCountryAdapter(Context context, List<CountryData> countryDataList){
        this.countryDataList = countryDataList;
        this.context = context;
        Log.d("customAdapterActivity", "inside customCountryAdapter constructor");
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        // inflate the item Layout xml
        View view = LayoutInflater.from(context).inflate(R.layout.country_item_row_layout,
                parent, false);
        Log.d("customAdapterActivity", "inside on_createViewHolder after assigning layoutInflater to view");
        // set the view's size, margins, paddings and layout parameters
        Log.d("customAdapterActivity", "inside on_createViewHolder before calling MyviewHolder constructor");
        MyViewHolder myViewHolder = new MyViewHolder(view); // pass the view to View Holder
        Log.d("customAdapterActivity", "inside on_createViewHolder after calling MyviewHolder constructor");
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, final int position) {
        Log.d("customAdapterActivity", "inside onBindViewHolder before setting data");
        // set the data
        holder.countryName.setText(countryDataList.get(position).getCountryName());
        Picasso.with(context).load(countryDataList.get(position).getCountryInfo().getFlag()).into(holder.countryFlagImage);
        holder.dailyNewCases.setText("+" + Integer.toString(countryDataList.get(position).getNewCases()));
        holder.dailyNewDeaths.setText("+" + Integer.toString(countryDataList.get(position).getNewDeaths()));
        Log.d("customAdapterActivity", "inside onBindViewHolder after setting data");
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", ""+ countryDataList.get(position).getCountryName() );

                // display a toast with person name on item click
                Toast.makeText(context, countryDataList.get(position).getCountryName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("customAdapterActivity", "inside getItemCount");
        if(countryDataList == null) {
            Log.d("getItemCount: NullCheck", "NULL List");
            return 0;
        }
        Log.d("getItemCount:EmptyCheck", " " + countryDataList.isEmpty());
        return countryDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        // init the item view's
        TextView countryName;
        ImageView countryFlagImage;
        TextView dailyNewCases;
        TextView dailyNewDeaths;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            countryName = (TextView) itemView.findViewById(R.id.country_name);
            countryFlagImage = (ImageView) itemView.findViewById(R.id.country_flag_image);
            dailyNewCases = (TextView) itemView.findViewById(R.id.daily_new_cases);
            dailyNewDeaths = (TextView) itemView.findViewById(R.id.daily_new_deaths);
        }
    }
}
