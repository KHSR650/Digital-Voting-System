package com.example.home.dvs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Aboutus extends Fragment {

    //View variables
    Button postProblem;
    Button status;

    public static Aboutus newInstance() {
        Aboutus fragment = new Aboutus();
        return fragment;
    }

    public Aboutus() {
    }

    @Override
    public void onStart() {
        super.onStart();

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("AboutUs");




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
        View rootView = inflater.inflate(R.layout.abouus, container, false);
        return rootView;
    }

}
