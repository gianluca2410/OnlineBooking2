package com.example.sharif.onlinebooking;

import static com.example.sharif.onlinebooking.Constant.ADMIN;
import static com.example.sharif.onlinebooking.Constant.CONTACT;
import static com.example.sharif.onlinebooking.Constant.DELETE;
import static com.example.sharif.onlinebooking.Constant.INSERT;
import static com.example.sharif.onlinebooking.Constant.UPDATE;
import static com.example.sharif.onlinebooking.Constant.USER;
import static com.example.sharif.onlinebooking.Constant.getSelectedLanguage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Operation extends AppCompatActivity {
  RecyclerView recyclerView;
  ProgressBar progressBar;
    DatabaseReference dRef;
    List<Book> data = new ArrayList<Book>();
    CustomAdapter customAdapter;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        recyclerView = findViewById(R.id.list);
        progressBar=findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dRef=  FirebaseDatabase.getInstance().getReference("Books");
        builder = new AlertDialog.Builder(this);
        getData();
    }


    public void openInsertScreen(View view) {
        Intent intent=new Intent(Operation.this,Admin.class);
        startActivity(intent);
    }
    public void getData(){
        progressBar.setVisibility(View.VISIBLE);
        data.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Book donation =new Book(   postSnapshot.child("BookName").getValue(String.class),postSnapshot.child("BookAuthor").getValue(String.class)
                            , postSnapshot.child("BookPages").getValue(String.class), postSnapshot.child("BookWords").getValue(String.class)
                            ,postSnapshot.child("BookPrice").getValue(String.class), postSnapshot.child("BookEdition").getValue(String.class)
                            ,postSnapshot.child("BookCode").getValue(String.class),postSnapshot.child("BookImage").getValue(String.class));
                    data.add(donation);
                }
                customAdapter = new CustomAdapter(data, Operation.this);
                recyclerView.setAdapter(customAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ImageViewHoler> {
        List<Book> data = new ArrayList<Book>();
        private Context activity;

        public CustomAdapter(List<Book> dataset, Context activity) {
            this.data = dataset;
            this.activity = activity;
        }

        @NonNull
        @Override
        public CustomAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(activity).inflate(R.layout.grid, parent, false);
            return new CustomAdapter.ImageViewHoler(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.ImageViewHoler holder, int position) {
            Book cun = data.get(position);
            holder.B_name.setText(cun.getB_name());
            holder.A_name.setText(cun.getA_name());
            holder.B_page.setText(cun.getB_page());
            holder.B_words.setText(cun.getB_words());
            holder.B_price.setText(cun.getB_price());
            holder.B_edition.setText(cun.getB_edition());
            holder.B_code.setText(cun.B_code);
            Picasso.with(Operation.this)
                    .load(cun.getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Operation.this,UpdatOperation.class)
                            .putExtra("price",cun.getB_price())
                            .putExtra("page",cun.getB_page())
                            .putExtra("edition",cun.getB_edition())
                            .putExtra("words",cun.getB_words())
                            .putExtra("name",cun.getB_name()));
                }
            });
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    builder.setMessage("Are you sure")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dRef.child(cun.getB_name()).removeValue();
                                    getData();
                                }
                            });
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.setTitle("Delete Package");
                    alert.show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            TextView B_name, A_name, B_page, B_words, B_price, B_edition, B_code;
            ImageView imageView;
            CardView cardView;

            public ImageViewHoler(View itemView) {
                super(itemView);
                B_name = itemView.findViewById(R.id.bname);
                A_name = itemView.findViewById(R.id.auther);
                B_page = itemView.findViewById(R.id.pages);
                B_words = itemView.findViewById(R.id.number);
                B_price = itemView.findViewById(R.id.price);
                B_edition = itemView.findViewById(R.id.rating);
                B_code = itemView.findViewById(R.id.code1);
                imageView = itemView.findViewById(R.id.img);
                cardView = itemView.findViewById(R.id.card);
            }
        }
    }
}
