package com.example.manasaa.firebaseexample;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manasaa.firebaseexample.model.ListItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String STORAGE_FOLDER = "uploads/";
    public static final String DATABASE_NAME = "sample";
    private static final int PICK_IMAGE_REQUEST = 123;

    private EditText mEditText;
    private ImageView imageView;
    private Uri filePath;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mUploadButton = (Button) findViewById(R.id.buttonUpload);
        TextView mDisplayList = (TextView) findViewById(R.id.displayList);
        mUploadButton.setOnClickListener(this);
        mDisplayList.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.editText);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_NAME);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (filePath != null && mEditText.length()!=0) {
            //getting the storage reference
            StorageReference sRef = storageReference.child(STORAGE_FOLDER + System.currentTimeMillis() + "." + getFileExtension(filePath));
            sRef.putFile(filePath)  //adding the file to reference
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ListItem upload = new ListItem(mEditText.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(upload);
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.imageView:
                                            selectImage();
                                            break;
            case R.id.buttonUpload:
                                        uploadFile();
                                         break;
            case R.id.displayList:
                                    startActivity(new Intent(this, DisplaylistActivity.class));
                                    break;
        }
    }
}