package com.yajith.womensafety;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.Toast;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static Timestamp timestamp1;
    int id;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    HashMap hashMap=new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Fragment fragment=new homefragement();
        if(fragment!=null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag, fragment);
            ft.commit();
        }
        if(savedInstanceState!=null)
        {
            id=savedInstanceState.getInt("id");
        }
        if(Login.flag==0) {
            insert();
        }
        //first();
        OnBackPressedCallback onBackPressedCallback=new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    logout();
                }
            };
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Login login=new Login();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void first() {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag,new homefragement());
        ft.commit();
    }
    public void insert()
    {
        hashMap.put("Longitute",Double.toString(Login.lon));
        hashMap.put("Latitude",Double.toString(Login.lat));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        hashMap.put("TimeStamp",currentDateTime);
        FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
              //  Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
                Login.flag=1;
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            logout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();

        if (id == R.id.home) {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag,new homefragement());
            ft.commit();
        } else if (id == R.id.maps) {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag,new mapsfragment());
            ft.commit();

        }
        else if(id==R.id.emergency)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag,new emergencyfragment());
            ft.commit();
        }
        else if (id == R.id.navigation) {
            Intent intent=new Intent(MainScreen.this,Navigate.class);
            startActivity(intent);
          /*FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag,new navigatefragement());
            ft.commit();*/

        } else if (id == R.id.logout) {
            logout();
        }
        else if(id==R.id.settings)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag,new settingsfragment());
            ft.commit();
        }
        else if(id==R.id.help)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag,new helpfragment());
            ft.commit();
        }
        else if(id==R.id.profile)
        {
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag,new profilefragment());
            ft.commit();
        }
        else
        {
            id=R.id.home;
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frag,new homefragement());
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public  void logout()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do You Want To Logout?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                //stopService(new Intent(getApplicationContext(),gpsservice.class));
                startActivity(new Intent(MainScreen.this,Login.class));
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.setTitle("Logout");
        alertDialog.show();

    }

    public void abouts(MenuItem item) {
        startActivity(new Intent(MainScreen.this,About.class));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id",id);
    }
}
