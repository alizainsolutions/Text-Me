package com.alizainsolutions.textme;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alizainsolutions.textme.Adapter.ChatRecyclerViewAdapter;
import com.alizainsolutions.textme.Model.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    ArrayList<MessageModel> arrayList;
    EditText messageET;
    ImageView sendMessageBtn;
    String chatID;
    ChatRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageET = findViewById(R.id.messageET);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        arrayList = new ArrayList<>();

        String receiverID = getIntent().getStringExtra("userId");
        String receiverName = getIntent().getStringExtra("username");
        getSupportActionBar().setTitle(receiverName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String senderID = FirebaseAuth.getInstance().getUid();
        assert senderID != null;
        assert receiverID != null;

        //online and last seen logic
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(receiverID)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null || documentSnapshot == null || !documentSnapshot.exists()) return;

                    String status = documentSnapshot.getString("online");
                    String lastSeen = documentSnapshot.getString("lastSeen");

                    if ("online".equalsIgnoreCase(status)) {
                        getSupportActionBar().setSubtitle("Online");
                    } else if (lastSeen != null && !lastSeen.isEmpty()) {
                        getSupportActionBar().setSubtitle("Last seen: " + lastSeen);
                    }
                });

        //set sender as online
        String currentUserId = FirebaseAuth.getInstance().getUid();
        if (currentUserId != null) {

            // Prepare update
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("online", "online");

            // Update in Firestore
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUserId)
                    .update(updateData);

        }









        chatID = (senderID.compareTo(receiverID) < 0) ? senderID + receiverID : receiverID + senderID;

        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatRecyclerViewAdapter(ChatActivity.this, arrayList);
        chatRecyclerView.setAdapter(adapter);

        // Real-time message listener
        FirebaseFirestore.getInstance()
                .collection("chats")
                .document(chatID)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null || value == null) return;

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                String senderId = dc.getDocument().getString("senderId");
                                String receiverId = dc.getDocument().getString("receiverId");
                                String message = dc.getDocument().getString("message");
                                Date timestamp = dc.getDocument().getDate("timestamp");
                                boolean isSender = senderID.equals(senderId);

                                // Format time as 12-hour with AM/PM
                                String formattedTime = "";
                                if (timestamp != null) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                    formattedTime = sdf.format(timestamp);
                                } else {
                                    // fallback time while timestamp updates from server
                                    formattedTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                                }

                                arrayList.add(new MessageModel(senderId, receiverId, message, formattedTime, isSender));
                                adapter.notifyItemInserted(arrayList.size() - 1);
                                chatRecyclerView.scrollToPosition(arrayList.size() - 1);
                            }
                        }
                    }
                });

        // Send message
        sendMessageBtn.setOnClickListener(v -> {
            String msg = messageET.getText().toString().trim();
            if (!msg.isEmpty()) {
                Map<String, Object> messageData = new HashMap<>();
                messageData.put("senderId", senderID);
                messageData.put("receiverId", receiverID);
                messageData.put("message", msg);
                messageData.put("timestamp", FieldValue.serverTimestamp());

                FirebaseFirestore.getInstance()
                        .collection("chats")
                        .document(chatID)
                        .collection("messages")
                        .add(messageData);

                messageET.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

        String currentUserId = FirebaseAuth.getInstance().getUid();
        if (currentUserId != null) {
            // Get current time
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            String formattedTime = sdf.format(now);

            // Prepare update
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("lastSeen", formattedTime);
            updateData.put("online", "offline");

            // Update in Firestore
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUserId)
                    .update(updateData);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        String currentUserId = FirebaseAuth.getInstance().getUid();
        if (currentUserId != null) {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("online", "online");

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUserId)
                    .update(updateData);
        }
    }


}
