package com.dit.des.arproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class AddUserLocationActivity extends AppCompatActivity implements OnItemSelectedListener{

    private Button btnAddLocation;
    private EditText place_name;
    private EditText input_altitude;

    private final String[] placeTypes = {"accounting","airport","amusement_park","aquarium","art_gallery",
            "atm","bakery","bank","bar","beauty_salon","bicycle_store","book_store","bowling_alley",
            "bus_station","cafe","campground","car_dealer","car_rental","car_repair","car_wash",
            "casino","cemetery","church","city_hall","clothing_store","convenience_store","courthouse",
            "dentist","department_store","doctor","electrician","electronics_store","embassy",
            "fire_station","florist","funeral_home","furniture_store","gas_station","gym","hair_care",
            "hardware_store","hindu_temple","home_goods_store","hospital","insurance_agency",
            "jewelry_store","laundry","lawyer","library","liquor_store","local_government_office",
            "locksmith","lodging","meal_delivery","meal_takeaway","mosque","movie_rental","movie_theater",
            "moving_company","museum","night_club","painter","park","parking","pet_store","pharmacy",
            "physiotherapist","plumber","police","post_office","real_estate_agency","restaurant",
            "roofing_contractor","rv_park","school","shoe_store","shopping_mall","spa","stadium",
            "storage","store","subway_station","supermarket","synagogue","taxi_stand","train_station",
            "transit_station","travel_agency","veterinary_care","zoo"};

    private String description;
    private String currentUserId;
    private String locationName;
    private String latitude, longitude, altitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SessionManager session = new SessionManager(this);
        currentUserId = Integer.toString(session.getSessionUserId());

        //todo get current location...

        place_name = (EditText) findViewById(R.id.place_name);
        input_altitude = (EditText) findViewById(R.id.input_altitude);
        btnAddLocation = (Button) findViewById(R.id.btnAddLocation);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationName = place_name.getText().toString();
                altitude = input_altitude.getText().toString();

                latitude = Double.toString(session.getCurrentLatitude());
                longitude = Double.toString(session.getCurrentLongitude());

                DBManager manager = new DBManager();
                manager.addUserLocation(getApplicationContext(),currentUserId, latitude, longitude, altitude, locationName, description);
                finish();
            }
        });

        // Spinner element
       Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.addAll(Arrays.asList(placeTypes));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        description = spinner.getSelectedItem().toString();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        description = item;
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }





    }
