package com.alizainsolutions.textme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alizainsolutions.textme.Model.MessageModel;
import com.alizainsolutions.textme.R;

import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<MessageModel> arrayList = new ArrayList<>();
    public ChatRecyclerViewAdapter(Context context, ArrayList<MessageModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(getItemViewType(position) == 1){
            holder.senderMessageTV.setText(arrayList.get(position).getMessage());
            holder.senderTimeTV.setText(arrayList.get(position).getTime());
        }else {
            holder.receiverMessageTV.setText(arrayList.get(position).getMessage());
            holder.receiverTimeTV.setText(arrayList.get(position).getTime());
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView senderMessageTV, receiverMessageTV, senderTimeTV, receiverTimeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageTV = itemView.findViewById(R.id.senderMessageTV);
            receiverMessageTV = itemView.findViewById(R.id.receiverMessageTV);
            senderTimeTV = itemView.findViewById(R.id.senderTimeTV);
            receiverTimeTV = itemView.findViewById(R.id.receiverTimeTV);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getSender()) {
            return 1;
        } else {
            return 2;
        }
    }
}
