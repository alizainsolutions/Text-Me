package com.alizainsolutions.textme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateProfileActivity extends AppCompatActivity {

    EditText userName, userBio;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setTitle("Create Profile");
        userName = findViewById(R.id.userName);
        userBio = findViewById(R.id.userBio);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v ->{
            String userNameStr = userName.getText().toString().trim();
            String userBioStr = userBio.getText().toString().trim();
            if(userNameStr.isEmpty() || userBioStr.isEmpty()){
                Toast.makeText(this, "Please enter username and bio!", Toast.LENGTH_SHORT).show();
            }else{
                createProfile(userNameStr, userBioStr);
            }
        });

    }

    void createProfile(String userName, String userBio){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("userName", userName);
        user.put("bio", userBio);
        user.put("userId", userId);

        FirebaseFirestore.getInstance().collection("users").document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CreateProfileActivity.this, "Profile Created!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateProfileActivity.this, MainActivity.class));
                    finishAffinity();
                }else {
                    Toast.makeText(CreateProfileActivity.this, "Failed to create profile!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}