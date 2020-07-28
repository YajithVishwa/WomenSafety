package com.yajith.womensafety;

import androidx.annotation.NonNull;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Login extends AppCompatActivity implements LocationListener{
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    HashMap hashMap=new HashMap<String,String>();
    public EditText emailID ;
    public static int flag=0;
    public static LocationListener locationListener;
    public static LocationManager locationManager;
    public static Location location;
    FusedLocationProviderClient mFusedLocationClient;
    public static double lon, lat;
    public static String email,mobileno,mobile1,pass;
    private EditText mobile;
    char[] mo=new char[12];
    public EditText passwordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        firebaseAuth=FirebaseAuth.getInstance();
        mobile=findViewById(R.id.mobile);
        final ProgressBar progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        TextView register=findViewById(R.id.register);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("User");
        emailID=findViewById(R.id.email);
        passwordID=findViewById(R.id.password);
        Button signin=findViewById(R.id.sign);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=emailID.getText().toString();
                mobile1=mobile.getText().toString();
                if(mobile1.charAt(0)=='+')
                {
                    String mo=mobile1.replace("+91","");
                    mobileno=mo;
                }
                pass=passwordID.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if(email.isEmpty())
                {
                    Toast.makeText(Login.this,"Enter the Email Id",Toast.LENGTH_SHORT).show();
                }
                else if(pass.isEmpty())
                {
                    Toast.makeText(Login.this,"Enter the Password",Toast.LENGTH_SHORT).show();
                }
                else if(mobile1.isEmpty())
                {
                    Toast.makeText(Login.this,"Enter the Mobile Number",Toast.LENGTH_SHORT).show();

                }
                try {

                    FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("Login").hasChild(mobileno))
                            {
                                FirebaseDatabase.getInstance().getReference().child("Login").child(mobileno).child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String emails = (String) dataSnapshot.getValue();
                                        if (emails.equals(email)) {

                                            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    if (task.isSuccessful()) {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        Intent intent1=new Intent(getApplicationContext(),gpsservice.class);
                                                        startService(intent1);
                                                        Intent intent = new Intent(Login.this, MainScreen.class);
                                                        finish();
                                                        startActivity(intent);

                                                    } else {
                                                        Toast.makeText(Login.this, "Not Success", Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Enter the Correct Email Id",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Enter the Correct Number",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                catch (Exception e)
                {
                    Toast.makeText(Login.this,"Enter valid login",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
//        lon=gpsservice.lon;
  //      lat=gpsservice.lat;
    //    String lo=String.valueOf(gpsservice.lon)+" , "+String.valueOf(gpsservice.lat);
      //  Toast.makeText(getApplicationContext(),lo,Toast.LENGTH_LONG).show();
    }
    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    public void registerof(View view) {
        Intent intent=new Intent(Login.this,Register.class);
        startActivity(intent);
    }
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                102
        );
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    String l=location.getLatitude()+" , "+location.getLongitude();
                                    lon=location.getLongitude();
                                    lat=location.getLatitude();
                                   // Toast.makeText(getApplicationContext(),l,Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lon=mLastLocation.getLongitude();
            lat=mLastLocation.getLatitude();
            //Toast.makeText(getApplicationContext(),j, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Login.this);
        alertDialog.setMessage("Do You Want To Exit?");
        alertDialog.setTitle("Alert!");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog1=alertDialog.create();
        alertDialog1.show();

    }
    @Override
    public void onLocationChanged(Location location) {
        lon=location.getLongitude();
        lat=location.getLatitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
