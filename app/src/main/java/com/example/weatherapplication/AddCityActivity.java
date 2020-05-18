package com.example.weatherapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;


public class AddCityActivity extends Activity {

    private EditText editTextCity;
    private Button addButton;

    public final static String CITY = "new_city";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city);
        editTextCity = findViewById(R.id.editText);
        addButton = findViewById(R.id.addButton);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WeathersActivity.class);
                final String cityText = editTextCity.getText().toString();
                intent.putExtra(CITY, cityText);
                setResult(2, intent);
                finish();
            }
        });
    }
}
