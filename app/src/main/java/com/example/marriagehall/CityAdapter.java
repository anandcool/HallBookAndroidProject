package com.example.marriagehall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marriagehall.ui.home.HomeFragment;

import static android.content.Context.MODE_PRIVATE;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private String[] city_names;
    private int userId;
    private String email ,name;

    public CityAdapter(String[] names , int userId, String email, String name) {
        this.city_names = names;
        this.userId = userId;
        this.email = email;
        this.name = name;
    }


    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.city_list_layout, parent, false);
        return new CityAdapter.CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.CityViewHolder holder, int position) {
        final String city = city_names[position];
        holder.city_name.setText(city);

        //to store value of city in shared preferences
//        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("city", city);
//        editor.putInt("req", 0);
//        editor.apply();

        //to set on click listener on Recycler View
        holder.city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on the click of specific Hall in Recycler View
                Intent in = new Intent(view.getContext(), DashboardActivity.class);
                in.putExtra("city", city);
                in.putExtra("user_id", userId);
                in.putExtra("email_id", email);
                in.putExtra("user_name", name);
                view.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return city_names.length;
    }

    public class CityViewHolder extends RecyclerView.ViewHolder{
        TextView city_name;
        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            city_name = itemView.findViewById(R.id.city_name);
        }
    }
}
