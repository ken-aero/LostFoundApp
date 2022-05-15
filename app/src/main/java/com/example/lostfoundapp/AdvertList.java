package com.example.lostfoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lostfoundapp.data.DatabaseHelper;
import com.example.lostfoundapp.model.Advert;

import java.util.ArrayList;
import java.util.List;

public class AdvertList extends AppCompatActivity {

    ListView advertListView ;
    ArrayList<String> advertArrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_list);

        Intent intent = getIntent();
        Boolean advert_removed = intent.getBooleanExtra("success", false);
        if (advert_removed) {
            Toast.makeText(AdvertList.this, "Advert removed successfully!", Toast.LENGTH_LONG).show();
        }

        advertListView = findViewById(R.id.advertListView);
        advertArrayList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(AdvertList.this);

        List<Advert> advertList = db.getAllAdverts();
        System.out.print(advertList.get(0).toString());
        for (Advert advert :advertList)
        {
            advertArrayList.add(advert.getDate() + ": " + advert.getName()
                + "\n" + advert.getDescription()
            );
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, advertArrayList);
        advertListView.setAdapter(adapter);

        advertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent editAdvertIntent = new Intent(getApplicationContext(), AdvertForm.class);
                editAdvertIntent.putExtra("advert_id", advertList.get(i).getAdvert_id());
                editAdvertIntent.putExtra("post_type", advertList.get(i).getPost_Type());
                editAdvertIntent.putExtra("name", advertList.get(i).getName());
                editAdvertIntent.putExtra("phone", advertList.get(i).getPhone());
                editAdvertIntent.putExtra("description", advertList.get(i).getDescription());
                editAdvertIntent.putExtra("date", advertList.get(i).getDate());
                editAdvertIntent.putExtra("location", advertList.get(i).getLocation());

                startActivityForResult(editAdvertIntent, 1);
                finish();
            }
        });

    }
}