package com.example.lostfoundapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.lostfoundapp.data.DatabaseHelper;
import com.example.lostfoundapp.model.Advert;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lostfoundapp.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ArrayList<String> advertArrayList;
    List<Advert> advertList;
    RadioButton advertRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        advertArrayList = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper(MapsActivity.this);
        advertList = db.getAllAdverts();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (advertList.size() > 0) {
            int num = 1;
            for (Advert advert : advertList) {

                String name = advert.getName().split(" - ")[0];
                System.out.println(name);

                String title = advert.getDate() + ": " + advert.getName();
                LatLng loc = new LatLng(Double.parseDouble(advert.getLocation_lat()), Double.parseDouble(advert.getLocation_lng()));
                mMap.addMarker(new MarkerOptions().position(loc).title(title));
                if (num == 1) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12));
                    num++;
                }
            }
        } else {
            // set default loc as Sydney
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));
            Toast.makeText(MapsActivity.this, "No lost and found items!", Toast.LENGTH_LONG).show();
        }
    }
}