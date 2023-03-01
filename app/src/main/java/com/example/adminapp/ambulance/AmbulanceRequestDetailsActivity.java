package com.example.adminapp.ambulance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.Request_Completed_Dialog;
import com.example.adminapp.Request_Remove_Dialog;
import com.example.adminapp.pojo.AmbulanceTaken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AmbulanceRequestDetailsActivity extends AppCompatActivity implements Request_Completed_Dialog.RequestDoneDialogListener, Request_Remove_Dialog.RequestRemoveDialogListener {

    private AmbulanceTaken ambulance;

    private ImageView image;
    private TextView nameTV, addressTV, phoneTV, mailTV, typeTV, fromTV, toTV, priceTV, dateTV;
    private LinearLayout phoneLO, mailLO;
    private Button completeBtn, removeBtn;

    private LoadingDialog dialog;

    private DatabaseReference rootRef;
    private DatabaseReference ambulanceRef;
    private DatabaseReference clientRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_request_details);

        ambulance = (AmbulanceTaken) getIntent().getSerializableExtra("RequestDetails");

        dialog = new LoadingDialog(AmbulanceRequestDetailsActivity.this, "Loading...");

        rootRef = FirebaseDatabase.getInstance().getReference();
        ambulanceRef = rootRef.child("Ambulance");
        clientRef = rootRef.child("Patient").child(ambulance.getPatientDetails().getPatient_id()).child("Ambulance Taken");

        image = findViewById(R.id.ambulance_request_details_user_ProfileImage);

        nameTV = findViewById(R.id.ambulance_request_details_user_NameTV);
        addressTV = findViewById(R.id.ambulance_request_details_user_address_tv);
        phoneTV = findViewById(R.id.ambulance_request_details_user_phone_tv);
        mailTV = findViewById(R.id.ambulance_request_details_user_email_tv);
        typeTV = findViewById(R.id.ambulance_request_details_ambulance_type_tv);
        fromTV = findViewById(R.id.ambulance_request_details_ambulance_from_tv);
        toTV = findViewById(R.id.ambulance_request_details_ambulance_to_tv);
        priceTV = findViewById(R.id.ambulance_request_details_ambulance_price_tv);
        dateTV = findViewById(R.id.ambulance_request_details_ambulance_date_tv);

        phoneLO = findViewById(R.id.ambulance_request_details_user_phone_lo);
        mailLO = findViewById(R.id.ambulance_request_details_user_mail_lO);

        completeBtn = findViewById(R.id.ambulance_request_details_completed_btn);
        removeBtn = findViewById(R.id.ambulance_request_details_remove_btn);

        Picasso.get().load(Uri.parse(ambulance.getPatientDetails().getImageURI())).into(image);
        nameTV.setText("Mr. " + ambulance.getPatientDetails().getFirstName() + " " + ambulance.getPatientDetails().getLastName());
        phoneTV.setText(ambulance.getPatientDetails().getPhone());
        mailTV.setText(ambulance.getPatientDetails().getEmail());
        typeTV.setText(ambulance.getAmbulanceType());
        fromTV.setText(ambulance.getFrom());
        toTV.setText(ambulance.getTo());
        priceTV.setText(ambulance.getPrice());
        dateTV.setText(ambulance.getDate());

        if (ambulance.getStatus().equals("Completed")) {
            completeBtn.setVisibility(View.GONE);
            removeBtn.setVisibility(View.VISIBLE);
        }

        phoneLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + ambulance.getPatientDetails().getPhone().trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        return;
                    }
                }
                startActivity(intent);
            }
        });

        mailLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + ambulance.getPatientDetails().getEmail());
                intent.setData(data);
                startActivity(intent);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request_Remove_Dialog removedDialog = new Request_Remove_Dialog();
                removedDialog.show(getSupportFragmentManager(), "Confirmation: ");
            }
        });

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Request_Completed_Dialog completedDialog = new Request_Completed_Dialog();
                completedDialog.show(getSupportFragmentManager(), "Confirmation: ");

            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(AmbulanceRequestDetailsActivity.this, AmbulanceRequestActivity.class));
        finish();
    }

    @Override
    public void onSubmit() {
        dialog.show();
        ambulance.setStatus("Completed");
        clientRef.child(ambulance.getId()).child("Details").setValue(ambulance).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ambulanceRef.child("All Completed").child(ambulance.getId()).child("Details").setValue(ambulance).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ambulanceRef.child("Ambulance Type").child(ambulance.getAmbulanceType()).child("Completed").child(ambulance.getId()).child("Details").setValue(ambulance).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ambulanceRef.child("Ambulance Type").child(ambulance.getAmbulanceType()).child("Request").child(ambulance.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        ambulanceRef.child("All Request").child(ambulance.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                dialog.dismiss();
                                                startActivity(new Intent(AmbulanceRequestDetailsActivity.this, AmbulanceRequestActivity.class));
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onRemove() {
        dialog.show();
        clientRef.child(ambulance.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ambulanceRef.child("All Completed").child(ambulance.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ambulanceRef.child("Ambulance Type").child(ambulance.getAmbulanceType()).child("Completed").child(ambulance.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                startActivity(new Intent(AmbulanceRequestDetailsActivity.this, AmbulanceRequestActivity.class));
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
}
