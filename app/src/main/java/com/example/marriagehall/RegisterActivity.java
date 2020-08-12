package com.example.marriagehall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.marriagehall.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password, cpass, phoneno;
    Button signup;
    String nm, em, pass, copass, pno;
    ImageView loginArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.reg_name);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_pass);
        cpass = findViewById(R.id.reg_cpass);
        phoneno = findViewById(R.id.reg_pno);

        loginArrow = findViewById(R.id.back_arrow);
        signup = findViewById(R.id.reg_signup);

        loginArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //after the click of signup button
                registerUser();
            }
        });
    }
    public void registerUser(){
        nm = name.getText().toString();
        em = email.getText().toString();
        pass = password.getText().toString();
        copass = cpass.getText().toString();
        pno = phoneno.getText().toString();

        if (em.length() == 0) {
            email.setError("Enter email address");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.setError("Enter valid email address");
            email.requestFocus();
            return;
        }
        else if(pass.length()==0) {
            password.setError("Enter password");
            password.requestFocus();
            return;
        }
        else if(pass.length()<6) {
            password.setError("Weak Password");
            password.requestFocus();
            return;
        }
        else if(!pass.equals(copass))
        {
            cpass.setError("Password does not matches");
            cpass.requestFocus();
            return;
        }
        else if(nm.length()==0)
        {
            name.setError("Enter name");
            name.requestFocus();
            return;
        }
        else if(pno.length()!=10)
        {
            phoneno.setError("Enter valid Phone Number");
            phoneno.requestFocus();
            return;
        }
        else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.ROOT_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(RegisterActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name",name.getText().toString());
                    params.put("email_id",email.getText().toString());
                    params.put("password",password.getText().toString());
                    params.put("confirm_pass",cpass.getText().toString());
                    params.put("phone_no",phoneno.getText().toString());
                    return params;
            }
        };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
}
