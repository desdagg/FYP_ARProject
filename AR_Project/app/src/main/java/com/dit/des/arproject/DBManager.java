package com.dit.des.arproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.orm.SugarRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class DBManager {
    private static final String TAG = "BASIC_APP";

    protected Connection connection = null;

    public int authenticateUser(Context context, String userName , String password){
        // Start an AsynchTask to execute the authentication
        Integer result = 6;
        AuthUser authUser =  new AuthUser(context);
        authUser.execute(userName, password);

        return result;
    }

    public int registerUser(Context context, String userName, String email, String password){
        //start AsyncTask to execute register
        Integer result = 5;
        RegisterUser register = new RegisterUser(context);
        register.execute(userName,email,password);

        return result;
    }

    public void logoutUser(Context context, String user, int userId){

        LogoutUser logoutUser = new LogoutUser(context);
        logoutUser.execute(user, Integer.toString(userId));
    }

    public int getUsers(Context context, String user){

        GetUsers getUsers = new GetUsers(context);
        getUsers.execute(user);
        return 0;
    }

    public int getUserLocations(Context context, int userId){

        GetUserLocations getUserLocations = new GetUserLocations(context);
        String userIdString = Integer.toString(userId);
        getUserLocations.execute(userIdString);
        return 0;
    }

    public int getLoggedInUsersLocation(Context context, String user){

        GetLoggedInUsersLocation getLocation = new GetLoggedInUsersLocation(context);
        getLocation.execute(user);
        return 0;
    }

    public int addUserLocation(Context context, String id, String latitude, String longitude, String altitude, String name, String description){
        AddUserLocation addUserLocation = new AddUserLocation(context);
        addUserLocation.execute(id,latitude,longitude,altitude,name,description);
        return 0;
    }

    public int updateUserCurrentLocation(Context context, int id, double latitude, double longitude, double altitude, String userActivity){

        UpdateUserCurrentLocation updateUserCurrentLocation = new UpdateUserCurrentLocation(context);
        updateUserCurrentLocation.execute(Integer.toString(id), Double.toString(latitude), Double.toString(longitude), Double.toString(altitude), userActivity);

        return 0;
    }




    class Worker extends AsyncTask<String,Void , Integer> {
        protected Context mContext;
        protected TaskCompleted mCallback;

        protected Worker (){
        }

        //constructor
        public Worker (Context context){
            this.mContext = context;
            this.mCallback = (TaskCompleted) context;
        }


        protected Integer doInBackground(String... params) {
            // Initialize connection variables.
            System.out.println("DoInBackground called in base class");
            return 3;
        }

        protected Integer connectDB() {
            // Initialize connection variables.
            String host = "fyp-dbserver.mysql.database.azure.com";
            String database = "descover_db";
            String user = "des_root@fyp-dbserver";
            String password = "Desmond1";

            // check that the driver is installed
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC driver detected in library path.");
                //throw new ClassNotFoundException("MySQL JDBC driver NOT detected in library path.", e);
            }

            System.out.println("MySQL JDBC driver detected in library path.");


            // Initialize connection object
            try {
                String url = String.format("jdbc:mysql://%s/%s", host, database);

                // Set connection properties.
                Properties properties = new Properties();
                properties.setProperty("user", user);
                properties.setProperty("password", password);
                properties.setProperty("useSSL", "true");
                properties.setProperty("verifyServerCertificate", "true");
                properties.setProperty("requireSSL", "false");

                // get connection
                connection = DriverManager.getConnection(url, properties);
            } catch (SQLException e) {
                Log.e(TAG, "Exception  " + e);
                //throw new SQLException("Failed to create connection to database.", e);
            }
            return 4;
        }
    }



    class AuthUser extends Worker {

        public AuthUser(Context context){
            this.mContext = context;
            this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            super.doInBackground();
            this.connectDB();

            String emailIn = params[0];
            String pWordIn = params[1];
            System.out.println("params   " + emailIn + " " + pWordIn);

            if (connection != null) {
                System.out.println("Successfully created connection to database.");
                int userId;
                // Perform some SQL queries over the connection.
                try {
                    //create sql statement using connection
                    Statement statement = connection.createStatement();
                    String query = "SELECT email ,id, encrypted_password FROM users where email = '" + emailIn + "';";
                    System.out.println("Query: " + query);
                    try {
                        ResultSet rs = statement.executeQuery(query);
                        while(rs.next()){
                            userId = rs.getInt("id");
                            String queryPassword = rs.getString("encrypted_password");
                            String queryEmail = rs.getString("email");
                            System.out.println("query email   " + queryEmail);
                            //if the email doesn't exist
                            if(queryEmail == null || queryEmail.isEmpty()){
                                System.out.println("Email is incorrect");
                                return 0;
                            }
                            //if the passwords are not the same
                            if(!queryPassword.equals(pWordIn)){
                                return 0;
                            }
                            //update db to show user is currently logged in
                            String queryLoggedIn = "update users set loggedIn = 1 where email = '" + emailIn + "';";
                            String setUserInCurrentLocation = "insert into usercurrentlocation (user_id, user_lat, user_long, user_alt, user_status) values (" + userId +", 0.0, 0.0, 0.0, 'standing');";
                            statement.executeUpdate(queryLoggedIn);
                            statement.executeUpdate(setUserInCurrentLocation);
                            SessionManager session = new SessionManager(mContext);
                            session.setSessionUserId(userId);
                            return 1;
                        }
                        statement.close();
                    }catch (SQLException ex){
                        System.out.println("SQL query error yo " + ex);
                    }
                    return 0;

                } catch (SQLException e) {
                    Log.e( TAG, "Exception  " + e);
                    //throw new SQLException("Encountered an error when executing given sql statement.", e);
                }
            } else {
                System.out.println("Failed to create connection to database.");
            }
            System.out.println("Execution finished.");
            return 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Integer result) {
            mCallback.onTaskComplete(result);
            super.onPostExecute(result);
        }
    }



    class LogoutUser extends Worker{

        public LogoutUser(Context context){
            this.mContext = context;
            //this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected Integer doInBackground(String... params){
            super.doInBackground();
            this.connectDB();

            String user = params[0];
            int userId = Integer.parseInt(params[1]);

            if (connection != null)
            {
                System.out.println("Successfully created connection to database.");

                // Perform some SQL queries over the connection.
                try {
                    String queryLogout = "update users set loggedIn = 0 where email = '" + user + "';";
                    String queryRemoveLocation = "delete from usercurrentlocation where user_id = " + userId + ";";

                    Statement updateStatement = connection.createStatement();
                    updateStatement.executeUpdate(queryLogout);
                    updateStatement.executeUpdate(queryRemoveLocation);
                    updateStatement.close();
                }catch (SQLException e) {
                    Log.e( TAG, "Exception  " + e);
                }
            }else {
                System.out.println("Failed to create connection to database.");
            }
            System.out.println("Execution finished.");
            return 1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Integer result) {
            //mCallback.onTaskComplete(result);
            //super.onPostExecute(result);
        }
    }



    class RegisterUser extends Worker{

        public RegisterUser(Context context){
            this.mContext = context;
            this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            super.doInBackground();
            this.connectDB();

            String username = params[0];
            String email = params[1];
            String password = params[2];

            if (connection != null) {
                System.out.println("Successfully created connection to database.");

                // Perform some SQL queries over the connection.
                try {
                    //check if email is already used
                    String isUserQuery = "SELECT IFNULL( (SELECT email FROM users WHERE email = '" + email + "' or name = '" + username + "' limit 1 ) ,'null') as email;";
                    try {
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery(isUserQuery);
                        while(rs.next()){
                            String queryEmail = rs.getString("email");
                            System.out.println("query email   " + queryEmail);
                            if(!queryEmail.equals("null")){
                                statement.close();
                                return 0;
                            }
                        }
                        statement.close();

                    }catch (SQLException e){
                        System.out.println("User does not exist with that email");
                    }


                    String insertString = "insert into users ( name, email, encrypted_password, salt) values ( '" + username + "', '" + email + "', '" + password + "', 'salt');";

                    Statement insertStatement = connection.createStatement();
                    insertStatement.executeUpdate(insertString);
                    insertStatement.close();
                    // NOTE No need to commit all changes to database, as auto-commit is enabled by default.

                }
                catch (SQLException e) {
                    Log.e( TAG, "Exception  " + e);
                    //throw new SQLException("Encountered an error when executing given sql statement.", e);
                }
            } else {
                System.out.println("Failed to create connection to database.");
            }
            System.out.println("Execution finished.");
            return 1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Integer result) {
            mCallback.onTaskComplete(result);
            super.onPostExecute(result);
        }
    }



    class GetUsers extends Worker{
        public GetUsers(Context context){
            this.mContext = context;
            this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected Integer doInBackground(String... params){
            this.connectDB();
            String userEmail = params[0];

            if(connection != null){
                System.out.println("Successfully created connection to database.");


                // Perform some SQL queries over the connection.
                try {
                    Statement statement = connection.createStatement();
                    String queryUsers = "Select id, name, email,  encrypted_password, loggedIn from users where email != '" + userEmail + "';";
                    long count;
                    ResultSet rs = statement.executeQuery(queryUsers);
                    while(rs.next()){
                        String name, email, password, createdAt;
                        int id, loggedIn;
                        createdAt = "undefined";
                        UserData userData = new UserData(
                                id = rs.getInt("id"),
                                name = rs.getString("name"),
                                email = rs.getString("email"),
                                password = rs.getString("encrypted_password"),
                                createdAt,
                                loggedIn = rs.getInt("loggedIn")
                        );
                        userData.save();
                        count = SugarRecord.count(UserData.class);
                        String outputString =
                                String.format(
                                        "Data row = (%d, %s, %s, %s , %d )",
                                        id,name, email ,password ,loggedIn);
                        System.out.println(outputString);
                    }
                    connection.close();

                }catch (SQLException e) {
                    System.out.println("Database Exception during SELECT." + e);

                }
            }


            //ArrayList<String> mike = new ArrayList<String>();
            return 7;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Integer result) {
            mCallback.onTaskComplete(result);
            super.onPostExecute(result);
        }
    }



    class GetUserLocations extends Worker {

        public GetUserLocations(Context context){
            this.mContext = context;
            this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            this.connectDB();
            String userId = params[0];
            if (connection != null) {
                System.out.println("Successfully created connection to database.");


                // Perform some SQL queries over the connection.
                try {

                    Statement statement = connection.createStatement();
                    String sqlQuery = "SELECT name, UL.user_id, UL.loc_lat, UL.loc_long, loc_alt,loc_name , loc_description " +
                            "FROM usersavedlocations UL JOIN users ON users.id = UL.user_id " +
                            "WHERE UL.user_id = "+ userId +";";
                    System.out.println(sqlQuery);
                    ResultSet results = statement.executeQuery(sqlQuery );
                    while (results.next()) {
                        String name, description;
                        double latitude ,longitude;
                        double altitude;
                        int user_id;
                        SavedLocation location = new SavedLocation(
                                name = results.getString("loc_name"),
                                user_id = results.getInt("user_id"),
                                latitude = results.getDouble("loc_lat"),
                                longitude = results.getDouble("loc_long"),
                                altitude = results.getDouble("loc_alt"),
                                description = results.getString("loc_description")
                        );
                        location.save();



                        String outputString =
                                String.format(
                                        "Data row = (%s, %d, %f, %f , %f, %s )",
                                        name,user_id, latitude ,longitude ,altitude ,description);
                        System.out.println(outputString);
                        long count = SugarRecord.count(SavedLocation.class);
                        List<SavedLocation> locations = SavedLocation.listAll(SavedLocation.class);
                        Log.i(TAG, "Add User SavedLocation to local DB");
                    }

                    connection.close();

                    //List<UserSavedLocation> myLocation = UserSavedLocation.find(UserSavedLocation.class, "user_id = ?", "1");
                    List<SavedLocation> myLocation = SavedLocation.find(SavedLocation.class, "name = ?", "test1");
                    Log.i(TAG,"User Location for User "  + 1 + myLocation.get(0));
                }
                catch (SQLException e) {
                    System.out.println("Database Exception during SELECT." + e);
                    //throw new SQLException("Encountered an error when executing given sql statement", e);
                } catch (Exception e){
                    Log.e(TAG,"Exception Thrown : "+e);
                }

            }
            else {
                System.out.println("Failed to create connection to database.");
            }
            System.out.println("Execution finished.");
            return 1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Integer result) {
            mCallback.onTaskComplete(result);

        }
    }


    class GetLoggedInUsersLocation extends Worker {
        public GetLoggedInUsersLocation(Context context) {
            this.mContext = context;
            this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected Integer doInBackground(String... params) {
            this.connectDB();
            String userEmail = params[0];

            if (connection != null) {
                System.out.println("Successfully created connection to database.");


                // Perform some SQL queries over the connection.
                try {
                    Statement statement = connection.createStatement();
                    String queryCurrentLocation = "select name, CL.user_id, CL.user_lat, CL.user_long, CL.user_alt, CL.user_status" +
                            " from usercurrentlocation CL JOIN users ON users.id = CL.user_id where users.email != '" + userEmail +"';";
                    long count;
                    ResultSet rs = statement.executeQuery(queryCurrentLocation);
                    while (rs.next()) {
                        String name, status;
                        double latitude, longitude, altitude;
                        int user_id;

                        CurrentLocation currentLocation = new CurrentLocation(
                                name = rs.getString("name"),
                                user_id = rs.getInt("user_id"),
                                latitude = rs.getDouble("user_lat"),
                                longitude = rs.getDouble("user_long"),
                                altitude = rs.getDouble("user_alt"),
                                status = rs.getString("user_status")
                        );
                        currentLocation.save();

                        count = SugarRecord.count(CurrentLocation.class);
                        String outputString =
                                String.format(
                                        "Data row = (%s, %d, %f, %f , %f, %s )",
                                        name, user_id, latitude, longitude, altitude, status);
                        System.out.println(outputString);
                    }
                    connection.close();

                } catch (SQLException e) {
                    System.out.println("Database Exception during SELECT." + e);

                }
            }


            //ArrayList<String> mike = new ArrayList<String>();
            return 7;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer result) {
            mCallback.onTaskComplete(result);
            super.onPostExecute(result);
        }
    }


    class AddUserLocation extends Worker {
        public AddUserLocation(Context context) {
            this.mContext = context;
            //this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected Integer doInBackground(String... params){
            this.connectDB();
            //load parameters
            int id = Integer.parseInt(params[0]);
            double latitude = Double.parseDouble(params[1]);
            double longitude = Double.parseDouble(params[2]);
            double altitude = Double.parseDouble(params[3]);
            String name = params[4];
            String description = params[5];


            if (connection != null) {
                System.out.println("Successfully created connection to database.");
                // Perform some SQL queries over the connection.
                try {
                    Statement statement = connection.createStatement();
                    String queryCurrentLocation = "insert into usersavedlocations (user_id, loc_lat, loc_long, loc_alt, loc_name, loc_description) " +
                            "values ( " + id + ", " + latitude + ", " + longitude + ", " + altitude + ", '" + name + "', '" + description + "');";
                    long count;
                    statement.executeUpdate(queryCurrentLocation);


                    connection.close();

                } catch (SQLException e) {
                    System.out.println("Database Exception during SELECT." + e);

                }
            }


            //ArrayList<String> mike = new ArrayList<String>();
            return 7;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer result) {
            //mCallback.onTaskComplete(result);
            super.onPostExecute(result);
        }

    }

    class UpdateUserCurrentLocation extends Worker {
        public UpdateUserCurrentLocation(Context context){
            this.mContext = context;
            this.mCallback = (TaskCompleted) context;
        }

        @Override
        protected Integer doInBackground(String... params){
            this.connectDB();

            int id = Integer.parseInt(params[0]);
            double latitude = Double.parseDouble(params[1]);
            double longitude = Double.parseDouble(params[2]);
            double altitude = Double.parseDouble(params[3]);
            String userActivity = params[4];

            if (connection != null) {
                System.out.println("Successfully created connection to database.");


                // Perform some SQL queries over the connection.
                try {
                    Statement statement = connection.createStatement();
                    String updateUserCurrentLocation = "UPDATE usercurrentlocation SET user_lat = " + latitude + ", user_long = " +
                            longitude + ", user_alt = " + altitude + ", user_status = '" + userActivity + "' WHERE user_id = " + id + ";";
                    long count;
                    statement.executeUpdate(updateUserCurrentLocation);
                    connection.close();

                } catch (SQLException e) {
                    System.out.println("Database Exception during SELECT." + e);

                }
            }

            return 0;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer result) {
            //mCallback.onTaskComplete(result);
            super.onPostExecute(result);
        }
    }
}
