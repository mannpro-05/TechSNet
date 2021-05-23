package com.example.techsnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class register extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference reference;
    EditText email,name,pass,cpass;
    TextView tv;
    ProgressBar pb;
    Users users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        pb = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv = findViewById(R.id.textView);
        cpass = findViewById(R.id.cpassword);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");
        System.out.println(reference);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null)
        {
            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void move_to_login(View view) {
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }

    public void expertRegistration(View view) {
        startActivity(new Intent(getApplicationContext(),tokenVarification.class));
        finish();
    }

    public void register(View view) {
        final String inpemail = email.getText().toString().trim(),inpname = name.getText().toString().trim(),
                inppass = pass.getText().toString().trim(),inpcpass = cpass.getText().toString().trim();

        if(TextUtils.isEmpty(inpemail)){
            email.setError("Email is Required.");
            return;
        }

        if(TextUtils.isEmpty(inppass)){
            pass.setError("Password is Required.");
            return;
        }

        if(inppass.length() < 6 ){
            pass.setError("Password Must be >= 6 Characters");
            return;
        }
        if ( inpcpass.length() < 6)
        {
            cpass.setError("Password Must be >= 6 Characters");
            return;
        }
        if (TextUtils.isEmpty(inpname))
        {
            name.setError("Name is Required!");
            return;
        }
        if (TextUtils.isEmpty(inpcpass))
        {
            cpass.setError("Field is required");
            return;
        }
        if (! inppass.equals(inpcpass))
        {
            pass.setError("The Passwords Do not match");
            pass.setFocusable(true);
            return;
        }

        pb.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(inpemail,inppass).addOnCompleteListener(task -> {

            if (task.isSuccessful())
            {

                Toast.makeText(register.this, "Success", Toast.LENGTH_SHORT).show();
                users = new Users();
                users.setEmail(inpemail);
                users.setName(inpname);
                reference.child(mAuth.getCurrentUser().getUid()).setValue(users);
                pb.setVisibility(View.GONE);
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
            else {
                pb.setVisibility(View.GONE);
                Toast.makeText(register.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}