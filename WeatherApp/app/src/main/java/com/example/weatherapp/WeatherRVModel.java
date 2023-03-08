package com.example.weatherapp;

public class WeatherRVModel {

    private String Day;
    private String Time;
    private String Temperature;
    private String Description;
    private String WeatherIcon;

    public WeatherRVModel(String day, String time, String temperature, String description, String weatherIcon) {
        this.Day = day;
        this.Time = time;
        this.Temperature = temperature;
        this.Description = description;
        this.WeatherIcon = weatherIcon;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        this.Day = day;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        this.Temperature = temperature;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getWeatherIcon() {
        return WeatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.WeatherIcon = weatherIcon;
    }
}
