package com.yajith.womensafety;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class editfragment extends Fragment {
    EditText editText1,editText2,editText3;
    Button button2,button;
    LinearLayout linearLayout;
    String[] emails=Login.email.split("@");
    HashMap hashMap=new HashMap<String,String>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fagement_edit, container, false);
        editText1 = v.findViewById(R.id.Name);
        editText2 = v.findViewById(R.id.Mobile);
        linearLayout=v.findViewById(R.id.linear);
        editText3 = v.findViewById(R.id.Parent);
        button2 = v.findViewById(R.id.save);
        button=v.findViewById(R.id.reset);
        retrive();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(Login.email);
                Snackbar snackbar=Snackbar.make(linearLayout,"Reset Link Send To Your Mail",Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        });
        return v;
    }
    public void retrive()
    {
        FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText1.setText((String)dataSnapshot.getValue(), TextView.BufferType.EDITABLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Mobile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText2.setText((String)dataSnapshot.getValue(), TextView.BufferType.EDITABLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Parent_Mobile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a=(String)dataSnapshot.getValue();
                if(!(a.equals("null"))) {
                    editText3.setText((String) dataSnapshot.getValue(), TextView.BufferType.EDITABLE);
                }
                else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void save()
    {
        if(editText1.getText().toString()!=""&&editText2.getText().toString()!=""&&editText3.getText().toString()!="") {
            hashMap.put("Name", editText1.getText().toString());
            hashMap.put("Mobile", editText2.getText().toString());
            if(editText3.getText().toString().equals(""))
            {
                hashMap.put("Parent_Mobile","null");
            }
            else {
                hashMap.put("Parent_Mobile", editText3.getText().toString());
            }
            FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frag, new profilefragment());
                    ft.commit();
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });
        }
    }
}
