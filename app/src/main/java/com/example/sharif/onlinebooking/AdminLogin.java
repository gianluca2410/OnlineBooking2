package com.example.sharif.onlinebooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity {
  EditText editText;
  Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chk);
        editText=findViewById(R.id.p1);
        button=findViewById(R.id.l);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("123456")){
                    Intent intent=new Intent(AdminLogin.this,Operation.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(AdminLogin.this,"Invalid",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
