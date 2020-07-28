package com.yajith.womensafety;

import android.app.DownloadManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class weather {
    String temp;
    public void url()
    {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, update(Login.lon, Login.lat),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject jsonObjectRequest1 = response.getJSONObject("main");
                    JSONArray jsonArray=response.getJSONArray("weather");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    temp=String.valueOf(jsonObjectRequest1.getDouble("temp"));
                }
                catch (JSONException e)
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
    }
    public String update(double lon,double lat)
    {
        String api="ea5812bb46ca714c0815ebc784978bed";
        String url="https://samples.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+api;
        return url;
    }
}
