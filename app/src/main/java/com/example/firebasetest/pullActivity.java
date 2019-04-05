package com.example.firebasetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class pullActivity extends AppCompatActivity {

    //RecyclerView recyclerView;

    EditText userInput;
    Button btnQ1;
    Button btnQ2;
    Button push;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseAuth firebaseAuth;
    //FirebaseUser user = null;
    String email;
    String password;
    ListView listView;
    ArrayList<String> grades;
    ListAdapter gradeAdapter;
    int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);
        email = getIntent().getStringExtra("emailStr");
        password = getIntent().getStringExtra("passwordStr");
        firebaseAuth = FirebaseAuth.getInstance();
        grades = new ArrayList<String>();
        gradeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grades);
        ListView listView = findViewById(R.id.lvGrades);
        listView.setAdapter(gradeAdapter);
        push = findViewById(R.id.btnPush);
        //DatabaseReference passID = myRef.child("simpsons/students/"+ Integer.toString(dbID) + "/password");
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

        userInput = findViewById(R.id.etStudentID);
        btnQ1 = findViewById(R.id.btnQuery1);
        btnQ2 = findViewById(R.id.btnQuery2);

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sintent = new Intent(pullActivity.this, pushActivity.class);
                sintent.putExtra("email",email);
                sintent.putExtra("password",password);
                startActivity(sintent);
            }
        });

        btnQ1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int studentID = Integer.parseInt(userInput.getText().toString());
                DatabaseReference gradeKey = myRef.child("simpsons/grades/");
                Query query = gradeKey.orderByChild("student_id").equalTo(studentID);
                grades.clear();
                query.addListenerForSingleValueEvent(valueEventListener);

            }
        });
        btnQ2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int studentID = Integer.parseInt(userInput.getText().toString());
                DatabaseReference gradeKey = myRef.child("simpsons/grades/");
                Query query = gradeKey.orderByChild("student_id").equalTo(studentID);
                grades.clear();
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });




    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                //Toast.makeText(getApplicationContext(),"listening",Toast.LENGTH_SHORT).show();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Grade grade = snapshot.getValue(Grade.class);

                    if(grade.getcourse_name().equals("Math")) {
                        grades.add("Math Grade is " + grade.getgrade());
                    }
                    if(grade.getcourse_name().equals("Physics")) {
                        grades.add("Physics Grade is " + grade.getgrade());
                    }
                    if(grade.getcourse_name().equals("Chemistry")) {
                        grades.add("Chemistry Grade is " + grade.getgrade());
                    }
                    if(grade.getcourse_name().equals("Biology")) {
                        grades.add("Biology Grade is " + grade.getgrade());
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //log error

        }
    };
}
