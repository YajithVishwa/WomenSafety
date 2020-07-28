package com.yajith.womensafety;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

public class mapsfragment extends Fragment implements OnMapReadyCallback {
    public Location location1;
    FusedLocationProviderClient fusedLocationProviderClient;
    private MapView mapView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fagement_maps,container,false);
        SupportMapFragment supportMapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.google_map);
        if(supportMapFragment==null)
        {
            FragmentManager fragmentManager=getFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            supportMapFragment=SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.google_map,supportMapFragment).commit();
        }
        supportMapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       LatLng latLng=new LatLng(Login.location.getLatitude(),Login.location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("You Are Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        googleMap.addMarker(markerOptions);

    }
}
