package com.dit.des.arproject.advanced;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.dit.des.arproject.BaseArActivity;
import com.dit.des.arproject.CurrentLocation;
import com.dit.des.arproject.GooglePlace;
import com.dit.des.arproject.SavedLocation;
import com.dit.des.arproject.util.AppData;
import com.orm.SugarRecord;
import com.wikitude.architect.ArchitectView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PoiDataFromAMExtension extends ArchitectViewExtension implements LocationListener{


    public PoiDataFromAMExtension(Activity activity, ArchitectView architectView) {
        super(activity, architectView);
    }

    /** If the POIs were already generated and sent to JavaScript. */
    private boolean injectedPois = false;

    /**
     * When the first location was received the POIs are generated and sent to the JavaScript code,
     * by using architectView.callJavascript.
     */
    @Override
    public void onLocationChanged(Location location) {
        if (!injectedPois) {
            final JSONArray jsonArray = BuildPoiInfo(location ,  activity.getIntent());
            architectView.callJavascript("World.loadPoisFromJsonData(" + jsonArray.toString() + ")"); // Triggers the loadPoisFromJsonData function
            injectedPois = true; // don't load pois again
        }
    }

    /**
     * The very basic LocationProvider setup of this sample app does not handle the following callbacks
     * to keep the sample app as small as possible. They should be used to handle changes in a production app.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    private static JSONArray BuildPoiInfo(final Location userLocation , Intent intent) {

        final JSONArray pois = new JSONArray();

        // ensure these attributes are also used in JavaScript when extracting POI data
        final String ATTR_ID = "id";
        final String ATTR_NAME = "name";
        final String ATTR_DESCRIPTION = "description";
        final String ATTR_LATITUDE = "latitude";
        final String ATTR_LONGITUDE = "longitude";
        final String ATTR_ALTITUDE = "altitude";
        String placeType = BaseArActivity.USER_PLACE;
        AppData appData;
        if (intent.hasExtra(BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE)) {
            appData = (AppData) intent.getSerializableExtra(BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE);
            placeType = appData.getDisplaytype();
        }




        if (placeType.equals(BaseArActivity.USER_PLACE)) {

            int count = (int) SugarRecord.count(SavedLocation.class);
            List<SavedLocation> locations = SavedLocation.listAll(SavedLocation.class);
            for (SavedLocation loc : locations) {
                // generate POIs

                final HashMap<String, String> poiInformation = new HashMap<String, String>();
                poiInformation.put(ATTR_ID, String.valueOf(loc.getUserId()));
                poiInformation.put(ATTR_NAME, loc.getName());
                poiInformation.put(ATTR_DESCRIPTION, loc.getDesc());
                //double[] poiLocationLatLon = getRandomLatLonNearby(userLocation.getLatitude(), userLocation.getLongitude());
                poiInformation.put(ATTR_LATITUDE, String.valueOf(loc.getLat()));
                poiInformation.put(ATTR_LONGITUDE, String.valueOf(loc.getLong()));
                //final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
                // Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
                poiInformation.put(ATTR_ALTITUDE, String.valueOf(loc.getAlt()));
                //poiInformation.put(ATTR_ALTITUDE, String.valueOf(Math.floor(Math.random() * 1001)));
                //poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
                pois.put(new JSONObject(poiInformation));

            }
        } else if (placeType.equals(BaseArActivity.GOOGLE_PLACE)){
            // Generate POI Information from Google Places stored in GooglePlace SQLite table

            int count = (int) SugarRecord.count(GooglePlace.class);
            List<GooglePlace> locations = GooglePlace.listAll(GooglePlace.class);
            for (GooglePlace loc : locations) {
                // generate POIs

                final HashMap<String, String> poiInformation = new HashMap<String, String>();
                poiInformation.put(ATTR_ID, String.valueOf(loc.getName()));
                poiInformation.put(ATTR_NAME, loc.getName());
                poiInformation.put(ATTR_DESCRIPTION, loc.getDesc());
                //double[] poiLocationLatLon = getRandomLatLonNearby(userLocation.getLatitude(), userLocation.getLongitude());
                poiInformation.put(ATTR_LATITUDE, String.valueOf(loc.getLat()));
                poiInformation.put(ATTR_LONGITUDE, String.valueOf(loc.getLong()));
                //final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
                // Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
                poiInformation.put(ATTR_ALTITUDE, String.valueOf(loc.getAlt()));
                //poiInformation.put(ATTR_ALTITUDE, String.valueOf(Math.floor(Math.random() * 1001)));
                //poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
                pois.put(new JSONObject(poiInformation));

            }
        } else if (placeType.equals(BaseArActivity.CURRENT_LOCATION)){
            int count = (int) SugarRecord.count(CurrentLocation.class);
            List<CurrentLocation> locations = CurrentLocation.listAll(CurrentLocation.class);
            for (CurrentLocation loc : locations) {
                // generate POIs

                final HashMap<String, String> poiInformation = new HashMap<String, String>();
                poiInformation.put(ATTR_ID, String.valueOf(loc.getUserId()));
                poiInformation.put(ATTR_NAME, loc.getName());
                poiInformation.put(ATTR_DESCRIPTION, loc.getDesc());
                //double[] poiLocationLatLon = getRandomLatLonNearby(userLocation.getLatitude(), userLocation.getLongitude());
                poiInformation.put(ATTR_LATITUDE, String.valueOf(loc.getLat()));
                poiInformation.put(ATTR_LONGITUDE, String.valueOf(loc.getLong()));
                //final float UNKNOWN_ALTITUDE = -32768f;  // equals "AR.CONST.UNKNOWN_ALTITUDE" in JavaScript (compare AR.GeoLocation specification)
                // Use "AR.CONST.UNKNOWN_ALTITUDE" to tell ARchitect that altitude of places should be on user level. Be aware to handle altitude properly in locationManager in case you use valid POI altitude value (e.g. pass altitude only if GPS accuracy is <7m).
                poiInformation.put(ATTR_ALTITUDE, String.valueOf(loc.getAlt()));
                //poiInformation.put(ATTR_ALTITUDE, String.valueOf(Math.floor(Math.random() * 1001)));
                //poiInformation.put(ATTR_ALTITUDE, String.valueOf(UNKNOWN_ALTITUDE));
                pois.put(new JSONObject(poiInformation));

            }

        }
        return pois;
    }

    private static double[] getRandomLatLonNearby(final double lat, final double lon) {
        return new double[]{lat + Math.random() / 5 - 0.1, lon + Math.random() / 5 - 0.1};
    }

}
