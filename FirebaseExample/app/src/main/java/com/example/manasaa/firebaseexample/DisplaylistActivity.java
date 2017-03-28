package com.example.manasaa.firebaseexample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.manasaa.firebaseexample.adapter.MyAdapter;
import com.example.manasaa.firebaseexample.listener.ClickListener;
import com.example.manasaa.firebaseexample.listener.RecyclerTouchListener;
import com.example.manasaa.firebaseexample.model.ListItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.example.manasaa.firebaseexample.MainActivity.DATABASE_NAME;


public class DisplaylistActivity extends AppCompatActivity {
    final String TAG = DisplaylistActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<ListItem> uploads;
    private ListItem deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"called onCreate");
        setContentView(R.layout.activity_show_images);

        uploads = new ArrayList<>();
        getDataFromFirebase();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getApplicationContext(), uploads);
        recyclerView.setAdapter(adapter);

       recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
           @Override
           public void onClick(View view, final int position) {
               Toast.makeText(getApplicationContext(), "called Onclick", Toast.LENGTH_LONG).show();
                deleteItem = uploads.get(position);
               Log.d(TAG, "called "+deleteItem.getName());
               DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_NAME);
               mDatabase.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot snapshot) {
                       for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                           ListItem upload = postSnapshot.getValue(ListItem.class);
                           Log.d(TAG,"called "+ postSnapshot.getKey()+upload.getName()+" name ");
                           if (upload.getName().equals(deleteItem.getName())){
                               Log.d(TAG, "called if true ");
                               postSnapshot.getRef().removeValue();
                               uploads.remove(position);
                               adapter.updateList(uploads);
                           }
                       }
                   }
                   @Override
                   public void onCancelled(DatabaseError databaseError) {
                   }
               });

               deleteImageFromStorage(deleteItem.getUrl());
           }
       }));
    }

    private void deleteImageFromStorage(String url) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference photoRef = storage.getReferenceFromUrl(url);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, " called deleted ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, " called  not delete ");
            }
        });

    }

    private void getDataFromFirebase() {
        Log.d(TAG,"called getDataFromFirebase");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_NAME);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ListItem upload = postSnapshot.getValue(ListItem.class);
                    uploads.add(upload);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}