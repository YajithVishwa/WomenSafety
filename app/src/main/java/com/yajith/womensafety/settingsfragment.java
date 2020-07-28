package com.yajith.womensafety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class settingsfragment extends Fragment {
    CheckBox checkBox;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fagement_settings,container,false);
        checkBox=v.findViewById(R.id.check);
        Database database=new Database(getContext(),"Women",null,1);
        FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("OneTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String check=(String)dataSnapshot.getValue();
                check=check.trim();
                if(check.equals("no"))
                {
                    checkBox.setChecked(false);
                }
                else
                {
                    checkBox.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(checkBox.isChecked())
        {
            HashMap hashMap=new HashMap<String,String>();
            hashMap.put("OneTime","yes");
            FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                        Toast.makeText(getContext(),"Login Automatic Mode Activated",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            HashMap hashMap=new HashMap<String,String>();
            hashMap.put("OneTime","no");
            FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {

                }
            });
        }

        return v;
    }
}
