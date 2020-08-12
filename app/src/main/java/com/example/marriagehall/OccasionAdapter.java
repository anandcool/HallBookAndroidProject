package com.example.marriagehall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OccasionAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] occasions;

    public OccasionAdapter(@NonNull Context context, String[] occasions) {
        super(context, R.layout.occasion_layout,occasions);
        this.context = context;
        this.occasions = occasions;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.occasion_layout, null);
        TextView txt = row.findViewById(R.id.occasion_text);
        txt.setText(occasions[position]);
        return row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.occasion_layout, null);
        TextView txt = row.findViewById(R.id.occasion_text);

        txt.setText(occasions[position]);
        return row;
    }
}
