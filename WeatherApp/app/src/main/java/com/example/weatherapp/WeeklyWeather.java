package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class WeeklyWeather extends AppCompatActivity {



    private WeatherRVAdapterWeekly weatherRVAdapterWeekly;
    private RecyclerView recyclerView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_weather);
        recyclerView2= findViewById(R.id.dailyRecycler);
        Intent intent=getIntent();
        String place= intent.getStringExtra("place");
        getSupportActionBar().setTitle(place+" 15 Day");
        weatherRVAdapterWeekly=new WeatherRVAdapterWeekly(this,MainActivity.weatherRVModelWeeklyArrayList,this);
        recyclerView2.setAdapter(weatherRVAdapterWeekly);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(layoutManager);
        weatherRVAdapterWeekly.notifyDataSetChanged();

    }
}