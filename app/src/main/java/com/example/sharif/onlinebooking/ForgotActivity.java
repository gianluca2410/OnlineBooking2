package com.example.sharif.onlinebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    EditText email;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        email=findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.pg);
    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {
        if(email.getText().toString().trim().isEmpty()){
            email.setError("required");
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ForgotActivity.this, "Email Sent Successfully !", Toast.LENGTH_SHORT).show();
                                email.setText("");

                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ForgotActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }
}