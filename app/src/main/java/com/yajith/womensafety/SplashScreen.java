package com.yajith.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {
    public static final int requestcode = 102;
    String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.SEND_SMS,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RelativeLayout linearLayout = findViewById(R.id.linear);

        if(!checkPermission(SplashScreen.this,permission,requestcode))
        {
            ActivityCompat.requestPermissions(this,permission,requestcode);
        }
        else
        {
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // startService(new Intent(getApplicationContext(),gpsservice.class));
                    startActivity(new Intent(SplashScreen.this, Login.class));

                    finishing();
                }
            },1000);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public Boolean checkPermission(Context context, String permission[], int requestCode) {
        Boolean flag=true;
        for(String i:permission)
        {
            if(context.checkSelfPermission(i)!=PackageManager.PERMISSION_GRANTED)
            {
                flag=false;
            }
        }
        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 102:if(((grantResults[0]==PackageManager.PERMISSION_GRANTED)&&(grantResults[1]==PackageManager.PERMISSION_GRANTED))&&((grantResults[2]==PackageManager.PERMISSION_GRANTED&&grantResults[3]==PackageManager.PERMISSION_GRANTED)))
            {
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //startService(new Intent(getApplicationContext(),gpsservice.class));
                        startActivity(new Intent(SplashScreen.this, Login.class));
                        finishing();
                    }
                },2000);
            }
            else
            {
                Toast.makeText(this,"Accept All Permission",Toast.LENGTH_SHORT).show();this.finish();
            };break;
            default:Toast.makeText(this,"Device Not Supported",Toast.LENGTH_SHORT).show();
        }
    }
    public void finishing()
    {
        this.finish();
    }
}
