package com.example.marriagehall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CityActivity extends AppCompatActivity {

    String[] city_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        //getting these values in intent from login activity
        Intent in = getIntent();
        final int user_id = in.getIntExtra("user_id",0);
        final String email = in.getStringExtra("user_email");
        final String name = in.getStringExtra("user_name");
        int req= in.getIntExtra("req", 0);

        //to check that if the user is new or already entered the location
        final SharedPreferences pref = getSharedPreferences("prefs", MODE_PRIVATE);
        if((!pref.getString("city","text").equals("text")) && req!=1){
            StringRequest stringReq = new StringRequest(Request.Method.POST, URL.ROOT_CITY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(CityActivity.this, DashboardActivity.class);
                            intent.putExtra("user_id", user_id);
                            intent.putExtra("city", pref.getString("city", null));
                            intent.putExtra("email_id",pref.getString("email", null));
                            intent.putExtra("user_name",pref.getString("name", null));
                            startActivity(intent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CityActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("city_name", pref.getString("city", null));
                    params.put("email_id", pref.getString("email", null));
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringReq);
        }

        //making recycler view
        final RecyclerView city_list = findViewById(R.id.city_list_names);
        city_list.setLayoutManager(new LinearLayoutManager(this));

        //to load city names from server
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL.ROOT_CITIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            city_names = new String[jsonArray.length()];
                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                city_names[i] = jsonArray.getJSONObject(i).getString("city");
                            }
                            city_list.setAdapter(new CityAdapter(city_names, user_id, email, name));
                        } catch (JSONException e) {
                            Toast.makeText(CityActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CityActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
