package com.alizainsolutions.textme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    EditText signUpEmail, signUpPassword;
    Button signUpBtn;
    TextView alreadyAccountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Sign Up");



        signUpBtn = findViewById(R.id.signUpBtn);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        alreadyAccountTV = findViewById(R.id.alreadyAccountTV);

        signUpBtn.setOnClickListener(v ->{
            String emailStr = signUpEmail.getText().toString().trim();
            String passwordStr = signUpPassword.getText().toString().trim();
            if(emailStr.isEmpty() || passwordStr.isEmpty()){
                Toast.makeText(this, "Please enter email and password!", Toast.LENGTH_SHORT).show();
            }else{
                signUp(emailStr, passwordStr);
            }
        });

        alreadyAccountTV.setOnClickListener(v ->{
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

    }

    void signUp(String email, String password){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, CreateProfileActivity.class));
                    finishAffinity();
                }else {
                    Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}