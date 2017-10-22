package com.example.home.dvs;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
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
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.id;

public class Activity_Details extends ActionBarActivity {


    String title,id,stime,etime,desc,mode,voters;


    public static final String LOG_TAG = "File-Path";
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    String s = null;
    ProgressDialog dialog1 = null;
String uid;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    public int conform = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        title = getIntent().getStringExtra("act_title");
        id = getIntent().getStringExtra("act_id");
        stime = getIntent().getStringExtra("act_start");
        etime = getIntent().getStringExtra("act_end");
        desc = getIntent().getStringExtra("act_desc");
        mode = getIntent().getStringExtra("act_mode");
        uid = getIntent().getStringExtra("UID");
        voters = getIntent().getStringExtra("Evoter");


        Toast.makeText(Activity_Details.this,"****"+uid+"***",Toast.LENGTH_SHORT).show();


        TextView a= (TextView)findViewById(R.id.head);
        TextView b= (TextView)findViewById(R.id.stime);
        TextView c= (TextView)findViewById(R.id.ftime);
        TextView e = (TextView)findViewById(R.id.vdesc);
        TextView d= (TextView)findViewById(R.id.status);
        TextView ch= (TextView)findViewById(R.id.checking);

        a.setText(title);
        b.setText(stime);
        c.setText(etime);
        d.setText(mode);
        e.setText(desc);
        Button l = (Button)findViewById(R.id.go_vote);
        if(mode.equals("Present")&&!voters.toString().contains(uid.toString()))
        {
                ch.setText("You not participated in this voting");
                l.setText("Go for Vote");
            l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    voting_area();
                }
            });
        }
        else if (mode.equals("Present")&&voters.toString().contains(uid.toString()))
        {
            ch.setText("You already  participated in this voting");
            l.setText("View Results");
            l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_result(v);
                }
            });
        }
        else if (mode.equals("Future"))
        {
            l.setText("Wait");
        }
    }

    public void voting_area(){

        Toast.makeText(Activity_Details.this,"clicked voting"+id,Toast.LENGTH_SHORT).show();
       Intent p = new Intent("android.intent.action.VOTING_AREA");
        p.putExtra("act_id",id);
        p.putExtra("act_title",title);
        p.putExtra("UID",uid);
        startActivity(p);
    }
    public void view_result(View v){
        Toast.makeText(Activity_Details.this,"Result will come",Toast.LENGTH_SHORT).show();
    }

}
