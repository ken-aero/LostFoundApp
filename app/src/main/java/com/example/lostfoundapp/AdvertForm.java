package com.example.lostfoundapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lostfoundapp.data.DatabaseHelper;
import com.example.lostfoundapp.model.Advert;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AdvertForm extends AppCompatActivity {

    DatabaseHelper db;
    EditText nameEditText;
    EditText phoneEditText;
    EditText descriptionEditText;
    EditText dateEditText;
    EditText locationEditText;
    RadioGroup advertRadioGroup;
    RadioButton advertRadioButton;
    Button formButton;
    Button locButton;
    int advert_idIntent;
    private static final String TAG = "ERROR";
    String placesLocation;
    double placesLat;
    double placesLong;
    LocationManager locationManager;
    LocationListener locationListener;
    double locLat;
    double locLng;

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_form);

        LinearLayout llSearchPlaces = findViewById(R.id.llSearchPlacement);

        advert_idIntent = 0;
        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        descriptionEditText = findViewById(R.id.editTextDesc);
        dateEditText = findViewById(R.id.editTextDate);
        locationEditText = findViewById(R.id.editTextLocation);
        formButton = findViewById(R.id.formActionBtn);
        locButton = findViewById(R.id.getLocationBtn);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                locLat = location.getLatitude();
                locLng = location.getLongitude();
            }
        };

        advertRadioGroup = findViewById(R.id.radio_group);
        // initial value
        int advertRadioButtonID = advertRadioGroup.getCheckedRadioButtonId();
        advertRadioButton = advertRadioGroup.findViewById(advertRadioButtonID);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        Intent intent = getIntent();
        advert_idIntent = intent.getIntExtra("advert_id", 0);
        if (advert_idIntent > 0) { // view record form
            formButton.setText("REMOVE");
            int post_typeIntent = intent.getIntExtra("post_type", 0);

            // hide places and location button for read only view
            llSearchPlaces.setVisibility(View.GONE);
            locButton.setVisibility(View.GONE);

            advertRadioButton.setEnabled(false);
            advertRadioButton = advertRadioGroup.findViewById(post_typeIntent);
            advertRadioButton.setChecked(true);
            advertRadioButton.setEnabled(false);

            String nameIntentText = intent.getStringExtra("name");
            nameEditText.setText(nameIntentText);
            nameEditText.setEnabled(false);

            String phoneIntentText = intent.getStringExtra("phone");
            phoneEditText.setText(phoneIntentText);
            phoneEditText.setEnabled(false);

            String descIntentText = intent.getStringExtra("description");
            descriptionEditText.setText(descIntentText);
            descriptionEditText.setEnabled(false);

            String dateIntentText = intent.getStringExtra("date");
            dateEditText.setText(dateIntentText);
            dateEditText.setEnabled(false);

            String locIntentText = intent.getStringExtra("location");
            locationEditText.setText(locIntentText);
            locationEditText.setEnabled(false);

        } else { // save form
            formButton.setText("SAVE");
            advertRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    advertRadioButton = findViewById(i);
                }
            });

            // hide location edit text for new records.
            locationEditText.setVisibility(View.GONE);

            // Initialize the SDK for Google Places API
            Places.initialize(getApplicationContext(), "PUT_MAPS_API_KEY_HERE");

            // Specify the types of place data to return.
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
            autocompleteFragment.setHint("Search Location");
            autocompleteFragment.setCountry("AU");
            autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

            locButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ActivityCompat.checkSelfPermission(AdvertForm.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(AdvertForm.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, (LocationListener) locationListener);

                    Criteria criteria = new Criteria();
                    String bestProvider = locationManager.getBestProvider(criteria, true);
                    Location location = locationManager.getLastKnownLocation(bestProvider);

                    if (location == null) {
                        Toast.makeText(getApplicationContext(), "GPS signal not found", Toast.LENGTH_SHORT).show();
                    }
                    if (location != null) {
                        String textAddress = onLocationChanged(location);
                        autocompleteFragment.setText(textAddress);
                        placesLocation = textAddress;
                        placesLat = location.getLatitude();
                        placesLong = location.getLongitude();
                    }
                }
            });

            // Set up a PlaceSelectionListener to handle the response.
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    placesLocation = place.getAddress();
                    placesLat = place.getLatLng().latitude;
                    placesLong = place.getLatLng().longitude;
                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }

        // FORM ACTION
        db = new DatabaseHelper(this);
        formButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formButton.getText() == "SAVE") {
                    int post_type = advertRadioButton.getId();
                    String name = advertRadioButton.getText() + " - " + nameEditText.getText().toString();
                    String phone = phoneEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String date = dateEditText.getText().toString();
                    String location = placesLocation;

                    long result = db.insertAdvert(new Advert(post_type, name, phone,
                            description, date, location, Double.toString(placesLat), Double.toString(placesLong)));
                    if (result > 0)
                    {
                        Intent createAdvertIntent = new Intent(getApplicationContext(), MainActivity.class);
                        createAdvertIntent.putExtra("success", true);
                        startActivityForResult(createAdvertIntent, 1);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(AdvertForm.this, "Error creating advert!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Boolean result = db.deleteAdvert(advert_idIntent);
                    if (result) {
                        Intent removeAdvertIntent = new Intent(getApplicationContext(), AdvertList.class);
                        removeAdvertIntent.putExtra("success", true);
                        startActivityForResult(removeAdvertIntent, 1);
                        finish();
                    } else {
                        Toast.makeText(AdvertForm.this, "Error removing advert!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public String onLocationChanged(Location location) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        locLat = location.getLatitude();
        locLng = location.getLongitude();
        String textAddress = "";

        Log.e("latitude", "latitude--" + locLat);
        try {
            Log.e("latitude", "inside latitude--" + locLat);
            addresses = geocoder.getFromLocation(locLat, locLng, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                textAddress = address + " , " + postalCode;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return textAddress;
    }
}