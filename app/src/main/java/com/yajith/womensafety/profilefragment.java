package com.yajith.womensafety;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profilefragment extends Fragment {
    TextView t1,t2,t3,t4;
    String name=null,mobile,email,parent;
    Button button;
    String[] update=new String[4];
    ProgressDialog progressDialog;
    String[] emails=Login.email.split("@");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fagement_profile,container,false);
        t1=view.findViewById(R.id.name);
        t2=view.findViewById(R.id.mobile);
        t3=view.findViewById(R.id.email);
        t4=view.findViewById(R.id.parent);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        button=view.findViewById(R.id.edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
            }
        });
        if(savedInstanceState!=null)
        {

            name=savedInstanceState.getString("Name");
            mobile=savedInstanceState.getString("Mobile");
            email=savedInstanceState.getString("Email");
            parent=savedInstanceState.getString("Parent");
            t1.setText(name);
            t2.setText(mobile);
            t3.setText(email);
            t4.setText(parent);
        }
        new profile(getActivity()).execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog.show();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
            }
        },1000);
    }

    private void edit() {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag,new editfragment());
        ft.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Name",name);
        outState.putString("Mobile",mobile);
        outState.putString("Email",email);
        outState.putString("Parent",parent);
    }
    class profile extends AsyncTask<Void,String,String>
    {
        String name1=null;
        Activity activity;
        profile(Activity activity)
        {
            this.activity=activity;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name1=(String)dataSnapshot.getValue();
                    update[0]=name1;
                    FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Parent_Mobile").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            parent=(String)dataSnapshot.getValue();
                            update[2]=parent;
                            FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Mobile").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mobile=(String)dataSnapshot.getValue();
                                    update[3]=mobile;
                                    FirebaseDatabase.getInstance().getReference().child("Login").child(Login.mobileno).child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            email=(String)dataSnapshot.getValue();
                                            update[1]=email;
                                            publishProgress(update);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            return name1;
        }

        @Override
        protected void onProgressUpdate(String[] values) {
            super.onProgressUpdate(values);
            progressDialog.show();
            t1.setText(values[0]);
            t2.setText(values[3]);
            t3.setText(values[1]);
            t4.setText(values[2]);
            onPostExecute(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                progressDialog.hide();
            }
        }
    }
}
