package com.example.sharif.onlinebooking;

import static com.example.sharif.onlinebooking.Constant.setUserLoginStatus;
import static com.example.sharif.onlinebooking.Constant.setUseraddress;
import static com.example.sharif.onlinebooking.Constant.setUsername;
import static com.example.sharif.onlinebooking.Constant.setUsernumber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    EditText user_name,password,confirm_password,email,number,user_addres;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    ProgressBar progressBar;
    ImageView imageView;
    StorageReference mRef;
    private Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        number=findViewById(R.id.user_phone);
        user_name=findViewById(R.id.user_name);
        password=findViewById(R.id.user_pass);
        confirm_password=findViewById(R.id.user_confirmpassword);
        email=findViewById(R.id.user_email);
        user_addres=findViewById(R.id.user_addres);
        firebaseAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.pg);
        imageView=findViewById(R.id.image);
        mRef= FirebaseStorage.getInstance().getReference("profile_images");
    }

    public void backToLoginScreen(View view) {
        finish();
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(this,ForgotActivity.class));
    }

    public void login(View view) {
        finish();
    }

    public void registerUser(View view) {
        if(user_name.getText().toString().trim().isEmpty()){
            user_name.setError("required");
        }
        else if(password.getText().toString().trim().isEmpty()){
            password.setError("required");
        }
        else if(confirm_password.getText().toString().trim().isEmpty()){
            confirm_password.setError("required");
        }
        else if(email.getText().toString().trim().isEmpty()){
            email.setError("required");
        }
        else if(number.getText().toString().trim().isEmpty()){
            number.setError("required");
        }
        else if(user_addres.getText().toString().trim().isEmpty()){
            user_addres.setError("required");
        }
        else if(!password.getText().toString().trim().equals(confirm_password.getText().toString().toString())){
            Toast.makeText(RegisterActivity.this,"password and confirm password are not same",Toast.LENGTH_LONG).show();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        add();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private void add(){

        StorageReference storageReference = mRef.child(System.currentTimeMillis() + "." + getFileEx(imgUri));
        storageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(RegisterActivity.this, "register successful", Toast.LENGTH_LONG).show();
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();
                        myRef=  FirebaseDatabase.getInstance().getReference("user").child(user_name.getText().toString());
                        myRef.child("Name").setValue(user_name.getText().toString());
                        myRef.child("Mail").setValue(email.getText().toString());
                        myRef.child("Phone").setValue(number.getText().toString());
                        myRef.child("Address").setValue(user_addres.getText().toString());
                        myRef.child("Image").setValue(downloadUrl.toString());
                        setUsername(RegisterActivity.this,user_name.getText().toString());
                        setUserLoginStatus(RegisterActivity.this,true);
                        setUsernumber(RegisterActivity.this,number.getText().toString());
                        setUseraddress(RegisterActivity.this,user_addres.getText().toString());

                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        progressBar.setVisibility(View.GONE);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });

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
            imageView.setImageURI(imgUri);
        }
    }
    // get the extension of file
    private String getFileEx(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}