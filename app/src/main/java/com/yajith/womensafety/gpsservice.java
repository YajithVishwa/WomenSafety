package com.yajith.womensafety;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mapbox.android.core.location.LocationEngineCallback;

import java.io.FileDescriptor;

public class gpsservice extends Service
{
    public static final String channel_id="Women_safety";
    private static final long UPDATE_INTERVAL_IN_MIL=10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MUL=UPDATE_INTERVAL_IN_MIL/2;
    private static final int Noti_id=1234;
    private boolean mChanging=false;
    private NotificationManager notificationManager;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Handler handler;
    private Location location;
    private final IBinder iBinder=new LocalBinder();
    public gpsservice()
    {

    }

    @Override
    public void onCreate() {
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        locationCallback=new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                OnNewLocation(locationResult.getLastLocation());
            }
        }
    }

    private void OnNewLocation(Location lastLocation) {
        location=lastLocation;
        EventBus.getDefault
    }

    public class LocalBinder implements IBinder {
        gpsservice getServices()
        {
            return gpsservice.this;
        }

        @Nullable
        @Override
        public String getInterfaceDescriptor() throws RemoteException {
            return null;
        }

        @Override
        public boolean pingBinder() {
            return false;
        }

        @Override
        public boolean isBinderAlive() {
            return false;
        }

        @Nullable
        @Override
        public IInterface queryLocalInterface(@NonNull String s) {
            return null;
        }

        @Override
        public void dump(@NonNull FileDescriptor fileDescriptor, @Nullable String[] strings) throws RemoteException {

        }

        @Override
        public void dumpAsync(@NonNull FileDescriptor fileDescriptor, @Nullable String[] strings) throws RemoteException {

        }

        @Override
        public boolean transact(int i, @NonNull Parcel parcel, @Nullable Parcel parcel1, int i1) throws RemoteException {
            return false;
        }

        @Override
        public void linkToDeath(@NonNull DeathRecipient deathRecipient, int i) throws RemoteException {

        }

        @Override
        public boolean unlinkToDeath(@NonNull DeathRecipient deathRecipient, int i) {
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
