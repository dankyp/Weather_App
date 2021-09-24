package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    EditText et_location;
    TextView tv_temp;
    TextView tv_weather;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "b6bd9ab45562773be8872be7a7d95bbc";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_WeatherApp);
        setContentView(R.layout.activity_main);
        et_location = findViewById(R.id.et_location);
        tv_temp = findViewById(R.id.tv_temp);
        tv_weather = findViewById(R.id.tv_weather);
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = et_location.getText().toString().trim();
        if (city.equals("")){
            Toast.makeText(getApplicationContext(),"City required!",Toast.LENGTH_LONG);
        } else{
            tempUrl = url + "?q=" + city + "&appid=" + appid + "&units=metric";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                String output = "";
                String degrees = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    int temp = jsonObjectMain.getInt("temp");
                    int temp_min = jsonObjectMain.getInt("temp_min");
                    int temp_max = jsonObjectMain.getInt("temp_max");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String cityName = jsonResponse.getString("name");
                    String countryName = jsonObjectSys.getString("country");
                    tv_temp.setTextColor(Color.RED);
                    degrees += df.format(temp) + "°C";
                    tv_temp.setText(degrees);
                    tv_weather.setTextColor(Color.RED);
                    output += "\n Current weather in " + cityName + " (" + countryName + ")"
                            + "\n Temp: " + df.format(temp) + " °C"
                            + "\n with " + description
                            + "\n Lows today of: " + df.format(temp_min) + " °C"
                            + "\n Highs today of: " + df.format(temp_max) + " °C";
                    tv_weather.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString().trim(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
