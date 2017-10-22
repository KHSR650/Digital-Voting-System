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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Activities extends Fragment {

    ProgressDialog dialog1 = null;
    String httpurl = "http://192.168.8.1/dvs/get_Activities.php";

    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    protected boolean active = true;
    protected int splashTime = 8000;
    AlertDialog levelDialog;
    String uid;
    public static Activities newInstance() {
        Activities fragment = new Activities();
        return fragment;
    }

    public Activities() {
    }

    @Override
    public void onStart() {
        super.onStart();
        notices();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Activities");



    }

    /**
     * Updates the status on the action bar.
     *

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

     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_activities, container, false);
        Bundle bundle = this.getArguments();
        uid = bundle.getString("UID");
        return rootView;
    }
    public void notices()
    {
        Toast.makeText(getActivity(),"****"+uid+"***",Toast.LENGTH_SHORT).show();
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

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();

                map.put("put_eid", c.getString("E_id"));
                map.put("put_emode", c.getString("mode"));
                map.put("put_estart", c.getString("E_start"));
                map.put("put_eend", c.getString("E_end"));
                map.put("put_edesc", c.getString("E_desc"));
                map.put("put_efor", c.getString("E_for"));
                map.put("put_etitle", c.getString("E_title"));
               map.put("put_evoter",c.getString("E_voters"));

                MyArrList.add(map);

            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(getActivity(), MyArrList, R.layout.model_active,
                    new String[]{"put_etitle", "put_emode"}, new int[]{R.id.ac_title, R.id.ac_mode});
            lisView1.setAdapter(sAdap);

            lisView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView,
                                        int position, long mylng) {

                    String sDate = MyArrList.get(position).get("put_eid").toString();

                    String sNotifi_Title = MyArrList.get(position).get("put_etitle").toString();

                    String sNotification = MyArrList.get(position).get("put_edesc").toString();

                    String sTime = MyArrList.get(position).get("put_estart").toString();

                    String sSdby = MyArrList.get(position).get("put_eend").toString();

                    String sSendto = MyArrList.get(position).get("put_emode").toString();
                    String eVoter = MyArrList.get(position).get("put_evoter").toString();

                    //String sAttachments = MyArrList.get(position).get("put_attachments").toString();


                    Intent var = new Intent("android.intent.action.ACTIVITY_DETAILS");
                    var.putExtra("act_title",sNotifi_Title);
                    var.putExtra("act_desc",sNotification);
                    var.putExtra("act_end", sSdby);
                    var.putExtra("act_id", sDate);
                    var.putExtra("act_start", sTime);
                    var.putExtra("act_mode", sSendto);
                    var.putExtra("act_mode", sSendto);
                    var.putExtra("UID",uid);
                    var.putExtra("Evoter",eVoter);

                    // var.putExtra("files", sAttachments);
                    startActivity(var);

                }

            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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
