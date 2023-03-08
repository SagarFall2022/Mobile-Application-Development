package com.example.weatherapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline";
    private static final String myAPIKey = "KZSM9G7BBLBAY9VM8YWKHJ3KN";
    private boolean isF=true;
    public String  placeNew;
    private static final String PREFS_NAME="MyPrefs";
    private static final String PREF_LOCATION = "location";
    private static final String PREF_UNITS = "units";

    private RequestQueue queue;
    private TextView datetime;
    private TextView temperature;
    private TextView feelsLike;
    private TextView weatherDisc;
    private TextView wind;
    private TextView humidity;
    private TextView uv;
    private TextView visibility;
    private TextView morning;
    private TextView evening;
    private TextView afternoon;
    private TextView night;
    private TextView sunrise;
    private TextView sunset;
    private ImageView icon;
    private Menu optionsMenu;
    private String place;
    private ArrayList<WeatherRVModel> weatherRVModelArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private RecyclerView recyclerView1;
    private SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences prefs;

    public static ArrayList<WeatherRVModelWeekly> weatherRVModelWeeklyArrayList;
    private WeatherRVAdapterWeekly weatherRVAdapterWeekly;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datetime = findViewById(R.id.datetime);
        temperature = findViewById(R.id.Temp);
        feelsLike = findViewById(R.id.feelslike);
        weatherDisc = findViewById(R.id.descripAndcloud);
        wind = findViewById(R.id.winds);
        humidity = findViewById(R.id.humidity);
        uv = findViewById(R.id.ultraViolet);
        visibility = findViewById(R.id.visibility);
        morning = findViewById(R.id.morning);
        afternoon = findViewById(R.id.afternoon);
        evening = findViewById(R.id.evening);
        night = findViewById(R.id.night);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        icon = findViewById(R.id.weatherIcon);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);
        prefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        //Shared PReferences Extra Credit implemented
        placeNew = prefs.getString(PREF_LOCATION,"Chicago,IL");
        isF=prefs.getBoolean(PREF_UNITS,true);

        queue = Volley.newRequestQueue(this);

        if(!hasNetworkConnection()){
            Toast.makeText(getApplicationContext(),"No network connection",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.nointernet);

        }
        else {
            makeWeatherRequest(placeNew);

        }

        //Swipe Refresh Implemented (Extra credit)
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!hasNetworkConnection()){
                    Toast.makeText(getApplicationContext(),"No network connection",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
                else{
                    makeWeatherRequest(place);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });



    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }


    private void makeWeatherRequest(String placeNew){
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
        buildURL.appendPath(placeNew);
        buildURL.appendQueryParameter("key", myAPIKey);
        buildURL.appendQueryParameter("unitGroup",isF ? "us":"metric");

        String urlToUSe = buildURL.build().toString();
        weatherRVModelArrayList= new ArrayList<>();
        weatherRVModelWeeklyArrayList= new ArrayList<>();
        weatherRVAdapter= new WeatherRVAdapter(this,weatherRVModelArrayList,this);
        WeeklyWeather WeeklyWeather=new WeeklyWeather();
        weatherRVAdapterWeekly=new WeatherRVAdapterWeekly(this,weatherRVModelWeeklyArrayList,WeeklyWeather);
        recyclerView1= findViewById(R.id.recyclerView);
        recyclerView1.setAdapter(weatherRVAdapter);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager);

        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE",response.toString());
                        try{
                            place= response.getString("address");
                            getSupportActionBar().setTitle(place);

                            setDateTime(response);
                            setCurrentWeatherIcon(response);
                            setConditions(response);
                            setCurrentDayTemp(response);
                            weatherRVModelArrayList.addAll(loadFile(response));
                            weatherRVModelWeeklyArrayList.addAll(loadFileWeekly(response));
                            weatherRVAdapter.notifyDataSetChanged();
                            weatherRVAdapterWeekly.notifyDataSetChanged();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
        Response.ErrorListener error= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };



            JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET,urlToUSe,null,listener,error);
            queue.add(request);
            SharedPreferences.Editor editor=prefs.edit();
            editor.putString(PREF_LOCATION,placeNew);
            editor.putBoolean(PREF_UNITS,isF);
            editor.apply();





    }

    private List<WeatherRVModelWeekly> loadFileWeekly(JSONObject response) {
        List<WeatherRVModelWeekly> dailyList= new ArrayList<>();
        SimpleDateFormat inputFormat= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat= new SimpleDateFormat("EEEE, M/d", Locale.getDefault());
        try{
            JSONArray daysArray= response.getJSONArray("days");
            for(int j=0;j<15;j++){

                JSONObject daysObj= daysArray.getJSONObject(j);
                //Setting the day
                String datetime = daysObj.getString("datetime");
                Date date = inputFormat.parse(datetime);
                String formattedDate= outputFormat.format(date);

                //Setting the temperature

                double tempMini,tempMax,morningTemp,afterTemp,evenTemp,nightTemp;
                String temperatureMini,temperatureMax,morning,afternoon,evening,night;
                if(isF){
                    tempMini= daysObj.getDouble("tempmin");
                    tempMax= daysObj.getDouble("tempmax");
                    morningTemp= daysObj.getJSONArray("hours").getJSONObject(8).getDouble("temp");
                    afterTemp=daysObj.getJSONArray("hours").getJSONObject(13).getDouble("temp");
                    evenTemp=daysObj.getJSONArray("hours").getJSONObject(17).getDouble("temp");
                    String str="Sunday, 3/12";

                    if(formattedDate.equals(str)){
                        nightTemp=daysObj.getJSONArray("hours").getJSONObject(22).getDouble("temp");
                    }
                    else{
                        nightTemp=daysObj.getJSONArray("hours").getJSONObject(23).getDouble("temp");
                    }


                    temperatureMini= String.format("%.0f\u00B0F",tempMini);
                    temperatureMax= String.format("%.0f\u00B0F",tempMax);
                    morning= String.format("%.0f\u00B0F",morningTemp);
                    afternoon= String.format("%.0f\u00B0F",afterTemp);
                    evening=String.format("%.0f\u00B0F",evenTemp);
                    night=String.format("%.0f\u00B0F",nightTemp);

                }
                else{
                    tempMini= daysObj.getDouble("tempmin");
                    tempMax= daysObj.getDouble("tempmax");
                    morningTemp= daysObj.getJSONArray("hours").getJSONObject(8).getDouble("temp");
                    afterTemp=daysObj.getJSONArray("hours").getJSONObject(13).getDouble("temp");
                    evenTemp=daysObj.getJSONArray("hours").getJSONObject(17).getDouble("temp");
                    nightTemp=daysObj.getJSONArray("hours").getJSONObject(22).getDouble("temp");
                    temperatureMini= String.format("%.0f\u00B0C",tempMini);
                    temperatureMax= String.format("%.0f\u00B0C",tempMax);
                    morning= String.format("%.0f\u00B0C",morningTemp);
                    afternoon= String.format("%.0f\u00B0C",afterTemp);
                    evening=String.format("%.0f\u00B0C",evenTemp);
                    night=String.format("%.0f\u00B0C",nightTemp);
                }
                String temperature= temperatureMini +"/ "+temperatureMax;

                //Precipitation
                double preciProb= daysObj.getDouble("precipprob");
                int preciProb1=(int)Math.ceil(preciProb);
                String precipitationProbabilty="("+ String.valueOf(preciProb1)+" % precip.)";

                //Description
                String desc= daysObj.getString("description");

                //UV
                double uv= daysObj.getDouble("uvindex");
                int uv1= (int)Math.ceil(uv);
                String ultra="UV Index: "+String.valueOf(uv1);

                //Weather Icon
                String icon= daysObj.getString("icon");
                icon=icon.replace("-","_");

                WeatherRVModelWeekly wvk= new WeatherRVModelWeekly(formattedDate,precipitationProbabilty,temperature,desc,ultra,morning,afternoon,evening,night,icon);
                dailyList.add(wvk);





            }


        }catch (Exception e){
            e.printStackTrace();
        }


        return dailyList;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if(item.getItemId()==R.id.faranhiet){
            if(isF){
                item.setIcon(getResources().getDrawable(R.drawable.units_c));
                isF= false;
               makeWeatherRequest(place);


            }
            else{
                item.setIcon(getResources().getDrawable(R.drawable.units_f));
                isF=true;
                makeWeatherRequest(place);


            }



            return true;

        }
        else if(item.getItemId()==R.id.daily){
            Intent intent = new Intent(this, WeeklyWeather.class);
            intent.putExtra("place",place);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId()==R.id.location){
            showChangeLocationDialog();
            return true;
        }


        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showChangeLocationDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Enter a location");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        String message="For US location enter a 'City' or 'City,State'\n\nFor International location enter 'City,Country'";
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newLocation=input.getText().toString();
                makeWeatherRequest(newLocation);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
            }
        });
        builder.show();
    }


    private List<WeatherRVModel> loadFile(JSONObject response) {


        List<WeatherRVModel> hourlyList= new ArrayList<>();
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputFormat= new SimpleDateFormat("hh:mm a");


        try{
            JSONArray daysArray= response.getJSONArray("days");

            for(int j=0;j<daysArray.length();j++){

            JSONObject dayObj= daysArray.getJSONObject(j);
            String day;
            if(j==0) {
                day = "Today";
            }
            else if(j==1){
                day="Tomorrow";
            }
            else{

                day = dayObj.getString("datetime");
            }
            JSONArray hoursArray= dayObj.getJSONArray("hours");


            for(int i=0;i<hoursArray.length();i++) {

                JSONObject hoursObj = hoursArray.getJSONObject(i);
                String time = hoursObj.getString("datetime");
                if(j==0) {
                    int hour = Integer.parseInt(time.substring(0, 2));
                    Calendar cal = Calendar.getInstance();
                    int current=cal.get(Calendar.HOUR_OF_DAY);
                    if (hour <= current) {
                        continue;
                    }
                }
                if (isF) {


                    Date date = inputFormat.parse(time);
                    String formattedTime = outputFormat.format(date);

                    double temp = hoursObj.getDouble("temp");

                    String temperature = String.format("%.0f\u00B0F", temp);
                    String description = hoursObj.getString("conditions");
                    String icon = hoursObj.getString("icon");
                    icon = icon.replace("-", "_");


                    WeatherRVModel wv = new WeatherRVModel(day, formattedTime, temperature, description, icon);
                    hourlyList.add(wv);
                }
                else{
                    Date date = inputFormat.parse(time);
                    String formattedTime= outputFormat.format(date);

                    double temp = hoursObj.getDouble("temp");

                    String temperature= String.format("%.0f\u00B0C",temp);
                    String description = hoursObj.getString("conditions");
                    String icon = hoursObj.getString("icon");
                    icon = icon.replace("-","_");


                    WeatherRVModel wv = new WeatherRVModel(day, formattedTime, temperature, description, icon);
                    hourlyList.add(wv);
                }



              }
            }



        }catch(JSONException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return hourlyList;

    }

    public void setCurrentDayTemp(JSONObject response) throws JSONException {
        double morningTemp= response.getJSONArray("days").getJSONObject(0)
                .getJSONArray("hours").getJSONObject(8).getDouble("temp");
        double afterTemp= response.getJSONArray("days").getJSONObject(0)
                .getJSONArray("hours").getJSONObject(13).getDouble("temp");
        double evenTemp=response.getJSONArray("days").getJSONObject(0)
                .getJSONArray("hours").getJSONObject(17).getDouble("temp");
        double nightTemp=response.getJSONArray("days").getJSONObject(0)
                .getJSONArray("hours").getJSONObject(23).getDouble("temp");

        String mornWithDegreeSymbol;
        String afterWithDegreeSymbol;
        String evenWithDegreeSymbol;
        String nightWithDegreeSymbol;
        if(isF){
            mornWithDegreeSymbol = String.format("%.0f\u00B0F", morningTemp);
            afterWithDegreeSymbol = String.format("%.0f\u00B0F", afterTemp);
            evenWithDegreeSymbol = String.format("%.0f\u00B0F", evenTemp);
            nightWithDegreeSymbol = String.format("%.0f\u00B0F", nightTemp);

        }
        else{
            mornWithDegreeSymbol = String.format("%.0f\u00B0C", morningTemp);
            afterWithDegreeSymbol = String.format("%.0f\u00B0C", afterTemp);
            evenWithDegreeSymbol = String.format("%.0f\u00B0C", evenTemp);
            nightWithDegreeSymbol = String.format("%.0f\u00B0C", nightTemp);
        }
        morning.setText(mornWithDegreeSymbol);
        afternoon.setText(afterWithDegreeSymbol);
        evening.setText(evenWithDegreeSymbol);
        night.setText(nightWithDegreeSymbol);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!hasNetworkConnection()){
            getMenuInflater().inflate(R.menu.menu_main, menu);
            optionsMenu=menu;
            optionsMenu.getItem(0).setEnabled(false);
            optionsMenu.getItem(1).setEnabled(false);
            optionsMenu.getItem(2).setEnabled(false);
        }
        else{
            getMenuInflater().inflate(R.menu.menu_main, menu);
            optionsMenu=menu;
            MenuItem tempItem= optionsMenu.findItem(R.id.faranhiet);
            if(isF){
                tempItem.setIcon(R.drawable.units_f);
            }else{
                tempItem.setIcon(R.drawable.units_c);
            }

        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);

        // Disable all menu items in the options-menu if there is no network connection
        boolean hasConnection = hasNetworkConnection();
        for (int i = 0; i < optionsMenu.size(); i++) {
            optionsMenu.getItem(i).setEnabled(hasConnection);
        }

        return true;
    }


    public void setDateTime(JSONObject  response) throws JSONException {
        Log.d(TAG, "Inside Set Date time ");
        JSONObject currentConditions = response.getJSONObject("currentConditions");
        long datetimeEpoch = currentConditions.getLong("datetimeEpoch");
        Date dateTimer =new Date(datetimeEpoch * 1000);
        SimpleDateFormat dateFormat= new SimpleDateFormat("E MMM dd hh:mm a','yyyy");
        String formattedDateTime= dateFormat.format(dateTimer);
        datetime.setText(formattedDateTime);
        //Sunrise Time
        long sunriseEpoch= currentConditions.getLong("sunriseEpoch");
        Date sunriseTimer= new Date(sunriseEpoch*1000);
        SimpleDateFormat sunriseTimeFormat= new SimpleDateFormat("hh:mm a");
        String formattedSunrise=sunriseTimeFormat.format(sunriseTimer);
        sunrise.setText("Sunrise: "+formattedSunrise);
        //Sunset Time
        long sunsetEpoch= currentConditions.getLong("sunsetEpoch");
        Date sunsetTimer= new Date(sunsetEpoch*1000);
        SimpleDateFormat sunsetTimeFormat= new SimpleDateFormat("hh:mm a");
        String formattedSunset=sunriseTimeFormat.format(sunsetTimer);
        sunset.setText("Sunset: "+formattedSunset);


    }

    public void setConditions(JSONObject response) throws JSONException{
        //Setting the temperature
        Log.d(TAG,"Inside Set Temperature");
        JSONObject currentConditions=response.getJSONObject("currentConditions");
        double temp= currentConditions.getDouble("temp");
        String tempWithDegreeSymbol;
        if(isF){
            tempWithDegreeSymbol= String.format("%.0f\u00B0F",temp);
        }
        else{
            tempWithDegreeSymbol= String.format("%.0f\u00B0C",temp);
        }

        temperature.setText(tempWithDegreeSymbol);

        //Setting the Feels like

        double feels=currentConditions.getDouble("feelslike");
        String tempFeelsLike;
        if(isF){
            tempFeelsLike=String.format("%.0f\u00B0F",feels);
        }
        else{
            tempFeelsLike=String.format("%.0f\u00B0C",feels);
        }


        feelsLike.setText("Feels Like: "+tempFeelsLike);

        //Setting the Weather Conditions
        String weatherDescription=currentConditions.getString("conditions");
        int cloudCover= currentConditions.getInt("cloudcover");
        weatherDisc.setText(weatherDescription+" ("+cloudCover+"% clouds)");

        //Getting the Wind Direction

        double windGust=0;
        double windDir= currentConditions.getDouble("winddir");
        double windSpeed=currentConditions.getDouble("windspeed");
        String windGustString= currentConditions.getString("windgust");
        //If the Wind Gust is null then I am only displaying Direction and Wind Speed
        //Else I am displaying all three
        if(!windGustString.isEmpty() && windGustString!="null"){
            windGust=Double.parseDouble(windGustString);
            String direction = getDirection(windDir);
            int roundedWindSpeed= (int)Math.ceil(windSpeed);
            int roundWindGust= (int)Math.ceil(windGust);
            if(isF){
                wind.setText("Winds: "+direction+" at "+String.valueOf(roundedWindSpeed)+" mph gusting to "+String.valueOf(roundWindGust)+" mph");
            }
            else{
                wind.setText("Winds: "+direction+" at "+String.valueOf(roundedWindSpeed)+" m/s gusting to "+String.valueOf(roundWindGust)+" m/s");
            }

        }
        else{
            int roundedWindSpeed= (int)Math.ceil(windSpeed);
            String direction = getDirection(windDir);
            if(isF){
                wind.setText("Winds: "+direction+" at "+String.valueOf(roundedWindSpeed)+" mph");
            }
            else{
                wind.setText("Winds: "+direction+" at "+String.valueOf(roundedWindSpeed)+" m/s");
            }

        }


        //Humidity

        double humidityTemp= currentConditions.getDouble("humidity");
        int humidity1=(int) Math.ceil(humidityTemp);
        humidity.setText("Humidity: "+humidity1+"%");

        //UV Index
        double uvTemp= currentConditions.getDouble("uvindex");
        int uv1= (int)Math.ceil(uvTemp);

        uv.setText("UV Index: "+String.valueOf(uv1));

        //Visibility
        double vis= currentConditions.getDouble("visibility");
        if(isF) {
            visibility.setText("Visibility: " + String.valueOf(vis)+" mi");
        }
        else {
            visibility.setText("Visibility: " + String.valueOf(vis)+" km");
        }
    }



    public String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }


    public void setCurrentWeatherIcon(JSONObject response) throws JSONException{

        JSONObject currentConditions= response.getJSONObject("currentConditions");
        String icon1= currentConditions.getString("icon");

        switch (icon1) {
            case "clear-day":
                icon.setImageResource(R.drawable.clear_day);
                break;
            case "clear-night":
                icon.setImageResource(R.drawable.clear_night);
                break;
            case "partly-cloudy-day":
                icon.setImageResource(R.drawable.partly_cloudy_day);
                break;
            case "partly-cloudy-night":
                icon.setImageResource(R.drawable.partly_cloudy_night);
                break;
            case "cloudy":
                icon.setImageResource(R.drawable.cloudy);
                break;
            case "rain":
                icon.setImageResource(R.drawable.rain);
                break;
            case "sleet":
                icon.setImageResource(R.drawable.sleet);
                break;
            case "snow":
                icon.setImageResource(R.drawable.snow);
                break;
            case "wind":
                icon.setImageResource(R.drawable.wind);
                break;
            case "fog":
                icon.setImageResource(R.drawable.fog);
                break;
            case "thunder-rain":
                icon.setImageResource(R.drawable.thunder_rain);
                break;
            case "thunder-showers-day":
                icon.setImageResource(R.drawable.thunder_showers_day);
                break;
            case "thunder-showers-night":
                icon.setImageResource(R.drawable.thunder_showers_night);
                break;
            case "snow-showers-day":
                icon.setImageResource(R.drawable.snow_showers_day);
                break;

            case "snow-showers-night":
                icon.setImageResource(R.drawable.snow_showers_night);
                break;

            case "showers-day":
                icon.setImageResource(R.drawable.showers_day);
                break;
            case "showers-night":
                icon.setImageResource(R.drawable.showers_night);
                break;
            default:
                Log.d(TAG,"ParseCurrentRecord:CANNOT FIND ICON"+icon1);
                break;
        }

    }


}

