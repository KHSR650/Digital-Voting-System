package com.example.home.dvs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Notices extends Fragment {
    ProgressDialog dialog1 = null;
    String httpurl = "http://192.168.8.1/dvs/get_notices.php";

    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    protected boolean active = true;
    protected int splashTime = 8000;
    AlertDialog levelDialog;
    String uid;
    @Override
    public void onStart() {
        super.onStart();
        notices();

    }
    public static Notices newInstance() {
        Notices fragment = new Notices();
        return fragment;
    }

    public Notices() {
    }
    //////////////////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        Bundle bundle = this.getArguments();
        uid = bundle.getString("UID");
        return rootView;
    }

    //////////////////////////////////////////////////////////////////////////
    private void notices() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final ListView lisView1 = (ListView)getActivity().findViewById(R.id.listView1);
        String url = httpurl;

        try {

            JSONArray data = new JSONArray(getJSONUrl(url));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                if (c.getString("To_id").equals(uid)) {
                    //new notifications in All Notifications

                    map.put("put_ntitle",c.getString("Title") );
                    map.put("put_ntime", c.getString("Time"));
                    map.put("put_nbody", c.getString("Body"));
                    MyArrList.add(map);

                }
            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(getActivity(), MyArrList, R.layout.activity_column,
                    new String[] {"put_ntitle","put_ntime"}, new int[] { R.id.ColTitle,R.id.ColTime});
            lisView1.setAdapter(sAdap);

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(getActivity());
            // OnClick Item
            lisView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView,
                                        int position, long mylng) {

                    String sTime = MyArrList.get(position).get("put_ntime").toString();

                    String sNotifi_Title = MyArrList.get(position).get("put_ntitle").toString();

                    String sNotification = MyArrList.get(position).get("put_nbody").toString();


                    //String sAttachments = MyArrList.get(position).get("put_attachments").toString();


                    Intent var = new Intent("com.example.rgukt.infra.NOTIFICATION_DETAILS");
                    var.putExtra("not_title",sNotifi_Title);
                    var.putExtra("not_body",sNotification);
                    var.putExtra("not_time", sTime);
                    startActivity(var);
                }

            });
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