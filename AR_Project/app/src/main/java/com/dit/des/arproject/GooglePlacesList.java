package com.dit.des.arproject;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dit.des.arproject.util.AppData;
import com.orm.SugarRecord;

import java.util.List;

import static com.dit.des.arproject.BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE;

public class GooglePlacesList extends ListActivity{

    private ListView placeTypesListView;

    private AppData appData;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_places_list);
        final Intent intent = getIntent();

        if (!intent.hasExtra(INTENT_EXTRAS_KEY_SAMPLE)) {
            throw new IllegalStateException(getClass().getSimpleName() +
                    " can not be created without valid AppData as intent extra for key " + INTENT_EXTRAS_KEY_SAMPLE + ".");
        }

        appData = (AppData) intent.getSerializableExtra(INTENT_EXTRAS_KEY_SAMPLE);


        placeTypesListView = (ListView) findViewById(android.R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, placeTypes);
        placeTypesListView.setAdapter(arrayAdapter);

    }

    @Override
    protected void onResume(){
        super.onResume();
        deleteAllRecords();
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){

        String selectedType;
        selectedType= (String) l.getItemAtPosition(position);
        Toast.makeText(getApplicationContext(),
                "selected : " + selectedType , Toast.LENGTH_SHORT)
                .show();

        Intent intent = new Intent (this, DistanceSelecter.class);
        intent.putExtra(BaseArActivity.PLACE_TYPE_KEY, selectedType);
        intent.putExtra(BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE, appData);
        startActivity(intent);
    }

    public static void deleteAllRecords() {
        List<GooglePlace> places = GooglePlace.listAll(GooglePlace.class);
        for (GooglePlace p : places){
            SugarRecord.deleteAll(p.getClass());
        }
    }

}
