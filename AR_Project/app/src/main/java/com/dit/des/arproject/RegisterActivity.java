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


public class RegisterActivity extends Activity implements TaskCompleted{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if(!email.contains("@")){
                    Toast.makeText(getApplicationContext(),
                            "Please enter a valid email address.", Toast.LENGTH_LONG)
                            .show();
                }else {

                    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        registerUser(name, email, password);

                        DBManager manager = new DBManager();
                        try {
                            manager.registerUser(RegisterActivity.this, name, email, password);
                        } catch (Exception e) {
                            Log.e(TAG, "Exception " + e);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */




    @Override
    public void onTaskComplete(Integer result) {
        //tif result is 1 then they are registered , open toast and return to login
        if(result == 1){
            Toast.makeText(getApplicationContext(),
                    "Your account has been created", Toast.LENGTH_LONG)
                    .show();
            // Launching the login activity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if(result == 0){
            Toast.makeText(getApplicationContext(),
                    "Account already exists with this email or username", Toast.LENGTH_LONG)
                    .show();
        }
    }
    
    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

}
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
