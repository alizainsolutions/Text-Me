package com.alizainsolutions.textme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alizainsolutions.textme.ChatActivity;
import com.alizainsolutions.textme.Model.PeopleModel;
import com.alizainsolutions.textme.R;

import java.util.ArrayList;

public class PeopleRecyclerViewAdapter extends RecyclerView.Adapter<PeopleRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<PeopleModel> arrayList = new ArrayList<>();
    public PeopleRecyclerViewAdapter(Context context, ArrayList<PeopleModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.people_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.peopleName.setText(arrayList.get(position).getUserName());
        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("userId", arrayList.get(position).getUserId());
            intent.putExtra("username", arrayList.get(position).getUserName());
            context.startActivity(intent);
//            context.startActivity(new Intent(context, ChatActivity.class).putExtra("userId", arrayList.get(position).getUserId()));

        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView peopleName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            peopleName = itemView.findViewById(R.id.peopleName);
        }
    }
}
