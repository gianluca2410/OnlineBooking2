package com.example.sharif.onlinebooking;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Admin extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
          EditText B_name,A_name,B_page,B_words,B_price,B_edition,B_code;
          Button button;
    StorageReference mRef;
    DatabaseReference dRef;
    ImageView imageview;
    private Uri imgUri;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mRef= FirebaseStorage.getInstance().getReference("book_images");
        B_name=findViewById(R.id.Bname);
        A_name=findViewById(R.id.aname);
        B_page=findViewById(R.id.bpage);
        B_words=findViewById(R.id.bword);
        B_price=findViewById(R.id.bprice);
        B_edition=findViewById(R.id.bedition);
        B_code=findViewById(R.id.code);
        button=findViewById(R.id.sa);
        progressBar=findViewById(R.id.progressBar);
        imageview=findViewById(R.id.imageview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(B_name.getText().toString().equals("")||A_name.getText().toString().equals("")||
                     B_page.getText().toString().equals("")||B_words.getText().toString().equals("")||
                     B_price.getText().toString().equals("")||B_edition.getText().toString().equals("")
                     ||B_code.getText().toString().equals("")){
                 Toast.makeText(Admin.this,"Enter data in all fields",Toast.LENGTH_SHORT).show();
             }
             else {

                 progressBar.setVisibility(View.VISIBLE);
                 StorageReference storageReference=mRef.child(System.currentTimeMillis()+"."+getFileEx(imgUri));
                 storageReference.putFile(imgUri)
                         .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                             @Override
                             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                 Toast.makeText(Admin.this,"upload successful",Toast.LENGTH_LONG).show();

                                 Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                 while (!urlTask.isSuccessful());
                                 Uri downloadUrl = urlTask.getResult();
                                 dRef=    FirebaseDatabase.getInstance().getReference("Books").child(B_name.getText().toString());
                                 dRef.child("BookImage").setValue(downloadUrl.toString());
                                 dRef.child("BookName").setValue(B_name.getText().toString());
                                 dRef.child("BookPrice").setValue(B_price.getText().toString());
                                 dRef.child("BookWords").setValue(B_words.getText().toString());
                                 dRef.child("BookAuthor").setValue(A_name.getText().toString());
                                 dRef.child("BookPages").setValue(B_page.getText().toString());
                                 dRef.child("BookCode").setValue(B_code.getText().toString());
                                 dRef.child("BookEdition").setValue(B_edition.getText().toString());
                                 progressBar.setVisibility(View.GONE);
                                 // call the notification function
                                 scheduleNotification(getNotification("Data added into firebase"),1000);
                                 finish();

                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(Admin.this,e.getMessage(),Toast.LENGTH_LONG).show();
                             }
                         })
                         .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                             @Override
                             public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                 double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                 progressBar.setProgress((int) progress );
                             }
                         });

             }
            }
        });
    }
    // notification function
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Local Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }
    // create notification after 1 second
    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, NotificationService. class ) ;
        notificationIntent.putExtra(NotificationService. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationService. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Admin.this. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }

    public void selectImage(View view) {

        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            imgUri  = data.getData();
            imageview.setImageURI(imgUri);
        }
    }
    // get the extension of file
    private String getFileEx(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
