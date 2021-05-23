package com.example.techsnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText email,pass;
    FirebaseAuth mAuth;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        pb = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        if (mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }


    public void login(View view) {
        final String inpemail = email.getText().toString().trim(),inppass = pass.getText().toString().trim();

        if(TextUtils.isEmpty(inpemail)){
            email.setError("Email is Required.");
            return;
        }

        if(TextUtils.isEmpty(inppass)){
            pass.setError("Password is Required.");
            return;
        }

        if(inppass.length() < 6){
            pass.setError("Password Must be >= 6 Characters");
            return;
        }

        pb.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(inpemail,inppass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {

                    Toast.makeText(login.this, "Success", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);startActivity(new Intent(getApplicationContext(),MainActivity.class));

                }
                else {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void move_to_register(View view) {
        startActivity(new Intent(getApplicationContext(),register.class));
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(getApplicationContext(), resetpassword.class));
    }
}