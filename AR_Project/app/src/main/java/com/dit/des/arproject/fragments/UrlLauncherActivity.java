package com.dit.des.arproject.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dit.des.arproject.util.AppData;
import com.wikitude.architect.ArchitectStartupConfiguration;
import com.dit.des.arproject.R;

/**
 * Activity used to start AR Fragments.
 */
public class UrlLauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_url_launcher);

        // AppData is created in the UrlLauncherStorageActivity.
        final AppData appData = (AppData) getIntent().getSerializableExtra(SimpleArFragment.INTENT_EXTRAS_KEY_SAMPLE);

        // Selects fragment type based on if Geo AR is used.
        Fragment fragment;
        if ((appData.getArFeatures() & ArchitectStartupConfiguration.Features.Geo) == ArchitectStartupConfiguration.Features.Geo) {
            fragment = new SimpleGeoArFragment();
        } else {
            fragment = new SimpleArFragment();
        }

        // Adds the AppData to Fragment arguments.
        final Bundle args = new Bundle();
        args.putSerializable(SimpleArFragment.INTENT_EXTRAS_KEY_SAMPLE, appData);
        fragment.setArguments(args);

        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFragement, fragment);
        fragmentTransaction.commit();
    }

}
