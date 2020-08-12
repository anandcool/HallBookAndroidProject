package com.example.marriagehall.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.marriagehall.DashboardActivity;
import com.example.marriagehall.R;
import com.example.marriagehall.URL;
import com.example.marriagehall.VolleySingleton;
import com.example.marriagehall.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private EditText name, password, confirmPass, phoneNo;
    private Button saveDetails;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        name = root.findViewById(R.id.edit_name);
        password = root.findViewById(R.id.edit_pass);
        confirmPass = root.findViewById(R.id.edit_cPass);
        phoneNo = root.findViewById(R.id.edit_pno);
        saveDetails = root.findViewById(R.id.edit_saveDetailsBtn);

        //intent to receive values from city adapter
        Intent in = getActivity().getIntent();
        final String email_id = in.getStringExtra("email_id");

        //to get user previous details of user to fill in edit text
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.ROOT_USER_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray objArray = new JSONArray(response);
//                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                            JSONObject userObj = objArray.getJSONObject(0);
                            name.setText(userObj.getString("name"));
                            password.setText(userObj.getString("password"));
                            confirmPass.setText(userObj.getString("confirm_pass"));
                            phoneNo.setText(userObj.getString("phone_no"));
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email_id", email_id);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);


        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to update the details altered by user into db
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL.ROOT_UPDATE_DETAILS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("updated")) {
                                    Toast.makeText(getActivity(), "Details Updated", Toast.LENGTH_SHORT).show();
//                                    DashboardActivity da = new DashboardActivity();
//                                    da.updateData(name.getText().toString());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("confirm_pass", confirmPass.getText().toString());
                        params.put("phone_no", phoneNo.getText().toString());
                        params.put("email_id", email_id);
                        return params;
                    }
                };
                VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);
            }
        });

        return root;
    }
}
