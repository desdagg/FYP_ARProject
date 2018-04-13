package com.dit.des.arproject;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dit.des.arproject.util.AppData;
import com.orm.SugarRecord;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

import static com.dit.des.arproject.BaseArActivity.INTENT_EXTRAS_KEY_SAMPLE;
import static com.google.vr.dynamite.client.Version.TAG;

public class UserPlacesList extends ListActivity implements TaskCompleted{

    private ListView usersListView;
    private SessionManager session;
    private String currentUser;
    private ProgressDialog pDialog;
    private String selectedUser;
    private int isFirstOpen = 0;
    private AppData appData;


    HashMap<String, Integer> hmap = new HashMap<String, Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userplaces_list);
        final Intent intent = getIntent();

        if (!intent.hasExtra(INTENT_EXTRAS_KEY_SAMPLE)) {
            throw new IllegalStateException(getClass().getSimpleName() +
                    " can not be created without valid AppData as intent extra for key " + INTENT_EXTRAS_KEY_SAMPLE + ".");
        }

        appData = (AppData) intent.getSerializableExtra(INTENT_EXTRAS_KEY_SAMPLE);


        session = new SessionManager(getApplicationContext());
        currentUser = session.getSessionUser();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //remove any old data from the hashmap
        hmap.clear();
        //populate the user list sqlite databse


        List<String> usersArrayList = new ArrayList<>();

        List<UserData> users;
         users = UserData.listAll(UserData.class);
        for(UserData u : users){
            usersArrayList.add(u.getName());
            hmap.put(u.getName(),u.getUserId());
        }
        usersListView = (ListView) findViewById(android.R.id.list);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, usersArrayList);
        usersListView.setAdapter(arrayAdapter);
        //Select.from(UserData.class).where
        //isFirstOpen = 0;
        // #delete all Saved Locations : Commented out during testing

        deleteAllRecords(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(isFirstOpen != 0){
            int idToRemove = hmap.get(selectedUser);
        }

        //UserData.executeQuery("select id from users where name = '" + selectedUser + "';");
        // #delete all Saved Locations : Commented out during testing
        deleteAllRecords(this);
        //remove selected user locations from the local DB
        //set the selected user to blank
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){

        selectedUser= (String) l.getItemAtPosition(position);
        Toast.makeText(getApplicationContext(),
                "selected : " + selectedUser + " with id " + hmap.get(selectedUser), Toast.LENGTH_SHORT)
                .show();
        pDialog.setMessage("Loading " + selectedUser + "'s places");
        isFirstOpen = 1;
        showDialog();
        DBManager manager = new DBManager();
        try {
            manager.getUserLocations(this,hmap.get(selectedUser));
        }catch (Exception e){
            Log.e(TAG, "Exception " + e);
        }
    }


    private void launchARView()
    {
        final Intent intent = new Intent(UserPlacesList.this, appData.getActivityClass());
        intent.putExtra(INTENT_EXTRAS_KEY_SAMPLE, appData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    public static void deleteAllRecords(Context applicationContext) {
        List<SavedLocation> locations = SavedLocation.listAll(SavedLocation.class);
        for (SavedLocation l : locations){
            SugarRecord.deleteAll(l.getClass());
        }
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onTaskComplete(Integer result){
        hideDialog();
        launchARView();
        int locCount = 0;
        List<SavedLocation> locations = SavedLocation.listAll(SavedLocation.class);
        for (SavedLocation l : locations){
            locCount++;
        }

        Toast.makeText(getApplicationContext(),
                "selected : " + locCount + " locations", Toast.LENGTH_SHORT)
                .show();
    }

}
