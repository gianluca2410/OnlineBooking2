package com.example.sharif.onlinebooking;

import static com.example.sharif.onlinebooking.Constant.*;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    RecyclerView recyclerView;
    List<Book> data = new ArrayList<Book>();
    int request_call = 1;
    DatabaseReference dRef,databaseReference;
    CustomAdapter customAdapter;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView = view.findViewById(R.id.list);
        progressBar=view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dRef=  FirebaseDatabase.getInstance().getReference("Books");

       getData();
        return view;
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
                customAdapter = new CustomAdapter(data, getContext());
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
            Picasso.with(getContext())
                    .load(cun.getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Buy book......");
                    builder.setMessage("Click ok to order the book \nfree home delivery...");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            databaseReference=  FirebaseDatabase.getInstance().getReference("UserBooking").child(getUsername(getContext()));
                            databaseReference.child("UserName").setValue(getUsername(getContext()));
                            databaseReference.child("UserNumber").setValue(getUsernumber(getContext()));
                            databaseReference.child("UserAddress").setValue(getUseraddress(getContext()));
                            databaseReference.child("BookName").setValue(cun.getB_name());
                            databaseReference.child("BookAuthor").setValue(cun.getA_name());
                            databaseReference.child("BookEdition").setValue(cun.getB_edition());
                            Toast.makeText(getContext(),"Thank for ordering",Toast.LENGTH_LONG).show();

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    builder.show();
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