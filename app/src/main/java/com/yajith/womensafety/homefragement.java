package com.yajith.womensafety;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class homefragement extends Fragment {
    MainScreen mainScreen;
    public String name = null, temp,n;
    String[] emails = Login.email.split("@");
    TextView textView1, textView2, textView3;
    ProgressDialog progressDialog;
    BroadcastReceiver broadcastReceiver;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fagement_home, container, false);
        mainScreen = new MainScreen();
        textView1 = v.findViewById(R.id.address);
        textView2 = v.findViewById(R.id.name2);
        weather weather = new weather();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.hide();
        get(Login.lon, Login.lat);
        new loaddate(getActivity()).execute();
        if (savedInstanceState!=null)
        {
            name=savedInstanceState.getString("Name");
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog.show();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
            }
        },1000);

    }

    @Override
    public void onPause() {
        super.onPause();
        progressDialog.hide();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Name",name);
    }

    public void get(double lon, double lat) {
        //progressDialog.show();
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            try {
                Address obj = addresses.get(0);
                String add = obj.getAddressLine(0);
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                String tex;
                tex = "You Are At " + obj.getAddressLine(0);
                textView1.setText(tex);
            } catch (IndexOutOfBoundsException e) {
                textView1.setText("You Are At Tamil Nadu");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //progressDialog.show();
    }

    class loaddate extends AsyncTask<Void, String, String> {
        String name1;
        Activity mcontext;
        loaddate(Activity context)
        {
            this.mcontext=context;
        }
        public void flag(String s)
        {
            name=s;
        }
        @Override
        protected String doInBackground(Void... strings) {
            //onPreExecute();
            //progressDialog.show();
            FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name1= (String) dataSnapshot.getValue();
                    flag(name1);
                    publishProgress(name1);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return n;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.show();
            textView2.setText(values[0]);
            onPostExecute(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                progressDialog.hide();
            }
        }
    }
}