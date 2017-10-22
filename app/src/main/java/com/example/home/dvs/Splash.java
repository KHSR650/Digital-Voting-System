package com.example.home.dvs;

import android.content.Intent;


        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;

/**
 * Created by Looser on j1-02-2017.
 */

public class Splash extends Activity {

    SQLiteDatabase mysql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        mysql = openOrCreateDatabase("Login", Context.MODE_PRIVATE, null);
        mysql.execSQL("CREATE TABLE IF NOT EXISTS profile(SNO int,UNAME varchar(30),PNAME varchar(30),PASSWD varchar(30));");
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    //if(checkLogin()){

                        Intent m = new Intent(Splash.this,Login.class);
                        startActivity(m);
                   // }
                   /* else
                    {
                        Intent m = new Intent(Splash.this,MainActivity.class);
                        startActivity(m);
                    }*/
                    finish();
                }
            }
        };

        timer.start();


    }

    public boolean checkLogin() {

        String rowid = "UNAME";
        String sno = "SNO";
        String name = "PNAME";
        String pass = "PASSWD";
        String[] columns = new String[]{sno, rowid, name, pass};
        Cursor cursor = mysql.query("profile", columns, null, null, null, null, null);

        String result = "";

        int irow = cursor.getColumnIndex(rowid);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result = result + cursor.getString(irow);
        }

        if (result.equals("")) {
            return true;
        } else {
            return false;
        }
        //String uname  = mysql.execSQL("SELECT UNAME FROM profile WHERE SNO=10;");


    }

}
