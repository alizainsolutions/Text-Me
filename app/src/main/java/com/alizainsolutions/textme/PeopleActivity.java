package com.alizainsolutions.textme;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alizainsolutions.textme.Adapter.PeopleRecyclerViewAdapter;
import com.alizainsolutions.textme.Model.PeopleModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PeopleActivity extends AppCompatActivity {
    RecyclerView peopleRecyclerView;
    ArrayList<PeopleModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        getSupportActionBar().setTitle("People");
        arrayList = new ArrayList<>();
        peopleRecyclerView = findViewById(R.id.peopleRecyclerView);
        peopleRecyclerView.setHasFixedSize(true);
        peopleRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        if(!document.getId().equals(FirebaseAuth.getInstance().getUid())){
                            arrayList.add(new PeopleModel(document.getString("userName"), document.getString("userBio"), document.getId()));
                            peopleRecyclerView.setAdapter(new PeopleRecyclerViewAdapter(PeopleActivity.this, arrayList));
                        }

                    }
                }
            }
        });

    }
}