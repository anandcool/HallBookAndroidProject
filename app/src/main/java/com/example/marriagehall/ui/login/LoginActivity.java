package com.example.marriagehall.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.marriagehall.CityActivity;
import com.example.marriagehall.DashboardActivity;
import com.example.marriagehall.R;
import com.example.marriagehall.RegisterActivity;
import com.example.marriagehall.URL;
import com.example.marriagehall.User;
import com.example.marriagehall.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private ImageView signupArrow;
    private EditText usernameEditText, passwordEditText;
    private ProgressBar loadingProgressBar;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //to check that if user had hit logout button
        Intent in = getIntent();
        int req = in.getIntExtra("req", 0);

        //check that user is already logged in or not
        final SharedPreferences sp = getSharedPreferences("myprefs", MODE_PRIVATE);
        if ((!sp.getString("id", "text").equals("text")) && req != 1) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.ROOT_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);//converting response to json object

                                if (!obj.getBoolean("error")) {
                                    JSONObject userJson = obj.getJSONObject("user"); //creating the json object of user that is received in response with server
                                    user = new User(
                                            userJson.getString("name"),
                                            userJson.getString("email_id"),
                                            userJson.getString("password"),
                                            userJson.getString("confirm_pass"),
                                            userJson.getString("phone_no")
                                    );
                                    Toast.makeText(LoginActivity.this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, CityActivity.class);
                                    intent.putExtra("user_id", userJson.getInt("user_id"));
                                    intent.putExtra("user_email", user.getEmail());
                                    intent.putExtra("user_name", user.getName());
                                    intent.putExtra("req", 0);
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "JSON Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "Error2:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email_id", sp.getString("id", null));
                    params.put("password", sp.getString("password", null));
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login_btn);
        loadingProgressBar = findViewById(R.id.loading);

        signupArrow = findViewById(R.id.back_arrow);
        signupArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    public void verifyUser() {
        String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.length() == 0) {
            usernameEditText.setError("Enter email address");
            usernameEditText.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameEditText.setError("Enter valid email address");
            usernameEditText.requestFocus();
            return;
        } else if (password.length() == 0) {
            passwordEditText.setError("Enter password");
            passwordEditText.requestFocus();
            return;
        } else {
            loadingProgressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.ROOT_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Toast.makeText(LoginActivity.this, "Working", Toast.LENGTH_SHORT).show();
                                JSONObject obj = new JSONObject(response);//converting response to json object

                                if (!obj.getBoolean("error")) {
                                    JSONObject userJson = obj.getJSONObject("user"); //creating the json object of user that is received in response with server
                                    user = new User(
                                            userJson.getString("name"),
                                            userJson.getString("email_id"),
                                            userJson.getString("password"),
                                            userJson.getString("confirm_pass"),
                                            userJson.getString("phone_no")
                                    );
                                    SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("id", user.getEmail());
                                    editor.putString("password", user.getPassword());
                                    editor.apply();
                                    Toast.makeText(LoginActivity.this, "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, CityActivity.class);
                                    intent.putExtra("user_id", userJson.getInt("user_id"));
                                    intent.putExtra("user_email", user.getEmail());
                                    intent.putExtra("user_name", user.getName());
                                    finish();
                                    startActivity(intent);
                                } else {
                                    loadingProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(LoginActivity.this, "" + obj.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "JSON Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "Error2:  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("email_id", usernameEditText.getText().toString());
                    params.put("password", passwordEditText.getText().toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }
}
