package com.dit.des.arproject.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.HashMap;
import android.util.Log;

import com.dit.des.arproject.GooglePlace;
import com.dit.des.arproject.UserData;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PlaceDataParser {

    /**
     * Reference for getPlacesFromServer method
     * https://github.com/priyankapakhale/GoogleMapsNearbyPlacesDemo/tree/master/app/src/main/java/com/example/priyanka/mapsdemo
     *
     */

    private static final String TAG = PlaceDataParser.class.getSimpleName();

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {

        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "Umkmown";
        String vicinity= "Umkmown";
        String latitude= "";
        String longitude="";
        String reference="";

        Log.d(TAG,"jsonobject ="+googlePlaceJson.toString());
        int count;

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }

            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);

            GooglePlace thePlace = new GooglePlace("point_of_interest", placeName, Double.parseDouble(latitude), Double.parseDouble(longitude), 13, vicinity, vicinity);
            thePlace.save();

            count = (int)SugarRecord.count(GooglePlace.class);
            String outputString =
                    String.format(
                            "Data row = (%s, %s, %s, %s  )",
                            placeName , latitude, longitude , vicinity);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;

    }
    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> placelist = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    public List<HashMap<String, String>> parseJasonString(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }
}
