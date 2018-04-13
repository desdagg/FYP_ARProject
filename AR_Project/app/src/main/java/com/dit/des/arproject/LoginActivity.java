
package com.dit.des.arproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



/*
import com.dit.des.loginandreg.app.AppConfig;
import com.dit.des.loginandreg.app.AppController;
*/

public class LoginActivity extends Activity implements TaskCompleted  {
    //private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;



    private String currentUser = "";
    //private SQLiteHandler db;

    private static final String TAG = "LOGIN_APP";





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        //db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());






        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take them to main activity
            Intent intent = new Intent(LoginActivity.this, LandingPageActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        int loginAccept = 0;

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        System.out.println("OUThh: userEmail: " + email + "password: " + password);
        pDialog.setMessage("Logging in ...");
        showDialog();
        currentUser = email;
        DBManager manager = new DBManager();
        try {
            manager.authenticateUser(this,email, password);

        }catch (Exception e){
            Log.e(TAG, "Exception " + e);
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
    public void onTaskComplete(Integer result) {
        //toast if user login details are incorrect
        if(result == 0){
            Toast.makeText(getApplicationContext(),
                    "credentials are incorrect", Toast.LENGTH_LONG)
                    .show();
            hideDialog();
            currentUser = "null";
        }

        //if log in details are correct
        if(result == 1){
            hideDialog();
            session.setLogin(true);
            session.setSessionUser(currentUser);
            Intent intent = new Intent(LoginActivity.this,
                    LandingPageActivity.class);
            startActivity(intent);
            finish();
        }
    }
//
//    public String getCurrentUser() {
//        return currentUser;
//    }
//
//    public void setCurrentUser(String currentUser) {
//        this.currentUser = currentUser;
//    }
}
