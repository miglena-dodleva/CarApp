package com.example.car_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CarApiActivity extends AppCompatActivity {

    TextView resultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_api);

        resultsTextView = (TextView) findViewById(R.id.results);
        getData();

    }

    public void getData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://catfact.ninja/fact";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        JSONObject jsonObject = null;
                        String statistic = "";
                        try {
                            jsonObject = new JSONObject(response);
                            String facts = jsonObject.getString("fact");
                            statistic = "Fact about cars :" + facts;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        resultsTextView.setText(statistic);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultsTextView.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }


}
