package com.example.marriagehall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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

public class HallActivity extends AppCompatActivity {

    ImageView img_hall;
    TextView name, rating, address, description;
    Button book_btn;
    RatingBar stars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img_hall = findViewById(R.id.hall_pictures);
        name = findViewById(R.id.hall_title);
        rating = findViewById(R.id.rating_text);
        address = findViewById(R.id.hall_address);
        description = findViewById(R.id.hall_description);
        stars = findViewById(R.id.hall_stars);
        book_btn = findViewById(R.id.book_btn);

        //to receive values from Hall Adapter class when user clicks on particular hall.
        Intent in = getIntent();
        final String hall_id = in.getStringExtra("id");
        final int user_id = in.getIntExtra("user_id", 0);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.ROOT_HALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray myobj = new JSONArray(response);
                            JSONObject obj = myobj.getJSONObject(0);
//                            img_hall.setImageResource(obj.getInt("image"));
                            name.setText(obj.getString("name"));
                            rating.setText(rating.getText()+""+obj.getInt("rating"));
                            stars.setNumStars(obj.getInt("rating"));
                            address.setText(obj.getString("address"));
                            description.setText(obj.getString("description"));
                        } catch (JSONException e) {
                            Toast.makeText(HallActivity.this, "JSON ERROR:"+e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("myTag", ""+e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HallActivity.this, ""+error.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", hall_id);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        //functioning of book now button
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HallActivity.this, BookingActivity.class);
                i.putExtra("hall_id", hall_id);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });
    }
}
