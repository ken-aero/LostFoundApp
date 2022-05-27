package com.example.lostfoundapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

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

    public void viewMapOnClick(View view) {
        Intent createMapIntent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(createMapIntent);
    }
}