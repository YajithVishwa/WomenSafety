package com.yajith.womensafety;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class emergencyfragment extends Fragment {
    public EditText mobile;
    public Button button,emergency;
    public RelativeLayout relativeLayout;
    HashMap hashMap=new HashMap<String,String>();
    String[] emails=Login.email.split("@");
    TextView textView;
    String a,b,message;
    ToggleButton toggleButton;
    boolean flag=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fagment_emergency, container, false);
        mobile = v.findViewById(R.id.mobile);
        button = v.findViewById(R.id.regi);
        textView=v.findViewById(R.id.text);
        toggleButton=v.findViewById(R.id.tb1);
        emergency=v.findViewById(R.id.emer);
        relativeLayout = v.findViewById(R.id.relative);
        message="Emergency I am in danger,Locate me "+Login.lat+","+Login.lon+" .Help Me";
        retrive();
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.getText().equals("Sim 1")) {
                    sendsms1();
                }
                else
                {
                    sendsms2();
                }
            }
        });
        return v;
    }

    private void sendsms1() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.getSmsManagerForSubscriptionId(0).sendTextMessage(a, null, message, null, null);
        Snackbar snackbar=Snackbar.make(relativeLayout,"Sent",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    private void sendsms2() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(a, null, message, null, null);
        Snackbar snackbar=Snackbar.make(relativeLayout,"Sent",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void visibil()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hashMap.put("Parent_Mobile", mobile.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        button.setClickable(false);
                        textView.setText(mobile.getText().toString());
                        mobile.setVisibility(View.INVISIBLE);
                        flag=false;
                    }
                });
            }
        });
    }
    private void retrive() {
        FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Parent_Mobile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                a= (String) dataSnapshot.getValue();
                if(a.equals("null"))
                {
                    visibil();
                }
                else
                {
                    mobile.setVisibility(View.INVISIBLE);
                    textView.setText(a);
                    button.setClickable(false);
                    button.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Mobile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                b= (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
