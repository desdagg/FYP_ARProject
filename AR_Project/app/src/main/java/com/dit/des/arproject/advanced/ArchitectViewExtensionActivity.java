package com.dit.des.arproject.advanced;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dit.des.arproject.util.AppData;
import com.wikitude.architect.ArchitectView;
import com.dit.des.arproject.BaseArActivity;

/*
import com.dit.des.arproject.advanced.plugins.MarkerTrackingPluginExtension;
import com.dit.des.arproject.advanced.plugins.QrPluginExtension;
import com.dit.des.arproject.advanced.plugins.input.CustomCameraExtension;
import com.dit.des.arproject.advanced.plugins.input.SimpleInputPluginExtension;
*/

import java.util.HashMap;
import java.util.Map;

public class ArchitectViewExtensionActivity extends BaseArActivity {

    private static final String EXTENSION_APPLICATION_MODEL_POIS = "application_model_pois";
    private static final String EXTENSION_GEO = "geo";
    private static final String EXTENSION_NATIVE_DETAIL = "native_detail";
    private static final String EXTENSION_SCREENSHOT = "screenshot";
    private static final String EXTENSION_CUSTOM_CAMERA = "custom_camera";
    private static final String EXTENSION_SIMPLE_INPUT = "simple_input";
    private static final String EXTENSION_FACE_DETECTION = "face_detection";
    private static final String EXTENSION_QR_CODE = "qr_code";
    private static final String EXTENSION_MARKER_TRACKING = "marker_tracking";

    private final Map<String, ArchitectViewExtension> extensions = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        AppData appData = (AppData) intent.getSerializableExtra(BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE);

        for (String extension : appData.getExtensions()) {
            switch (extension) {
                case EXTENSION_GEO:
                    extensions.put(EXTENSION_GEO, new GeoExtension(this, architectView));
                    break;

                case EXTENSION_APPLICATION_MODEL_POIS:
                    extensions.put(EXTENSION_APPLICATION_MODEL_POIS, new PoiDataFromAMExtension(this, architectView));
                    break;


                case EXTENSION_NATIVE_DETAIL:
                    extensions.put(EXTENSION_NATIVE_DETAIL, new NativePoiDetailExtension(this, architectView));
                    break;
            }
        }

        if (extensions.containsKey(EXTENSION_GEO) && extensions.containsKey(EXTENSION_APPLICATION_MODEL_POIS)) {
            PoiDataFromAMExtension applicationModelExtension = (PoiDataFromAMExtension) extensions.get(EXTENSION_APPLICATION_MODEL_POIS);
            GeoExtension geoExtension = (GeoExtension) extensions.get(EXTENSION_GEO);
            geoExtension.setLocationListenerExtension(applicationModelExtension);
        }

        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onCreate();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onPostCreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onResume();
        }
    }

    @Override
    protected void onPause() {
        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onPause();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        for (ArchitectViewExtension extension : extensions.values()) {
            extension.onDestroy();
        }
        extensions.clear();

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArchitectView.getPermissionManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
