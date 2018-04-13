package com.dit.des.arproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

public class WelcomeActivity extends Activity implements TaskCompleted{

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnOpenApp;

    //private SQLiteHandler db;
    private SessionManager session;
    private String currentUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnOpenApp = (Button) findViewById(R.id.btnOpenApp);

        // Session manager
        session = new SessionManager(getApplicationContext());

        currentUserLoggedIn = session.getSessionUser();
//        if(savedInstanceState == null){
//            Bundle extras = getIntent().getExtras();
//            if(extras == null){
//                currentUserLoggedIn = "error";
//                System.out.println("error with the get extras");
//            }else{
//                currentUserLoggedIn = extras.getString("CURRENT_USER");
//            }
//        }else{
//            currentUserLoggedIn = (String) savedInstanceState.getSerializable("CURRENT_USER");
//        }

        // SqLite database handler
        //db = new SQLiteHandler(getApplicationContext());

        // session manager
        //session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Displaying the user details on the screen
        txtName.setText(currentUserLoggedIn);
        //txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnOpenApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear the local database
                clearSQLiteDB();
                openApp();
            }
        });
    }


    private void openApp(){
        //add the list of users to the local database
        DBManager manager = new DBManager();
        manager.getUsers(this, currentUserLoggedIn);
        manager.getLoggedInUsersLocation(this, currentUserLoggedIn);

        Toast.makeText(getApplicationContext(),
                "App should open", Toast.LENGTH_LONG)
                .show();

        //uncomment for wikitude
        //Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(getApplicationContext(), MenuSelect.class);
        startActivity(intent);
        //finish();
    }


    private void clearSQLiteDB() {
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        //update database to show user is logged out
        DBManager manager = new DBManager();
        manager.logoutUser(getApplicationContext(), currentUserLoggedIn, session.getSessionUserId());

        // Launching the login activity
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTaskComplete(Integer result) {

        //return 1;
    }
}
