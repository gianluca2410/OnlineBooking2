package com.example.sharif.onlinebooking;

import static com.example.sharif.onlinebooking.Constant.ADMIN;
import static com.example.sharif.onlinebooking.Constant.ALARM;
import static com.example.sharif.onlinebooking.Constant.CONTACT;
import static com.example.sharif.onlinebooking.Constant.USER;
import static com.example.sharif.onlinebooking.Constant.getSelectedLanguage;
import static com.example.sharif.onlinebooking.Constant.getUserLoginStatus;
import static com.example.sharif.onlinebooking.Constant.setSelectedLanguage;
import static com.example.sharif.onlinebooking.Constant.setUserLoginStatus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Login extends AppCompatActivity {
   Button user,admin,contact,alarm;
   TextView current_time;
   int count=0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user=findViewById(R.id.user);
        admin=findViewById(R.id.admin);
        contact=findViewById(R.id.contact);
        alarm=findViewById(R.id.alarm);
        current_time=findViewById(R.id.current_time);
        getCurrentTime();
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getUserLoginStatus(Login.this)){
                    Intent intent=new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(Login.this,LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       Intent intent=new Intent(Login.this, AdminLogin.class);
                startActivity(intent);
            }
        });
    }

public void getCurrentTime(){

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                synchronized (this) {
                    wait(1000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            current_time.setText("Number of seconds you use the app " +count++);
                            getCurrentTime();
                        }
                    });

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    };
    thread.start();

}
    @Override
    protected void onStart() {
        if(getSelectedLanguage(this).equals("English")){
            user.setText(getResources().getString(R.string.user));
            admin.setText(getResources().getString(R.string.admin));
            contact.setText(getResources().getString(R.string.contact));

                    alarm.setText(getResources().getString(R.string.alarm));
        }
        else{
            user.setText(USER);
            admin.setText(ADMIN);
            contact.setText(CONTACT);
            alarm.setText(ALARM);
        }
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                setSelectedLanguage(Login.this,"English");
                user.setText(getResources().getString(R.string.user));
                admin.setText(getResources().getString(R.string.admin));
                contact.setText(getResources().getString(R.string.contact));
                return true;
            case R.id.item2:
                setSelectedLanguage(Login.this,"Italian");
                user.setText(USER);
                admin.setText(ADMIN);
                contact.setText(CONTACT);
                return true;
                case R.id.item3:
                setUserLoginStatus(Login.this,false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        @SuppressLint("Range") String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        // TODO Fetch other Contact details as you want to use

                    }
                }
                break;
        }
    }
    public void openContact(View view) {

        Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(intent, 1);
    }

    public void addAlaram(View view) {
          startActivity(new Intent(this,AlaramActivity.class));


    }

}
