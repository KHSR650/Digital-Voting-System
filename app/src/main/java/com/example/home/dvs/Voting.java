package com.example.home.dvs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

/**
 * Created by home on 3/25/2017.
 */

public class Voting extends AppCompatActivity {
    ViewFlipper vf;
    final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    String id, title,uid;
    ListView l1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voting_area);
        id = getIntent().getStringExtra("act_id");
        title = getIntent().getStringExtra("act_title");
        uid = getIntent().getStringExtra("UID");

        TextView t = (TextView) findViewById(R.id.Vtitle);
        t.setText(title+"\n"+"select your Nominee ");
        get_data();
    }
public void get_data() {
    if (android.os.Build.VERSION.SDK_INT > 9) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    final ListView lisView1 = (ListView) findViewById(R.id.nomi_list);


    String url = "http://192.168.8.1/dvs/get_Nominee_details.php";
    try {

        JSONArray data = new JSONArray(getJSONUrl(url));

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        for (int i = 0; i < data.length(); i++) {
            JSONObject c = data.getJSONObject(i);

            map = new HashMap<String, String>();
            if (c.getString("E_id").equals(id)) {

                map.put("put_nid", c.getString("N_id"));
                map.put("put_nname", c.getString("N_name"));
                map.put("put_nage", c.getString("N_age"));
                map.put("put_ndata", c.getString("N_data"));
                map.put("put_nprty", c.getString("N_prty"));
                map.put("put_nsymbol", c.getString("N_symbol"));
                MyArrList.add(map);

            }
        }

        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(this, MyArrList, R.layout.nominee_list,
                new String[]{"put_nname", "put_nprty"}, new int[]{R.id.ac_title, R.id.ac_mode});
        lisView1.setAdapter(sAdap);
        lisView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView,
                                    int position, long mylng) {

                String name = MyArrList.get(position).get("put_nname").toString();

                String age = MyArrList.get(position).get("put_nage").toString();

                String nid = MyArrList.get(position).get("put_nid").toString();

                String ndata = MyArrList.get(position).get("put_ndata").toString();

                String party = MyArrList.get(position).get("put_nprty").toString();

                String symbol = MyArrList.get(position).get("put_nsymbol").toString();

                //String sAttachments = MyArrList.get(position).get("put_attachments").toString();


                Intent var = new Intent("android.intent.action.CONFIRM_VOTE");
                var.putExtra("nomi_name",name);
                var.putExtra("nomi_age",age);
                var.putExtra("nomi_id",nid);
                var.putExtra("nomi_ndata",ndata);
                var.putExtra("nomi_party",party);
                var.putExtra("nomi_symbol",symbol);
                var.putExtra("act_id",id);
                var.putExtra("act_title",title);
                var.putExtra("UID",uid);
                // var.putExtra("files", sAttachments);
                startActivity(var);

            }

        });
    } catch (JSONException j) {
        j.printStackTrace();
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