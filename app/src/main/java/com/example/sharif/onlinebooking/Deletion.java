package com.example.sharif.onlinebooking;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Deletion extends AppCompatActivity {
      EditText editText;
      Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletion);
        editText=findViewById(R.id.c);
        button=findViewById(R.id.del1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(Deletion.this,"Enter book code",Toast.LENGTH_SHORT).show();
                }
                else {
//                    Database database=new Database(Deletion.this);
//                  int b=  database.Del(editText.getText().toString());
//                    if(b>0){
//                        Toast.makeText(Deletion.this,"Book data deleted",Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(Deletion.this,"Book data not deleted somthing wrong",Toast.LENGTH_SHORT).show();
//                    }
                }
                editText.setText("");
            }
        });
    }
}
