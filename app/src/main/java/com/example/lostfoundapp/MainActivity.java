package com.example.lostfoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Boolean advert_created = intent.getBooleanExtra("success", false);
        if (advert_created) {
            Toast.makeText(MainActivity.this, "Advert created successfully!", Toast.LENGTH_LONG).show();
        }
    }

    public void createAdvertOnClick(View view) {
        Intent createAdvertIntent = new Intent(MainActivity.this, AdvertForm.class);
        startActivity(createAdvertIntent);
    }

    public void viewAdvertListOnClick(View view) {
        Intent createAdvertIntent = new Intent(MainActivity.this, AdvertList.class);
        startActivity(createAdvertIntent);
    }
}