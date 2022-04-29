package com.example.sharif.onlinebooking;

import static com.example.sharif.onlinebooking.Constant.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText user_email,user_password;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        user_email=findViewById(R.id.user_email);
        user_password=findViewById(R.id.user_password);
        firebaseAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.pg);
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(this,ForgotActivity.class));
    }

    public void login(View view) {
        if (user_email.getText().toString().trim().isEmpty()) {
            user_email.setError("required");
        } else if (user_password.getText().toString().trim().isEmpty()) {
            user_password.setError("required");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(user_email.getText().toString().trim(), user_password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "wrong mail or password" + task.getException(), Toast.LENGTH_LONG).show();
                    } else if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser();
                        getData();

                    }
                }
            });

        }
    }
    private void getData(){
        final String user_m=user_email.getText().toString().trim();
        myRef=  FirebaseDatabase.getInstance().getReference().child("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(user_m.equals(dataSnapshot1.child("Mail").getValue(String.class))) {
                        setUsername(LoginActivity.this,dataSnapshot1.child("Name").getValue(String.class));
                        setUsernumber(LoginActivity.this,dataSnapshot1.child("Phone").getValue(String.class));
                        setUseraddress(LoginActivity.this,dataSnapshot1.child("Address").getValue(String.class));
                        setUserLoginStatus(LoginActivity.this, true);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        progressBar.setVisibility(View.GONE);
                        finish();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openSignUpScreen(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }
}