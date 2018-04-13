package com.dit.des.arproject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dit.des.arproject.util.AppData;

import static com.dit.des.arproject.BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE;

public class DistanceSelecter extends Activity implements TaskCompleted{


    private ProgressDialog pDialog;
    private SeekBar seek_bar;
    private TextView distanceTextView;
    private Button startARBtn;
    private int finalDistance;
    private String placeType;
    private final int MAX_DISTANCE = 10000;
    private final int MIN_DISTANCE = 100;
    private AppData appData;
    private double currentLat;
    private double currentLong;
    private double currentAlt;
    private final static String KEY_LOCATION = "location";
    private SessionManager session;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_selecter);

        final Intent intent = getIntent();

        appData = (AppData) intent.getSerializableExtra(INTENT_EXTRAS_KEY_SAMPLE);
        placeType = (String) intent.getSerializableExtra(BaseArActivity.PLACE_TYPE_KEY);

        session = new SessionManager(getApplicationContext());

        currentLat = session.getCurrentLatitude();
        currentLong = session.getCurrentLongitude();

        currentAlt = session.getCurrentAltitude();
        currentUserId = session.getSessionUserId();
        setSeek_bar();

        startARBtn = (Button) findViewById(R.id.openARButton);
        startARBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalDistance < 100){
                    finalDistance = 100;
                }
                Toast.makeText(getApplicationContext(),
                        "distance selected : " + finalDistance + " for " + placeType, Toast.LENGTH_SHORT)
                        .show();

                showNearbyPlaces(placeType, finalDistance);
            }
        });
    }

    public void setSeek_bar(){
        seek_bar = (SeekBar) findViewById(R.id.seekBar);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        String seekValue = MIN_DISTANCE/1000 + " Kilometers";
        distanceTextView.setText(seekValue);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int distanceValue;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        distanceValue = i * 100;

                        if(distanceValue == 0) {
                            distanceValue = MIN_DISTANCE;
                        }
                        String seekValue = distanceValue /1000 + " Kilometers";
                        distanceTextView.setText(seekValue);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if(distanceValue == 0) {
                            distanceValue = MIN_DISTANCE;
                        }
                        String seekValue = distanceValue /1000 + " Kilometers";
                        distanceTextView.setText(seekValue);
                        finalDistance = distanceValue;
                    }
                }
        );
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showNearbyPlaces(String placeType, int radius){

        // Get places from Google
        GoogleApiAdapter google = new GoogleApiAdapter(getApplicationContext());
        google.getNearbyPlaces(this, placeType, radius, currentLat, currentLong);
        pDialog.setMessage("Loading nearby places");

        showDialog();
       //ar view is loaded in onTaskComplete when ASync task is finished
    }

    @Override
    public void onTaskComplete(Integer result){
        hideDialog();
        // Show the Nearby Places in the AR View
        final Intent intent = new Intent(getApplicationContext(), appData.getActivityClass());
        intent.putExtra(BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE, appData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }
}
