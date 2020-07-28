package com.yajith.womensafety;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerOptions;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Navigate extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener,MapboxMap.OnMapClickListener{
    private MapView mapView;
    private static final long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private static final long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    private PermissionsManager permissionsManager;
    private Point point1,point2;
    private Button button;
    Double longitude,lattitude;
    private Marker marker;
    private DirectionsRoute currentRoute;
    private MapboxMap mapboxMap;
    HashMap hashMap=new HashMap<String,String>();
    LinearLayout linearLayout;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG="Navigate";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.api_mapbox));
        setContentView(R.layout.activity_navigate);
        button=findViewById(R.id.navigate);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        linearLayout=findViewById(R.id.linearla);
        mapView.getMapAsync(this);
        Snackbar snackbar=Snackbar.make(linearLayout,"Click To Drop A Point",Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationLauncherOptions navigationLauncherOptions= NavigationLauncherOptions.builder().directionsRoute(currentRoute)
                        .shouldSimulateRoute(false).build();
                NavigationLauncher.startNavigation(Navigate.this,navigationLauncherOptions);
            }
        });
    }
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this,"Navigating Permission Needed", Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if(marker!=null)
        {
            mapboxMap.removeMarker(marker);
        }
        marker=mapboxMap.addMarker(new MarkerOptions().position(point));
        point2=Point.fromLngLat(point.getLongitude(),point.getLatitude());
        point1=Point.fromLngLat(Login.lon,Login.lat);
        getRoute(point1,point2);
        button.setEnabled(true);
        return true;
    }
    private void getRoute(Point origin,Point destination)
    {
        NavigationRoute.builder(this).origin(origin).destination(destination).accessToken(Mapbox.getAccessToken())
                .build().getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                try {
                    if (response.body() == null) {
                        Toast.makeText(getApplicationContext(), "No Route and No Access Token", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (response.body().routes().size() == 0) {
                        Toast.makeText(getApplicationContext(), "No Route", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    currentRoute = response.body().routes().get(0);
                    if (navigationMapRoute != null) {
                        navigationMapRoute.removeRoute();
                    } else {
                        navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap);
                    }
                    navigationMapRoute.addRoute(currentRoute);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Wait Loading",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                   enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this,"Permission Not Granted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        Navigate.this.mapboxMap = mapboxMap;
        Navigate.this.mapboxMap.addOnMapClickListener(this);
    try {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
            }
        });
    }
    catch (Exception e)
    {

    }

    }
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        try {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            if (PermissionsManager.areLocationPermissionsGranted(this)) {
                //locationComponent.setRenderMode(RenderMode.NORMAL);
                locationComponent.activateLocationComponent(
                        LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
               // Location location=locationComponent.getLastKnownLocation();
                //longitude=location.getLongitude();
                //lattitude=location.getLatitude();
                //update();
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setCameraMode(CameraMode.TRACKING);
                locationComponent.setRenderMode(RenderMode.COMPASS);
            } else {
                permissionsManager = new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(this);
            }
        }
        catch (Exception e)
        {}
    }

    private void update() {
        hashMap.put("Lon",Double.toString(longitude));
        hashMap.put("Lat",Double.toString(lattitude));
        String[] emails=Login.email.split("@");
        FirebaseDatabase.getInstance().getReference().child("Login").child(emails[0]).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        locationComponent.setLocationComponentEnabled(false);
        mapView.onDestroy();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }
}