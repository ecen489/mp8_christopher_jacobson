package com.example.firebasetest;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class pushActivity extends AppCompatActivity {

    FirebaseDatabase fbdb;
    DatabaseReference dbrf;
    FirebaseAuth firebaseAuth;
    int radioID = R.id.rad_bart;
    int dbID = 123;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        fbdb = FirebaseDatabase.getInstance();
        dbrf = fbdb.getReference();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Login Successful!",Toast.LENGTH_SHORT).show();
                    //user = FirebaseAuth.getCurrentUser(); //The user is signed in
                } else {
                    Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void radioClick(View view) {
        radioID = view.getId();
        if(radioID==R.id.rad_bart){dbID = 123;}
        if(radioID==R.id.rad_ralph){dbID = 404;}
        if(radioID==R.id.rad_milhouse){dbID = 456;}
        if(radioID==R.id.rad_lisa){dbID = 888;}
    }


    public void buttonClick(View view) {

        EditText edTxt= findViewById(R.id.edtxt);
        String pass = edTxt.getText().toString();
        edTxt.getText().clear();

        DatabaseReference passID = dbrf.child("simpsons/students/" + Integer.toString(dbID) + "/password");
        passID.setValue(pass);

        // DatabaseReference stud = dbrf.child("simpsons/students/999");
        // stud.child("email").setValue("abc@def.com");
    }

    public void buttonClick2(View view) {

        final TextView myTxt = findViewById(R.id.myTxt);

        DatabaseReference passID = dbrf.child("simpsons/students/"+ Integer.toString(dbID) + "/password");
        passID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                String pass = data.getValue().toString();
                myTxt.setText(pass);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //log error
            }
        });

    }
}
