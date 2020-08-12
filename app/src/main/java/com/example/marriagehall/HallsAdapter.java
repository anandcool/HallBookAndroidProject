package com.example.marriagehall;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.StringRequest;

import static androidx.core.content.ContextCompat.startActivity;

public class HallsAdapter extends RecyclerView.Adapter<HallsAdapter.HallsViewHolder> {

    private String[] names;
    private String[] ratings;
    private String[] hall_id;
    private int user_id;

    public HallsAdapter(int user_id, String[] hall_id, String[] names, String[] ratings){
        this.user_id = user_id;
        this.hall_id = hall_id;
        this.names = names;
        this.ratings = ratings;
    }

    @NonNull
    @Override
    public HallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.halls_list_layout,parent,false);
        return new HallsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HallsViewHolder holder, int position) {
        final String id = hall_id[position];
        final String title = names[position];
        final String star = ratings[position];
        holder.hall_name.setText(title);
        holder.hall_rating.setText(holder.hall_rating.getText()+star);

        //to set on click listener on Recycler View
        holder.hall_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on the click of specific Hall in Recycler View
                Intent in = new Intent(view.getContext(), HallActivity.class);
                in.putExtra("id",id);
                in.putExtra("user_id", user_id);
                view.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
                return names.length;
    }

    public class HallsViewHolder extends RecyclerView.ViewHolder{
        ImageView hall_img;
        TextView hall_name, hall_rating;
        LinearLayout hall_id;
        public HallsViewHolder(@NonNull View itemView) {
            super(itemView);
            hall_img = itemView.findViewById(R.id.hall_img);
            hall_name = itemView.findViewById(R.id.hall_name);
            hall_rating = itemView.findViewById(R.id.hall_rating);
            hall_id = itemView.findViewById(R.id.hall_id);
        }
    }

}
