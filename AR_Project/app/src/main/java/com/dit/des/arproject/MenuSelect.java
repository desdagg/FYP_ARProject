package com.dit.des.arproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dit.des.arproject.advanced.ArchitectViewExtensionActivity;
import com.dit.des.arproject.util.AppData;
import com.dit.des.arproject.util.PermissionUtil;
import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;
import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;
import com.wikitude.common.camera.CameraSettings;
import com.wikitude.common.permission.PermissionManager;

import java.util.Arrays;
import java.util.List;

public class MenuSelect extends Activity implements TaskCompleted{


    private static final String EXTENSION_APPLICATION_MODEL_POIS = "application_model_pois";
    private static final String EXTENSION_GEO = "geo";
    private static final String EXTENSION_NATIVE_DETAIL = "native_deteil";


    private Button btn_userPlaces;
    private Button btn_addUserPlace;
    private Button btn_testGoogle;
    private Button btn_ShowUsers;
    private Button btn_ShowMyPlaces;
    //private final AppData sampleData;

    private final PermissionManager permissionManager = ArchitectView.getPermissionManager();
    private SessionManager session;
    private String currentUserLoggedIn;
    private int testCase = 3;    //  1 = Presenting POI  2 = App Module &  3 = Native


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        //clear the local database
        //clearSQLiteDB();

        btn_userPlaces = (Button) findViewById(R.id.btn_userPlaces);
        btn_userPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserPlaces();
            }
        });
        btn_addUserPlace = (Button) findViewById(R.id.btn_addUserPlace);
        btn_addUserPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUerPlace();  // Test mode
            }
        });
        btn_testGoogle = (Button) findViewById(R.id.btn_testGoogle);
        btn_testGoogle.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              showNearbyPlaces();
          }
          });
        btn_ShowMyPlaces = (Button) findViewById(R.id.btnShowMyPlaces);
        btn_ShowMyPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "selected ", Toast.LENGTH_SHORT)
                        .show();
                showMyPlaces();
            }
        });

        btn_ShowUsers = (Button) findViewById(R.id.btnShowUsers);
        btn_ShowUsers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                showLoggedInUsers();
            }
        });

        // Session manager
        session = new SessionManager(getApplicationContext());

        currentUserLoggedIn = session.getSessionUser();
    }


    public void showMyPlaces(){

        DBManager manager = new DBManager();
        manager.getUserLocations(this,session.getSessionUserId());
    }

    public void showLoggedInUsers(){
        final AppData appData = configureAppData( BaseArActivity.CURRENT_LOCATION);
        launchARView(appData);

    }


    public void openUserPlaces(){

        final AppData appData = configureAppData(BaseArActivity.USER_PLACE);
        Intent intent = new Intent(getApplicationContext(),
                UserPlacesList.class);
        intent.putExtra(BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE, appData);
        startActivity(intent);
    }


    private void addUerPlace(){

        Intent intent = new Intent(getApplicationContext(),
                AddUserLocationActivity.class);
        startActivity(intent);
    }


    private void showNearbyPlaces(){

        // Show the Nearby Places in the AR View
        final AppData appData = configureAppData( BaseArActivity.GOOGLE_PLACE);
        final Intent intent = new Intent(getApplicationContext(), GooglePlacesList.class);
        intent.putExtra(BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE, appData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void launchARView(final AppData appData)
    {
        // Check permissions
        final String[] permissions = PermissionUtil.getPermissionsForArFeatures(appData.getArFeatures());

        permissionManager.checkPermissions(MenuSelect.this, permissions, PermissionManager.WIKITUDE_PERMISSION_REQUEST, new PermissionManager.PermissionManagerCallback() {
            @Override
            public void permissionsGranted(int requestCode) {
                final Intent intent = new Intent(MenuSelect.this, appData.getActivityClass());
                intent.putExtra(BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE, appData);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void permissionsDenied(@NonNull String[] deniedPermissions) {
                Toast.makeText(MenuSelect.this, getString(R.string.permissions_denied) + Arrays.toString(deniedPermissions), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showPermissionRationale(final int requestCode, @NonNull String[] strings) {
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MenuSelect.this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(R.string.permission_rationale_title);
                alertBuilder.setMessage(getString(R.string.permission_rationale_text) + Arrays.toString(permissions));
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionManager.positiveRationaleResult(requestCode, permissions);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
    }

    private AppData configureAppData(String displayType){

        final String name, path ;
        final Class activityClass ;
        final List<String> extensions;
        final int features;
        final CameraSettings.CameraPosition cameraPosition;
        final CameraSettings.CameraResolution cameraResolution;

        name = "Native";
        path = "NativeDetailScreen/index.html";
        activityClass = ArchitectViewExtensionActivity.class;
        extensions = Arrays.asList(EXTENSION_APPLICATION_MODEL_POIS, EXTENSION_GEO, EXTENSION_NATIVE_DETAIL);

        features = ArchitectStartupConfiguration.Features.Geo;
        cameraPosition = CameraSettings.CameraPosition.BACK;
        cameraResolution = CameraSettings.CameraResolution.AUTO;

        AppData data = new AppData.Builder(name, path)
                .activityClass(activityClass)
                .extensions(extensions)
                .arFeatures(features)
                .cameraPosition(cameraPosition)
                .cameraResolution(cameraResolution)
                .camera2Enabled(false)
                .displayType(displayType)
                .build();

        return data;
    }


    //probably not needed here
    private void clearSQLiteDB() {
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());

    }

    @Override
    public void onTaskComplete(Integer result) {
        final AppData appData = configureAppData( BaseArActivity.USER_PLACE);

        launchARView(appData);
    }

}
