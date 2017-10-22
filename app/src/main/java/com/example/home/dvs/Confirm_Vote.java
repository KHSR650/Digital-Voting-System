package com.example.home.dvs;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class Confirm_Vote extends ActionBarActivity {


    String title, id, name, age, symbol, data, nid,uid="",party;
    String otp;

    SQLiteDatabase mysql;
    public static final String LOG_TAG = "File-Path";
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    String s = null;
    int confirm;
    ProgressDialog dialog1 = null;
    EditText edit_text_otp;
    Button submit;

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    NotificationManager NM;
    public int conform = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nominee_model);


        title = getIntent().getStringExtra("act_title");
        id = getIntent().getStringExtra("act_id");
        name = getIntent().getStringExtra("nomi_name");
        age = getIntent().getStringExtra("nomi_age");
        symbol = getIntent().getStringExtra("nomi_symbol");
        data = getIntent().getStringExtra("nomi_data");
        nid = getIntent().getStringExtra("nomi_id");
        party = getIntent().getStringExtra("nomi_party");
        uid = getIntent().getStringExtra("UID");


        TextView h = (TextView) findViewById(R.id.heading);
        h.setText(title);
        TextView n = (TextView) findViewById(R.id.n_name);
        n.setText(name);
        TextView a = (TextView) findViewById(R.id.n_age);
        a.setText("Age ::"+age);
        TextView p = (TextView) findViewById(R.id.n_party);
        p.setText("Party ::"+party);
        TextView s = (TextView) findViewById(R.id.n_symbol);
        s.setText("Symbol::"+symbol);
        //TextView d = (TextView) findViewById(R.id.n_desc);
       // d.setText(data);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.optlayout);
        linearLayout.setVisibility(View.INVISIBLE);



        edit_text_otp  = (EditText) findViewById(R.id.otp);
        submit = (Button) findViewById(R.id.Vote);


        submit.setVisibility(View.VISIBLE);




        Toast.makeText(this,uid,Toast.LENGTH_SHORT).show();

        final Button l = (Button) findViewById(R.id.Vote);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        Confirm_Vote.this);
                alertDialog.setTitle("Cinformation :: ");
                alertDialog.setMessage("Are you sure you want to this Nominee?");

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                LinearLayout linearLay = (LinearLayout) findViewById(R.id.optlayout);
                                linearLay.setVisibility(View.VISIBLE);
                                submit.setVisibility(View.INVISIBLE);

                               sending_otp();
                                    if(confirm==111)
                                    {
                                        Toast.makeText(Confirm_Vote.this,"Enter OTP",Toast.LENGTH_SHORT).show();
                                    }


                            }

                        });
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }

        });
    }




    ////////////

    private class AsyncVote extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Confirm_Vote.this);
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
                url = new URL("http://192.168.8.1/dvs/Add_Vote.php");

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
                        .appendQueryParameter("eid", params[0])
                        .appendQueryParameter("nid", params[1])
                        .appendQueryParameter("uid", params[2]);
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

                Toast.makeText(Confirm_Vote.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {
                if (!result.equals("0")) {
                    Toast.makeText(Confirm_Vote.this, result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Confirm_Vote.this, "Unsuccess", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    public  void  Check_otp(View v) {

        String otp2 = edit_text_otp.getText().toString();
        Toast.makeText(Confirm_Vote.this,otp2, Toast.LENGTH_SHORT).show();
       if (otp2.equals(otp)) {
            Toast.makeText(Confirm_Vote.this, "entered correclty", Toast.LENGTH_SHORT).show();
            confirm = 999;
           new AsyncVote().execute(id, nid, uid);
           Toast.makeText(Confirm_Vote.this, "Voting Sucess", Toast.LENGTH_SHORT).show();

           Thread timer = new Thread() {
               @Override
               public void run() {
                   try {

                       sleep(10000);
                   } catch (Exception e) {
                       e.printStackTrace();
                   } finally {
                        dialog1.dismiss();
                   }
               }
           };
           dialog1 = ProgressDialog.show(Confirm_Vote.this, "", "Voting completed \n Moving to Home ", true);
           timer.start();
           //Toast.makeText(Confirm_Vote.this, "Moving to Main Page", Toast.LENGTH_SHORT).show();
           Intent i = new Intent("android.intent.action.MAINACTIVITY");
           i.putExtra("UID",uid);

           startActivity(i);
           finish();

        } else {
            Toast.makeText(Confirm_Vote.this, "Please enter correclty", Toast.LENGTH_SHORT).show();
            confirm = 777;
        }




        //}
     //   */
    }
    //////////////
    private void  sending_otp() {
        int k = (int) (100000+(Math.random()*100000));
        otp = Integer.toString(k);
        android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Confirm_Vote.this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("DVS")
                .setContentText("OTP is :"+otp);

        // Obtain NotificationManager system service in order to show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(112, mBuilder.build());
        Toast.makeText(Confirm_Vote.this,"Otp send",Toast.LENGTH_SHORT).show();
        confirm=111;
    }
}



