package com.example.techsnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference reference,removeReference;
    TextView name;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);

        if (mAuth.getCurrentUser() == null)
        {
            Intent intent =new Intent(getApplicationContext(),register.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = getIntent();
            System.out.println(intent.getStringExtra("user"));
            builder = new AlertDialog.Builder(this);
            reference = FirebaseDatabase.getInstance().getReference("Experts");
            removeReference = FirebaseDatabase.getInstance().getReference("Tokens");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(mAuth.getUid()))
                    {
                        reference = reference.child(mAuth.getUid());
                    }
                    else {
                        reference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
                    }

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            name.setText(snapshot.child("name").getValue().toString());

                        }

                        @Override
                        public void onCancelled(@NonNull  DatabaseError error) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error) {

                }
            });

            removeReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull  DataSnapshot snapshot) {
                    System.out.println(snapshot.getValue());
                    System.out.println(snapshot.child("-MaCnY837GaD_PORqATQ").getValue());
                }

                @Override
                public void onCancelled(@NonNull  DatabaseError error) {

                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println("hello MAnn"+ item.getItemId());
        switch (item.getItemId()){
            case R.id.logout:
                builder.setMessage("Are you sure that you want to logout ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), login.class));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("logout.");
                alertDialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        builder.setMessage("Are you sure that you want to exit ?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                System.exit(0);
            }
        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Exit the Application");
        alert.show();
    }
}