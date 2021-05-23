package com.example.techsnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetpassword extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
    }

    public void resetPassword(View view) {
        mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(resetpassword.this, "An email has been sent to your account!!.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), login.class));
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(resetpassword.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}