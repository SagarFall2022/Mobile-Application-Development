package com.example.weatherapp;

public class WeatherRVModelWeekly {
    private String Day;
    private String precipitation;
    private String Temperature;
    private String Description;
    private String WeatherIcon;
    private String Morning;
    private String Afternoon;
    private String Evening;
    private String Night;
    private String Uv;

    public WeatherRVModelWeekly(String day, String precipitation, String temperature, String description,String uv, String morning,String afternoon,String evening,String night,String weatherIcon) {
        this.Day = day;
        this.precipitation = precipitation;
        this.Temperature = temperature;
        this.Description = description;
        this.WeatherIcon = weatherIcon;
        this.Morning=morning;
        this.Afternoon=afternoon;
        this.Evening=evening;
        this.Night=night;
        this.Uv=uv;
    }

    public String getUv() {
        return Uv;
    }

    public void setUv(String uv) {
        Uv = uv;
    }

    public String getMorning() {
        return Morning;
    }

    public void setMorning(String morning) {
        Morning = morning;
    }

    public String getAfternoon() {
        return Afternoon;
    }

    public void setAfternoon(String afternoon) {
        Afternoon = afternoon;
    }

    public String getEvening() {
        return Evening;
    }

    public void setEvening(String evening) {
        Evening = evening;
    }

    public String getNight() {
        return Night;
    }

    public void setNight(String night) {
        Night = night;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        this.Day = day;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
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
