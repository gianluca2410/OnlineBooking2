package com.example.sharif.onlinebooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatOperation extends AppCompatActivity {
    EditText p,pa,e,w;
    Button button;
    DatabaseReference dRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updat_operation);
        p=findViewById(R.id.bprice5);
        pa=findViewById(R.id.bpage5);
        e=findViewById(R.id.bedition5);
        w=findViewById(R.id.bword5);
        button=findViewById(R.id.sa1);
        dRef=  FirebaseDatabase.getInstance().getReference("Books");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p.getText().toString().equals("")||e.getText().toString().equals("")||
                        pa.getText().toString().equals("")||w.getText().toString().equals("")){
                    Toast.makeText(UpdatOperation.this,"All must be filed",Toast.LENGTH_SHORT).show();
                }
                else {
                    dRef.child(getIntent().getStringExtra("name")).child("BookPrice").setValue(p.getText().toString());
                    dRef.child(getIntent().getStringExtra("name")).child("BookPages").setValue(pa.getText().toString());
                    dRef.child(getIntent().getStringExtra("name")).child("BookEdition").setValue(e.getText().toString());
                    dRef.child(getIntent().getStringExtra("name")).child("BookWords").setValue(w.getText().toString());
                    Toast.makeText(UpdatOperation.this,"data updated",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        p.setText(getIntent().getStringExtra("price"));
        pa.setText(getIntent().getStringExtra("page"));
        e.setText(getIntent().getStringExtra("edition"));
        w.setText(getIntent().getStringExtra("words"));
        super.onStart();
    }
}
