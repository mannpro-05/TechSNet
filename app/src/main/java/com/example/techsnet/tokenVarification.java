package com.example.techsnet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Iterator;


public class tokenVarification extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseAuth mAuth;
    TextView tokenInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_varification);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        reference = FirebaseDatabase.getInstance().getReference("Tokens");
        tokenInput = findViewById(R.id.token);
    }

    public static class Token {
        public String name;
        public Token(){
            //
        }
        // getters and setters...
    }


    public void validateToken(View view) {
        if (TextUtils.isEmpty(tokenInput.getText()))
        {
            tokenInput.setError("This field is required!!!");
        }
        else{
            int flag = 0;
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println(snapshot.getChildrenCount());
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Token token = snapshot1.getValue(Token.class);
                        if (token.name.equals(tokenInput.getText().toString())){
                            Intent intent = new Intent(getApplicationContext(), expertRegistration.class);
                            intent.putExtra("token",snapshot1.getKey());
                            startActivity(intent);
                            finish();
                        }
                    }
                    //Toast.makeText(tokenVarification.this, "Please enter a valid token!!!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }
}