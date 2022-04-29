package com.example.sharif.onlinebooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Update extends AppCompatActivity {
    EditText editText;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        editText=findViewById(R.id.Cn);
        button=findViewById(R.id.UP);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(Update.this,"Enter book code",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(Update.this,UpdatOperation.class);
                    intent.putExtra("CNIC",editText.getText().toString());
                    startActivity(intent);
                }
                editText.setText("");
            }
        });
    }
}
