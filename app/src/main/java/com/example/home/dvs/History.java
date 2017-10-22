package com.example.home.dvs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class History extends Fragment {

    ProgressDialog dialog1 = null;
    String httpurl = "http://192.168.8.1/dvs/get_History.php";

    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    protected boolean active = true;
    protected int splashTime = 8000;
    AlertDialog levelDialog;
    String uid;


    public static History newInstance() {
        History fragment = new History();
        return fragment;
    }

    public History() {
    }

    @Override
    public void onStart() {
        super.onStart();

        history();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("History");



    }

    /**
     * Updates the status on the action bar.
     *
     */
    private void setTitle(CharSequence title) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null == activity) {
            return;
        }
        if (null == activity.getSupportActionBar()) {
            return;
        }
        activity.getSupportActionBar().setTitle(title);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);
        Bundle bundle = this.getArguments();
        uid = bundle.getString("UID");
        return rootView;
    }


    public void history()
    {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final ListView lisView1 = (ListView)getActivity().findViewById(R.id.history_list);
        String url = httpurl;

        try {

            JSONArray data = new JSONArray(getJSONUrl(url));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
              //  if (c.getString("UID").equals(uid)) {

                    map = new HashMap<String, String>();

                    map.put("put_htime", c.getString("Time"));
                    map.put("put_hdesc", c.getString("Hdesc"));

                    MyArrList.add(map);
               // }

            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(getActivity(), MyArrList, R.layout.history_model,
                    new String[] {"put_htime","put_hdesc"}, new int[] { R.id.h_time,R.id.h_desc});
            lisView1.setAdapter(sAdap);

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(getActivity());
            // OnClick Item

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //////////////////////////////////////////////////////////////////////////
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
