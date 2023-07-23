package edu.csustan.cs3810.loginamer;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Task extends AsyncTask<String, Void, Integer> {
    private Context main;
    private AlertDialog dialog;
    private static final String PROTOCOL = "http://";
    private static final String IP = "35.233.134.106";
    private static final String FILE_NAME = "login.php";
    private static final String HTTP_POST_METHOD = "POST";
    private static final int CONNECTION_ERROR = -1;

    public Task(Context main) {
        this.main = main;
    }

    // Construct the URL for the login.php file
    private String getURL(String fileName) {
        return PROTOCOL + IP + "/" + fileName;
    }

    // Establish a connection to the server
    private HttpURLConnection establishConnection() {
        try {
            URL url = new URL(getURL(FILE_NAME));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HTTP_POST_METHOD);
            connection.setDoOutput(true);
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Send the login request to the server
    private void sendRequest(HttpURLConnection conn, String id, String password) {
        try {
            OutputStream outputStream = conn.getOutputStream();
            String requestBody = "id=" + id + "&password=" + password;
            outputStream.write(requestBody.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get the response from the server
    private String getResponse(HttpURLConnection conn) {
        try {
            InputStream inputStream = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        // Create an alert dialog to show login status
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle("Login Status");
        dialog = builder.create();
    }

    private Integer runQuery(String id, String password) {
        // Run the query to validate the login credentials
        HttpURLConnection connection = establishConnection();
        if (connection == null) {
            return CONNECTION_ERROR;
        }
        sendRequest(connection, id, password);
        String response = getResponse(connection);
        connection.disconnect();
        int numOfRecords = Integer.parseInt(response);
        return numOfRecords;
    }

    @Override
    protected Integer doInBackground(String... params) {
        String id = params[0];
        String password = params[1];
        return runQuery(id, password);
    }


    @Override
    protected void onPostExecute(Integer result) {
        // Display the login status message in the dialog
        String message;
        if (result > 0) {
            message = "Login success";
        } else {
            message = "Login fail";
        }
        dialog.setMessage(message);
        dialog.show();
    }
}
