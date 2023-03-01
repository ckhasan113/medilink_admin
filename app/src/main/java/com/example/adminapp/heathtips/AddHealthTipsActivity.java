package com.example.adminapp.heathtips;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.pojo.HealthTips;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddHealthTipsActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 848;
    private static final int PERMISSION_CODE = 8972;

    private ImageView addHealthImage;
    private EditText healthTipEdt, healthTipTitleEdt;
    private Button submitBtn, updateBtn, removeBtn;
    private TextView noImageTV;

    private DatabaseReference rootRef;
    private DatabaseReference tipRef;

    private String id, image, tip, title;

    private boolean isPermissionGranted = false;

    private Uri ImageUrl_main;

    private StorageReference storageReference;

    private LoadingDialog dialog;

    private HealthTips ht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_tips);

        ht = (HealthTips) getIntent().getSerializableExtra("HealthTips");

        dialog = new LoadingDialog(AddHealthTipsActivity.this, "Loading...");

        rootRef = FirebaseDatabase.getInstance().getReference();
        tipRef = rootRef.child("Admin").child("Health Tips");

        addHealthImage = findViewById(R.id.health_tips_image);
        healthTipEdt = findViewById(R.id.tips_edt);
        healthTipTitleEdt = findViewById(R.id.tips_title_edt);
        submitBtn = findViewById(R.id.submit_tip);
        updateBtn = findViewById(R.id.update_tip);
        removeBtn = findViewById(R.id.remove_tip);
        noImageTV = findViewById(R.id.health_tips_image_TV);

        if (ht != null){
            id = ht.getId();
            image = ht.getImage();
            tip = ht.getTips();
            title = ht.getTitle();
            ImageUrl_main = Uri.parse(image);

            updateBtn.setVisibility(View.VISIBLE);
            removeBtn.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.GONE);

            if (ht.getImage().equals("null")){
                noImageTV.setVisibility(View.VISIBLE);
                addHealthImage.setVisibility(View.GONE);
            }else {
                Picasso.get().load(Uri.parse(ht.getImage())).into(addHealthImage);
            }
            healthTipTitleEdt.setText(ht.getTitle());
            healthTipEdt.setText(ht.getTips());
        }

        noImageTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHealthImage.setVisibility(View.VISIBLE);
                noImageTV.setVisibility(View.GONE);
                addImage();
            }
        });

        addHealthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageUrl_main == null){
                    image = "null";
                }else {
                    image = String.valueOf(ImageUrl_main);
                }
                tip = healthTipEdt.getText().toString().trim();
                if (tip.isEmpty()){
                    healthTipEdt.setError(getString(R.string.required_field));
                    healthTipEdt.requestFocus();
                    return;
                }
                title = healthTipTitleEdt.getText().toString().trim();
                if (title.isEmpty()){
                    healthTipTitleEdt.setError(getString(R.string.required_field));
                    healthTipTitleEdt.requestFocus();
                    return;
                }
                id = tipRef.push().getKey();
                addHealthTips();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageUrl_main == null){
                    image = "null";
                }else {
                    image = String.valueOf(ImageUrl_main);
                }
                tip = healthTipEdt.getText().toString().trim();
                addHealthTips();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipRef.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddHealthTipsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddHealthTipsActivity.this, HealthTipsListActivity.class);
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    public void addImage(){
        checkPermission();
        if(isPermissionGranted){
            openGallery();
        }else {
            Toast.makeText(AddHealthTipsActivity.this, "Please Allow Permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void addHealthTips() {
        dialog.show();
        if (image.equals("null")){
            HealthTips ht = new HealthTips(id, image, tip, title);
            tipRef.child(id).child("Value").setValue(ht).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AddHealthTipsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddHealthTipsActivity.this, HealthTipsListActivity.class);
                    dialog.dismiss();
                    startActivity(intent);
                    finish();
                }
            });
        }else {
            storageReference = FirebaseStorage.getInstance().getReference();
            final Uri imageUri = ImageUrl_main;
            final StorageReference imageRef = storageReference.child("HealthTips").child(imageUri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    image = downloadUri.toString();
                    HealthTips ht = new HealthTips(id, image, tip, title);
                    tipRef.child(id).child("Value").setValue(ht).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddHealthTipsActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddHealthTipsActivity.this, HealthTipsListActivity.class);
                            dialog.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE){
            ImageUrl_main = data.getData();
            addHealthImage.setImageURI(ImageUrl_main);
        }
    }

    private void checkPermission() {
        if ((ActivityCompat
                .checkSelfPermission(AddHealthTipsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) &&
                (ActivityCompat
                        .checkSelfPermission(AddHealthTipsActivity.this
                                ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(AddHealthTipsActivity.this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },PERMISSION_CODE);

        }else {
            isPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==PERMISSION_CODE){

            if((grantResults[0] ==PackageManager.PERMISSION_GRANTED
                    && grantResults[1] ==PackageManager.PERMISSION_GRANTED
            )){
                isPermissionGranted = true;
            }else {
                checkPermission();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddHealthTipsActivity.this, HealthTipsListActivity.class);
        startActivity(intent);
        finish();
    }
}
