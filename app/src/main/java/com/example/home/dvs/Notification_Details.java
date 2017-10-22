package com.example.home.dvs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Notification_Details extends Activity {
	

	String title,time,body;
	TextView tv_title,tv_time,tv_body;
	Button downloads;
	
	 String status;
     int st;
    public static final String LOG_TAG = "File-Path";
	private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
     
    //initialize root directory
    File rootDir = Environment.getExternalStorageDirectory();

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_details);


		//downloads = (Button) findViewById(R.id.attach);
		title = getIntent().getStringExtra("not_title");

		time = getIntent().getStringExtra("not_time");
		body = getIntent().getStringExtra("not_body");

        tv_title = (TextView)findViewById(R.id.title);
        
        tv_time = (TextView)findViewById(R.id.time );
        
        tv_body = (TextView)findViewById(R.id.body);

		tv_title.setText(title);
		tv_time.setText(time);
		tv_body.setText(body);

	}


	@Override
 	public void onBackPressed() 
	{
 		finish();
 		return;
 	}



}
