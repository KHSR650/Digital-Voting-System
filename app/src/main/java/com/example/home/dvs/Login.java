package com.example.home.dvs;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by home on 2/28/2017.
 */

public class Login extends AppCompatActivity {
    EditText getid, getpass;
    Button login;
    SQLiteDatabase mysql;
    String userName = "";
    boolean secure = false;
    ProgressDialog dialog1 = null;
    String uid = "";
    String passw = "";

    SharedPreferences sharedpreferences;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mysql = openOrCreateDatabase("Login", Context.MODE_PRIVATE, null);
        getid = (EditText) findViewById(R.id.userId);
        getpass = (EditText) findViewById(R.id.phno);
        login = (Button) findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog1 = ProgressDialog.show(Login.this, "", "Sending request\n please wait....", true);

                uid = getid.getText().toString();
                passw = getpass.getText().toString();
                if (uid.length() ==12 &&  !passw.equals("") && passw.length()>9) {
                    new AsyncLogin().execute(uid, passw);


                } else {
                    Toast.makeText(Login.this, "Fill the Fields", Toast.LENGTH_SHORT).show();
                }
                dialog1.dismiss();
            }
        });

    }

    public void secureLogin() {


        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();

        ContentValues cv = new ContentValues();
        cv.put("SNO", 10);
        cv.put("UNAME", uid);
        cv.put("PASSWD", passw);
        cv.put("PNAME", userName);
        mysql.insert("profile", null, cv);
        //Toast.makeText(getActivity(),"Stored",Toast.LENGTH_SHORT).show();
        Toast.makeText(Login.this,"****"+uid+"***",Toast.LENGTH_SHORT).show();
        Intent k =  new Intent("android.intent.action.MAINACTIVITY");
        k.putExtra("UID",uid);
        startActivity(k);
        finish();
    }





    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Login.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.8.1/dvs/user_login.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("userid", params[0])
                        .appendQueryParameter("phno", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(Login.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                if (!result.equals("0")) {
                    Toast.makeText(Login.this, result, Toast.LENGTH_LONG).show();
                    userName = result;
                    secureLogin();
                } else {
                    Toast.makeText(Login.this, "Invalid adhaar or phno", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

}

