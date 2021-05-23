package com.example.techsnet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class expertRegistration extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference reference,removeReference;
    EditText email,name,pass,cpass;
    TextView tv;
    ProgressBar pb;
    Users users;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_registration);
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
        reference = database.getReference("Experts");
        removeReference = database.getReference("Tokens");
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

    }

    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null)
        {
            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void expertRegister(View view) {
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

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                removeReference.child(token).removeValue();
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
                Toast.makeText(getApplicationContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}