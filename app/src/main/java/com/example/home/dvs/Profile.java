package com.example.home.dvs;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Profile extends Fragment {

    String httpurl = "192.168.8.1/dvs/user_details.php";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    public static int Array_size = 0;
    private View mainView;

    String userID;
    SQLiteDatabase mysql;
    TextView t1,t2,t3,t4,t5;
    public static Profile newInstance() {
        Profile fragment = new Profile();
        return fragment;
    }

    public Profile() {
    }

    @Override
    public void onStart() {
        super.onStart();
        mysql = getActivity().openOrCreateDatabase("Login", Context.MODE_PRIVATE, null);
        //userID = getuserID();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Profile");
        profile();

        //new AsyncRetrieve().execute();
    }

    /**

     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);
        Bundle bundle = this.getArguments();
        userID = bundle.getString("UID");
        t1 = (TextView)getActivity().findViewById(R.id.name);
        t2 = (TextView)getActivity().findViewById(R.id.adhaar);
        t3 = (TextView)getActivity().findViewById(R.id.mail);
        t4 = (TextView)getActivity().findViewById(R.id.phno);
        t5 = (TextView)getActivity().findViewById(R.id.adds);

        return rootView;
    }

    public String getuserID() {

        String rowid = "UNAME";
        String sno = "SNO";
        String name = "PNAME";
        String pass = "PASSWD";
        String[] columns = new String[]{sno, rowid, name, pass};
        Cursor cursor = mysql.query("profile", columns, null, null, null, null, null);

        String id = "";
        String uname = "";

        int irow = cursor.getColumnIndex(rowid);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            id = id + cursor.getString(irow);
            uname = uname + cursor.getString(irow);
        }
        //String uname  = mysql.execSQL("SELECT UNAME FROM profile WHERE SNO=10;");

        return id;
    }

    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        // This method does not interact with UI, You need to pass result to onPostExecute to display
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("http://192.168.8.1/dvs/CheckConnection.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
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
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }


        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(String result) {

            pdLoading.dismiss();
            if (result.equals("connection success")) {
                //notices();
            } else {
                // you to understand error returned from doInBackground method
                Toast.makeText(getActivity(), "CHECK YOUR NETWORK", Toast.LENGTH_LONG).show();

            }

        }

    }

    public void profile() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        String url = httpurl;

        try {

            JSONArray data = new JSONArray(getJSONUrl(url));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();

                if (c.getString("UID").equals(userID)) {
                    map.put("put_name", c.getString("U_name"));
                    map.put("put_adhaar", c.getString("U_ahaar"));
                    map.put("put_phno", c.getString("U_phno"));
                    map.put("put_email", c.getString("U_email"));
                    map.put("put_age", c.getString("U_age"));
                    map.put("put_dob", c.getString("U_dob"));
                    map.put("put_add", c.getString("Adress"));
                    MyArrList.add(map);

                    Toast.makeText(getActivity(),"***Details came**",Toast.LENGTH_SHORT);
                }

            }

            t1.setText(MyArrList.get(0).get("put_name").toString());
            t2.setText(MyArrList.get(0).get("put_adhaar").toString());
            t3.setText(MyArrList.get(0).get("put_email").toString());
            t4.setText(MyArrList.get(0).get("put_phno").toString());
            t5.setText(MyArrList.get(0).get("put_add").toString());

        }
        catch (Exception e)
        {

        };
    }

    private String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}