package com.example.marriagehall.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.marriagehall.HallsAdapter;
import com.example.marriagehall.R;
import com.example.marriagehall.URL;
import com.example.marriagehall.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //making recycler view
        final RecyclerView hall_list = root.findViewById(R.id.hall_list);
        hall_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        //getting city name to filter halls
        Intent in = getActivity().getIntent();
        final String city = in.getStringExtra("city");
        final int user_id = in.getIntExtra("user_id", 0);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.ROOT_HALLS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("No halls found for this particular location")) {
                            try {
                                JSONArray obj = new JSONArray(response);
                                String[] ids = new String[obj.length()];
                                String[] halls = new String [obj.length()];
                                String[] ratings = new String[obj.length()];
                                for(int i=0; i<obj.length(); i++){
                                    String str0 = obj.getJSONObject(i).getString("id");
                                    ids[i] = str0;
                                    String str1 = obj.getJSONObject(i).getString("name");
                                    halls[i] = str1;
                                    String str2 = obj.getJSONObject(i).getString("rating");
                                    ratings[i] = str2;
                                }
                                hall_list.setAdapter(new HallsAdapter(user_id, ids, halls, ratings));
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

        return root;
    }
}
