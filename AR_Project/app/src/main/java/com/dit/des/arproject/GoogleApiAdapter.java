package com.dit.des.arproject;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dit.des.arproject.util.PlaceDataParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.orm.SugarRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;



class GoogleApiAdapter {

    static final String NEARBY_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    static final String GOOGLE_API_KEY = "AIzaSyD3bbxqttFPogngHY5IEte6OdWbLs6euNc";


    static final int PROXIMITY_RADIUS = 10000;
    private static double TEST_LAT = 53.291049;
    private static double TEST_LONG = -6.219579;
    protected Context context;  // Used to display Toasts during testing

    private Location currentLocation;

    public GoogleApiAdapter(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public GoogleApiAdapter(Context context){this.context = context;};

    public Location getCurrentLocation() {
        return currentLocation;
    }



    private String buildUrl(String placeType, int radius, double latitude, double longitude) {

        StringBuilder googlePlaceUrl = new StringBuilder(NEARBY_PLACES_URL);
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + radius);
        googlePlaceUrl.append("&type=" + placeType);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + GOOGLE_API_KEY);

        Log.d("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    public void getNearbyPlaces(Context context, String placeType, int radius, double latitude, double longitude) {
        String url;
        Object dataTransfer[] = new Object[2];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(context);

        // Build the URL
        url = buildUrl(placeType, radius, latitude, longitude);
        dataTransfer[0] = url;

        getNearbyPlacesData.execute(dataTransfer);


    }


    class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

        private String googlePlacesData;
        private String url;
        private Context context;
        private TaskCompleted mCallback;

        public GetNearbyPlacesData(Context context){
            this.context = context;
            this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected String doInBackground(Object... objects) {
            this.url = (String) objects[0];

            try {
                googlePlacesData = getPlacesFromServer(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return googlePlacesData;
        }


        @Override
        protected void onPostExecute(String s) {

            List<HashMap<String, String>> nearbyPlaceList;
            PlaceDataParser parser = new PlaceDataParser();
            nearbyPlaceList = parser.parseJasonString(s);

            Log.d("nearbyplacesdata", "called parse method");
            //showNearbyPlaces();
            mCallback.onTaskComplete(1);

        }

        private void showNearbyPlaces() {

            // TODO  Get the GooglePlace records to an AR View
            int count = (int) SugarRecord.count(GooglePlace.class);
            Log.d("nearbyplacesdata", count + " places found");
            Toast.makeText( context, count + " places found", Toast.LENGTH_SHORT).show();
        }

        /**
         * Reference for getPlacesFromServer method
         * https://github.com/priyankapakhale/GoogleMapsNearbyPlacesDemo/tree/master/app/src/main/java/com/example/priyanka/mapsdemo
         *
         */
        private String getPlacesFromServer(String myUrl) throws IOException {
            String data = "";
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(myUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                inputStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();
                br.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
                urlConnection.disconnect();
            }
            Log.d("DownloadURL", "Returning data= " + data);

            return data;
        }

    }
}