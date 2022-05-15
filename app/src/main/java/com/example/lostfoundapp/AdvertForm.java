package com.example.lostfoundapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.lostfoundapp.data.DatabaseHelper;
import com.example.lostfoundapp.model.Advert;

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
    int advert_idIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_form);

        advert_idIntent = 0;
        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        descriptionEditText = findViewById(R.id.editTextDesc);
        dateEditText = findViewById(R.id.editTextDate);
        locationEditText = findViewById(R.id.editTextLocation);
        formButton = findViewById(R.id.formActionBtn);

        advertRadioGroup = findViewById(R.id.radio_group);
        // initial value
        int advertRadioButtonID = advertRadioGroup.getCheckedRadioButtonId();
        advertRadioButton = advertRadioGroup.findViewById(advertRadioButtonID);

        Intent intent = getIntent();
        advert_idIntent = intent.getIntExtra("advert_id", 0);
        if (advert_idIntent > 0) { // edit form
            formButton.setText("REMOVE");
            int post_typeIntent = intent.getIntExtra("post_type", 0);

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
        }

        // FORM ACTION
        db = new DatabaseHelper(this);
        formButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formButton.getText() == "SAVE") {
                    int post_type = advertRadioButton.getId();
                    String name = advertRadioButton.getText() + " " + nameEditText.getText().toString();
                    String phone = phoneEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    String date = dateEditText.getText().toString();
                    String location = locationEditText.getText().toString();

                    long result = db.insertAdvert(new Advert(post_type, name, phone, description, date, location));
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
}