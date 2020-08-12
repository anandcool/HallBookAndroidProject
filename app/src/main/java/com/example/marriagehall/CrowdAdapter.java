package com.example.marriagehall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.marriagehall.R;

public class CrowdAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] crowd;

    public CrowdAdapter(@NonNull Context context, String[] crowd) {
        super(context, R.layout.num_guests_layout,crowd);
        this.context = context;
        this.crowd = crowd;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.num_guests_layout, null);
        TextView txt = row.findViewById(R.id.crowd_text);
        txt.setText(crowd[position]);
        return row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.num_guests_layout, null);
        TextView txt = row.findViewById(R.id.crowd_text);

        txt.setText(crowd[position]);
        return row;
    }
}
