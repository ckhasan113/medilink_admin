package com.example.adminapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;

import com.example.adminapp.ambulance.AmbulanceRequestActivity;
import com.example.adminapp.doctor.DoctorInfoActivity;
import com.example.adminapp.ePharmachy.EPharmachyListActivity;
import com.example.adminapp.foreignDoctor.ForeignHospitalListActivity;
import com.example.adminapp.heathtips.HealthTipsListActivity;
import com.example.adminapp.hospital.HospitalListActivity;
import com.example.adminapp.nurse.NurseVendorListActivity;
import com.example.adminapp.physiotherapist.PhysiotherapistVendorActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //private Button chamberBtn;

    private CardView doctorCard, hospitalCard, physiotherapistCard, healthTipsCard, ePharmacyCard, ambulanceCard, nurseCard, foreignCard;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootRef = FirebaseDatabase.getInstance().getReference();

        doctorCard = findViewById(R.id.doctor_list_admin_panel);
        healthTipsCard = findViewById(R.id.health_tips);
        hospitalCard = findViewById(R.id.hospital__list_admin_panel);
        physiotherapistCard = findViewById(R.id.physiotherapist_list_admin_panel);
        ePharmacyCard = findViewById(R.id.ePharmacy_admin_panel);
        ambulanceCard = findViewById(R.id.ambulance_admin_panel);
        nurseCard = findViewById(R.id.nurse_admin_panel);
        foreignCard = findViewById(R.id.foreign_appointment_admin_panel);

        doctorCard.setOnClickListener(this);
        healthTipsCard.setOnClickListener(this);
        hospitalCard.setOnClickListener(this);
        physiotherapistCard.setOnClickListener(this);
        ePharmacyCard.setOnClickListener(this);
        ambulanceCard.setOnClickListener(this);
        nurseCard.setOnClickListener(this);
        foreignCard.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify1", "notification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("admin");

        /*chamberBtn = findViewById(R.id.chamber);

        chamberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChamberListActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("device_token", "");
        rootRef.child("Users").child("adminID").child("Token").updateChildren(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.doctor_list_admin_panel){
            Intent intent = new Intent(MainActivity.this, DoctorInfoActivity.class);
            startActivity(intent);
            finish();
        }else if (view.getId() == R.id.hospital__list_admin_panel){
            Intent intent = new Intent(MainActivity.this, HospitalListActivity.class);
            startActivity(intent);
            finish();
        }else if (view.getId() == R.id.health_tips){
            Intent intent = new Intent(MainActivity.this, HealthTipsListActivity.class);
            startActivity(intent);
            finish();
        }else if (view.getId() == R.id.physiotherapist_list_admin_panel){
            Intent intent = new Intent(MainActivity.this, PhysiotherapistVendorActivity.class);
            startActivity(intent);
            finish();
        }else if (view.getId() == R.id.ePharmacy_admin_panel){
            Intent intent = new Intent(MainActivity.this, EPharmachyListActivity.class);
            startActivity(intent);
            finish();
        }else if (view.getId() == R.id.ambulance_admin_panel){
            Intent intent = new Intent(MainActivity.this, AmbulanceRequestActivity.class);
            startActivity(intent);
            finish();
        }else if (view.getId() == R.id.nurse_admin_panel){
            Intent intent = new Intent(MainActivity.this, NurseVendorListActivity.class);
            startActivity(intent);
            finish();
        }else if (view.getId() == R.id.foreign_appointment_admin_panel){
            Intent intent = new Intent(MainActivity.this, ForeignHospitalListActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
