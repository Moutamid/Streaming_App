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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

public class Edit_Channel extends AppCompatActivity {

    EditText channel_name , channel_des , channel_cast , channel_time , channel_link ,channel_id;
    Button channel_btn_add2;
    ImageView channel_img_add2;

    private static final int IMAGE_REQUEST = 1;
    Uri uri;
    ProgressDialog pd;
    String imageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_channel);

        pd = new ProgressDialog(this);
        pd.setTitle("Wait...");
        pd.setMessage("Test Uploading...");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        channel_name = findViewById(R.id.channel_name2);
        channel_id = findViewById(R.id.channel_id2);
        channel_des = findViewById(R.id.channel_des2);
        channel_cast = findViewById(R.id.channel_cast2);
        channel_time = findViewById(R.id.channel_time2);
        channel_link = findViewById(R.id.channel_link2);
        channel_btn_add2 = findViewById(R.id.channel_btn_add2);
        channel_img_add2 = findViewById(R.id.channel_img_add2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            channel_name.setText(bundle.getString("name"));
            channel_link.setText(bundle.getString("link"));
            channel_des.setText(bundle.getString("des"));
            channel_cast.setText(bundle.getString("cast"));
            channel_time.setText(bundle.getString("time"));
            channel_id.setText(bundle.getString("id"));
        }

        channel_img_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        channel_btn_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = channel_id.getText().toString().trim();
                String name = channel_name.getText().toString().trim();
                String des = channel_des.getText().toString().trim();
                String cast = channel_cast.getText().toString().trim();
                String time = channel_time.getText().toString().trim();
                String link = channel_link.getText().toString().trim();

                if (id.isEmpty()){
                    Toast.makeText(Edit_Channel.this, "Enter Unique Channel ID", Toast.LENGTH_SHORT).show();
                }
                else if (name.isEmpty()){
                    Toast.makeText(Edit_Channel.this, "Enter Channel Name", Toast.LENGTH_SHORT).show();
                }
                else if (des.isEmpty()){
                    Toast.makeText(Edit_Channel.this, "Enter Channel Description", Toast.LENGTH_SHORT).show();
                }
                else if (cast.isEmpty()){
                    Toast.makeText(Edit_Channel.this, "Enter Channel Cast", Toast.LENGTH_SHORT).show();
                }
                else if (time.isEmpty()){
                    Toast.makeText(Edit_Channel.this, "Enter Channel Time", Toast.LENGTH_SHORT).show();
                }
                else if (link.isEmpty()){
                    Toast.makeText(Edit_Channel.this, "Enter Channel Link", Toast.LENGTH_SHORT).show();
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
            channel_img_add2.setImageURI(uri);
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
                uploadData();
            }
        });
    }


    private void uploadData() {
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
                    Toast.makeText(Edit_Channel.this, "Test Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Edit_Channel.this, "Failed to Upload..." , Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }
}