package com.example.weatherapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.weatherapplication.AddCityActivity.CITY;

public class WeathersActivity extends Activity {

    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton myLocationButton,youtubeButton,addCityButton;

    private List<String> listCity = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            listCity = savedInstanceState.getStringArrayList("LIST");
        }
        else{
            initCityList();
        }
        setContentView(R.layout.weather_list);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(listCity, this);
        recyclerView.setAdapter(adapter);

        myLocationButton = findViewById(R.id.myLocationButton);
        myLocationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WeathersActivity.this, MainActivity.class));
            }
        });
        youtubeButton = findViewById(R.id.youtubeButton);
        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=JqUREqYduHw&list=PLwygboCFkeeA2w1fzJm44swdG-NnyB6ip"));
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });
        addCityButton = findViewById(R.id.addCityButton);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WeathersActivity.this,AddCityActivity.class),2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2) {
            String str = data.getStringExtra(CITY);
            int insertIndex = listCity.size();
            listCity.add(insertIndex, str);
            adapter.notifyItemInserted(insertIndex);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("LIST", (ArrayList<String>) adapter.getList());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listCity = savedInstanceState.getStringArrayList("LIST");
    }

    private void initCityList(){
        listCity.add("London");
        listCity.add("Warsaw");
        //listCity.add("Venice");
        listCity.add("Amsterdam");
        listCity.add("Chicago");
        listCity.add("Tokio");
        listCity.add("Moskwa");
        //listCity.add("Bratislava");
        listCity.add("Praga");

    }

}
