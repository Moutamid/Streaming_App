package com.moutamid.streaming_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Add_Channel extends AppCompatActivity {

    EditText channel_name , channel_des , channel_cast , channel_time , channel_link ,channel_id;
    Button channel_btn_add;
    ImageView channel_img;

    private static final int IMAGE_REQUEST = 1;
    Uri uri;
    ProgressDialog pd;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);

        pd = new ProgressDialog(this);
        pd.setTitle("Wait...");
        pd.setMessage("Test Uploading...");

        channel_name = findViewById(R.id.channel_name);
        channel_id = findViewById(R.id.channel_id);
        channel_des = findViewById(R.id.channel_des);
        channel_cast = findViewById(R.id.channel_cast);
        channel_time = findViewById(R.id.channel_time);
        channel_link = findViewById(R.id.channel_link);
        channel_img = findViewById(R.id.channel_img_add);
        channel_btn_add = findViewById(R.id.channel_btn_add);

        channel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        // 3rd btn for submit all data using firebase
        channel_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = channel_id.getText().toString().trim();
                String name = channel_name.getText().toString().trim();
                String des = channel_des.getText().toString().trim();
                String cast = channel_cast.getText().toString().trim();
                String time = channel_time.getText().toString().trim();
                String link = channel_link.getText().toString().trim();

                if (id.isEmpty()){
                    Toast.makeText(Add_Channel.this, "Enter Unique Channel ID", Toast.LENGTH_SHORT).show();
                }
                else if (name.isEmpty()){
                    Toast.makeText(Add_Channel.this, "Enter Channel Name", Toast.LENGTH_SHORT).show();
                }
                else if (des.isEmpty()){
                    Toast.makeText(Add_Channel.this, "Enter Channel Description", Toast.LENGTH_SHORT).show();
                }
                else if (cast.isEmpty()){
                    Toast.makeText(Add_Channel.this, "Enter Channel Cast", Toast.LENGTH_SHORT).show();
                }
                else if (time.isEmpty()){
                    Toast.makeText(Add_Channel.this, "Enter Channel Time", Toast.LENGTH_SHORT).show();
                }
                else if (link.isEmpty()){
                    Toast.makeText(Add_Channel.this, "Enter Channel Link", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadImage();
                }
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            uri = data.getData();
            channel_img.setImageURI(uri);
        }
        else {
            Toast.makeText(this, "No Image is Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImage(){
        pd.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Channel_Images").child(uri.getLastPathSegment());
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uriImage = uriTask.getResult();
                imageURL = uriImage.toString();
                uploadTest();
            }
        });
    }

    private void uploadTest(){
        pd.show();
        String check = channel_id.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Channels_app2").child(check);
        HashMap<String, String> hashMap = new HashMap<>();
        String id = channel_id.getText().toString().trim();
        String name = channel_name.getText().toString();
        String des = channel_des.getText().toString();
        String cast = channel_cast.getText().toString();
        String time = channel_time.getText().toString();
        String link = channel_link.getText().toString();

        hashMap.put("id", id);
        hashMap.put("name", name);
        hashMap.put("des", des);
        hashMap.put("cast", cast);
        hashMap.put("time", time);
        hashMap.put("link", link);
        hashMap.put("image1", imageURL);

        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Toast.makeText(Add_Channel.this, "Test Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_Channel.this, "Failed to Upload..." , Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }
}