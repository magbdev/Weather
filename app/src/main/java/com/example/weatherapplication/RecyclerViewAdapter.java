package com.example.weatherapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    @NonNull

    private List<String> city = new ArrayList<>();
    private Context context;

    public List<String> getList(){
        return city;
    }

    public RecyclerViewAdapter(@NonNull List<String> city, Context context) {
        this.city = city;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherAsyncTask task = new WeatherAsyncTask(holder.image,holder.city, holder.temperature,context);
        task.execute(city.get(position));

    }

    @Override
    public int getItemCount() {
        return city.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView city;
        TextView temperature;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            city = itemView.findViewById(R.id.city);
            temperature = itemView.findViewById(R.id.temperature);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }


}


