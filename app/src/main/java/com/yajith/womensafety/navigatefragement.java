package com.yajith.womensafety;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.List;
import java.util.Map;

public class navigatefragement extends Fragment implements OnMapReadyCallback, PermissionsListener {
    GoogleMap Map;
    LinearLayout linearLayout;
    Button button;
    Mapbox mapbox;
    private PermissionsManager permissionsManager;
    private MapView mapView;

    MapboxMap mapboxMap;
    MarkerOptions markerOptions1,markerOptions2;
    Polyline polyline;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fagement_navigation, container, false);
        linearLayout = v.findViewById(R.id.linearla);
        button = v.findViewById(R.id.get);
        mapView=v.findViewById(R.id.mapView);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    Mapbox.getInstance(getContext(), getString(R.string.api_mapbox));
        SupportMapFragment mapFragment;
        if (savedInstanceState == null) {
            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(getContext(), null);
            options.camera(new CameraPosition.Builder()
                    .target(new com.mapbox.mapboxsdk.geometry.LatLng(Login.lat,Login.lon))
                    .zoom(9)
                    .build());
            mapFragment = SupportMapFragment.newInstance(options);
            transaction.add(R.id.mapView, mapFragment, "com.mapbox.map");
            transaction.commit();
        } else {
            mapFragment = (SupportMapFragment) getFragmentManager().findFragmentByTag("com.mapbox.map");
        }
        if(mapFragment!=null)
        {
            mapFragment.getMapAsync(new com.mapbox.mapboxsdk.maps.OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {

                }
            });
        }
    }
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle).build());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        navigatefragement.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7"),
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

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
            Toast.makeText(getContext(),"Not Granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
