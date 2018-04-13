package com.dit.des.arproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ARProjectLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    private static final String KEY_CURRENT_USER_EMAIL = "sessionUserEmail";
    private static final String KEY_CURRENT_USER_ID = "SessionUserId";

    private static final String KEY_CURRENT_LATITUDE = "latitude";
    private static final String KEY_CURRENT_LONGITUDE = "longitude";
    private static final String KEY_CURRENT_ALTITUDE = "altitude";

    private static final double DIT_LATITUDE = 53.337863;
    private static final double DIT_LONGITUDE = -6.268079;
    private static final double DIT_ALTITUDE = 13.0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setSessionUser(String user){
        editor.putString(KEY_CURRENT_USER_EMAIL, user);
        editor.commit();
    }

    public void setSessionUserId(int userId){
        editor.putInt(KEY_CURRENT_USER_ID, userId);
        editor.commit();
    }

    public int getSessionUserId(){
        return pref.getInt(KEY_CURRENT_USER_ID, 0);
    }

    public String getSessionUser(){
        return pref.getString(KEY_CURRENT_USER_EMAIL, "undefined");
    }

    public void setCurrentLatitude (double latitude) {
        editor.putLong ( KEY_CURRENT_LATITUDE , Double.doubleToRawLongBits(latitude));
        editor.commit();
    }

    public void setCurrentLongitude (double longitude) {
        editor.putLong ( KEY_CURRENT_LONGITUDE , Double.doubleToRawLongBits(longitude));
        editor.commit();
    }

    public void setCurrentAltitude (double altitude){
        editor.putLong ( KEY_CURRENT_ALTITUDE, Double.doubleToRawLongBits(altitude));
        editor.commit();
    }

    public double getCurrentLatitude () {
        return Double.longBitsToDouble(pref.getLong(KEY_CURRENT_LATITUDE, Double.doubleToLongBits(DIT_LATITUDE)));
    }

    public double getCurrentLongitude () {
        return Double.longBitsToDouble(pref.getLong(KEY_CURRENT_LONGITUDE, Double.doubleToLongBits(DIT_LONGITUDE)));

    }

    public double getCurrentAltitude() {
        return Double.longBitsToDouble(pref.getLong(KEY_CURRENT_ALTITUDE, 0));
    }

    //todo add in altitude
}