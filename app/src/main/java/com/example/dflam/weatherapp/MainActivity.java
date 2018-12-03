package com.example.dflam.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements TextView.OnEditorActionListener, View.OnClickListener {

    TextView tempTextView;
    TextView dateTextView;
    TextView weatherDescTextView;
    TextView cityTextView;
    ImageView weatherImageView;
    EditText etCity;
    Button getWeatherButton;
    String etUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = findViewById(R.id.tempTextView);
        dateTextView = findViewById(R.id.dateTextView);
        weatherDescTextView = findViewById(R.id.weatherDescTextView);
        cityTextView = findViewById(R.id.cityTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        getWeatherButton = findViewById(R.id.getWeatherButton);
        getWeatherButton.setOnClickListener(this);

        etCity = findViewById(R.id.etCity);
        String city = etCity.getText().toString();
        etUnits = "Imperial";

        if (city == "")
            city = "46250";

        dateTextView.setText(getCurrentDate());

        getWeather(city, etUnits);
    }

    private String getCurrentDate () {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }

    public void getWeather(String city, String etUnits) {


        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=" + etUnits + "&appid=3c9639b2d937cc613e77cf142beccbe4";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject responseObject) {
                        //tempTextView.setText("Response: " + response.toString());
                        Log.v("Weather", "Response: " + responseObject.toString());

                        try {
                            JSONObject mainJSONObject = responseObject.getJSONObject("main");
                            JSONArray weatherArray = responseObject.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                            String temp = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp")));
                            String weatherDescription = firstWeatherObject.getString("description");
                            String city = responseObject.getString("name");

                            tempTextView.setText(temp);
                            weatherDescTextView.setText(weatherDescription);
                            cityTextView.setText(city);

                            int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
                            weatherImageView.setImageResource(iconResourceId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);

    }

    @Override
    public void onClick(View v) {
        etCity = findViewById(R.id.etCity);
        String city = etCity.getText().toString();
        etUnits = "Imperial";

        if (city == "")
            city = "Indianapolis";

        getWeather(city, etUnits);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            etCity = findViewById(R.id.etCity);
            String city = etCity.getText().toString();
            etUnits = "Imperial";

            if (city == "")
                city = "Indianapolis";

            getWeather(city, etUnits);
        }
        return false;
    }
}
