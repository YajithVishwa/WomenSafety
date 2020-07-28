package com.yajith.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    public EditText t1,t2,t3,mobile,con;
    FirebaseAuth firebaseAuth;
    String mobi;
    HashMap hashMap=new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        t1=findViewById(R.id.email);
        t2=findViewById(R.id.password);
        t3=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        con=findViewById(R.id.repass);
        Button b1=findViewById(R.id.reg);
        firebaseAuth=FirebaseAuth.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email=t1.getText().toString().trim();
                String pass=t2.getText().toString().trim();
                String nam=t3.getText().toString();
                String pass1=con.getText().toString().trim();
                mobi=mobile.getText().toString();
                if(mobi.charAt(0)=='+')
                {
                    String m1=mobi.replace("+91","");
                    mobi=m1;
                }
                if(email.equals("")&&pass.equals("")&&nam.equals("")&&mobi.equals("")&&pass1.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter the correct details",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(pass.equals(pass1)))
                {
                    Toast.makeText(getApplicationContext(),"Enter the Same Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                hashMap.put("Email",email);
                hashMap.put("Name",nam);
                hashMap.put("Mobile",mobi);
                hashMap.put("OneTime","no");
                hashMap.put("Parent_Mobile","null");
                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String[] emails=email.split("@");
                            FirebaseDatabase.getInstance().getReference().child("Login").child(mobi).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent intent=new Intent(Register.this,Login.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(Register.this,"Please check before signup",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void login(View view) {
        Intent intent=new Intent(Register.this,Login.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
