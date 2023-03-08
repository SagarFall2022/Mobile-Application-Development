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

public class WeatherRVAdapterWeekly extends RecyclerView.Adapter<WeatherRVAdapterWeekly.ViewHolder> {


        private Context context;
        private ArrayList<WeatherRVModelWeekly> weatherRVModelArrayList;
        private final WeeklyWeather weeklyWeather;


        public WeatherRVAdapterWeekly(Context context, ArrayList<WeatherRVModelWeekly> weatherRVModelArrayList,WeeklyWeather weeklyWeather) {
            this.context = context;
            this.weatherRVModelArrayList = weatherRVModelArrayList;
            this.weeklyWeather=weeklyWeather;
        }



        @NonNull
        @Override
        public com.example.weatherapp.WeatherRVAdapterWeekly.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.daily_rv_item,parent,false);
            return new com.example.weatherapp.WeatherRVAdapterWeekly.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.weatherapp.WeatherRVAdapterWeekly.ViewHolder holder, int position) {
            WeatherRVModelWeekly model = weatherRVModelArrayList.get(position);
            holder.day.setText(model.getDay());
            holder.temperature.setText(model.getTemperature());
            holder.description.setText(model.getDescription());
            holder.precipitation.setText(model.getPrecipitation());
            holder.morning.setText(model.getMorning());
            holder.afternoon.setText(model.getAfternoon());
            holder.evening.setText(model.getEvening());
            holder.night.setText(model.getNight());
            holder.uv.setText(model.getUv());
            int iconID=weeklyWeather.getResources().getIdentifier(model.getWeatherIcon(), "drawable",weeklyWeather.getPackageName());
            holder.cardIcon.setImageResource(iconID);

        }

        @Override
        public int getItemCount() {
            return weatherRVModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView day,precipitation,temperature,description,morning,afternoon,evening,night,uv;
            private ImageView cardIcon;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                day=itemView.findViewById(R.id.textView5);
                precipitation=itemView.findViewById(R.id.textView8);
                temperature=itemView.findViewById(R.id.textView6);
                description=itemView.findViewById(R.id.textView7);
                cardIcon=itemView.findViewById(R.id.imageView4);
                uv=itemView.findViewById(R.id.textView9);
                morning=itemView.findViewById(R.id.textView10);
                afternoon=itemView.findViewById(R.id.textView11);
                evening=itemView.findViewById(R.id.textView12);
                night=itemView.findViewById(R.id.textView13);


            }
        }
    }


