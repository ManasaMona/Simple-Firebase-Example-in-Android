package com.example.manasaa.firebaseexample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manasaa.firebaseexample.R;

import com.example.manasaa.firebaseexample.model.ListItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.manasaa.firebaseexample.MainActivity.DATABASE_NAME;

/**
 * Created by manasa.a on 24-03-2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    String TAG = MyAdapter.class.getSimpleName();
    private Context context;
    private List<ListItem> listItems;
    public MyAdapter(Context context, List<ListItem> listItems) {
        this.listItems = listItems;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(v,listItems,MyAdapter.this);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem upload = listItems.get(position);
        holder.textViewName.setText(upload.getName());
        holder.mListItem = upload;
        Glide.with(context).load(upload.getUrl()).into(holder.imageView);

    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public  void updateList(List<ListItem> uploads){

        listItems=uploads;
        notifyItemRangeChanged(0,listItems.size());
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        final String TAG =  ViewHolder.class.getSimpleName();
        public TextView textViewName;
        public ImageView imageView;
         public  ListItem mListItem;
        MyAdapter myAdapter;

        public ViewHolder(View itemView, List<ListItem> listItems, MyAdapter myAdapter) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewList);
//            itemView.setOnClickListener(this);
//            this.myAdapter=myAdapter;

        }

//        @Override
//        public void onClick(View v) {
//            myAdapter
//
//        }
    }
}
