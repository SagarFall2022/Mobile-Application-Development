package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRVModel> weatherRVModelArrayList;
    private final MainActivity mainAct;


    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModel> weatherRVModelArrayList, MainActivity mainAct) {
        this.context = context;
        this.weatherRVModelArrayList = weatherRVModelArrayList;
        this.mainAct = mainAct;
    }



    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {
         WeatherRVModel model = weatherRVModelArrayList.get(position);
         holder.day.setText(model.getDay());
         holder.temperature.setText(model.getTemperature());
         holder.description.setText(model.getDescription());
         holder.time.setText(model.getTime());
         int iconID=mainAct.getResources().getIdentifier(model.getWeatherIcon(), "drawable",mainAct.getPackageName());
         holder.cardIcon.setImageResource(iconID);

       }

    @Override
    public int getItemCount() {
        return weatherRVModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView day,time,temperature,description;
        private ImageView cardIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day=itemView.findViewById(R.id.textView);
            time=itemView.findViewById(R.id.textView2);
            temperature=itemView.findViewById(R.id.textView3);
            description=itemView.findViewById(R.id.textView4);
            cardIcon=itemView.findViewById(R.id.imageView);


        }
    }
}
