package com.example.coronaupdate1;

import android.content.Context;
import android.content.Intent;
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

    private static final String TAG = "CustomAdapter";
    private final Context context;
    private final List<CountryData> countryDataList;

    public CustomCountryAdapter(Context context, List<CountryData> countryDataList){
        this.countryDataList = countryDataList;
        this.context = context;
        Log.d(TAG, "inside customCountryAdapter constructor");
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        // inflate the item Layout xml
        View view = LayoutInflater.from(context).inflate(R.layout.country_item_row_layout,
                parent, false);
        Log.d(TAG, "inside on_createViewHolder after assigning layoutInflater to view");
        // set the view's size, margins, paddings and layout parameters
        Log.d(TAG, "inside on_createViewHolder before calling MyviewHolder constructor");
        MyViewHolder myViewHolder = new MyViewHolder(view); // pass the view to View Holder
        Log.d(TAG, "inside on_createViewHolder after calling MyviewHolder constructor");
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
        Log.d(TAG, "inside onBindViewHolder after setting data");
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ""+ countryDataList.get(position).getCountryName() );

                // display a toast with person name on item click
                Toast.makeText(context, countryDataList.get(position).getCountryName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, CountryDetailActivity.class);
                intent.putExtra("country_name", countryDataList.get(position).getCountryName());
                intent.putExtra("flag_image", countryDataList.get(position).getCountryInfo().getFlag());
                intent.putExtra("active_cases", Integer.toString(countryDataList.get(position).getActiveCases()));
                intent.putExtra("total_cases", Integer.toString(countryDataList.get(position).getTotalCases()));
                intent.putExtra("new_cases", Integer.toString(countryDataList.get(position).getNewCases()));
                intent.putExtra("total_deaths", Integer.toString(countryDataList.get(position).getTotalDeaths()));
                intent.putExtra("new_deaths", Integer.toString(countryDataList.get(position).getNewDeaths()));
                intent.putExtra("total_recovered", Integer.toString(countryDataList.get(position).getTotalRecovered()));
                intent.putExtra("new_recovered", Integer.toString(countryDataList.get(position).getNewRecovered()));
                intent.putExtra("total_tests", Integer.toString(countryDataList.get(position).getTotalTests()));
                Log.d(TAG, "onClick: before startActivity");
                context.startActivity(intent);
                Log.d(TAG, "onClick: after Start Activity");
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "inside getItemCount");
        if(countryDataList == null) {
            Log.d("getItemCount: NullCheck", "NULL List");
            return 0;
        }
        Log.d(TAG, " " + countryDataList.isEmpty());
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
