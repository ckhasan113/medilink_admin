package com.example.adminapp.hospital;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.HospitalDoctorListAdapter;
import com.example.adminapp.pojo.HospitalDetails;
import com.example.adminapp.pojo.HospitalDoctorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HospitalDoctorListActivity extends AppCompatActivity implements HospitalDoctorListAdapter.HospitalDoctorListAdapterListener {

    private DatabaseReference doctorRef;

    private HospitalDetails hospital;

    private LoadingDialog dialog;

    private TextView nameTV;

    private RecyclerView doctorRecycler;

    private List<HospitalDoctorDetails> list = new ArrayList<HospitalDoctorDetails>();

    private HospitalDoctorListAdapter adapter;

    private Button completedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_doctor_list);

        hospital = (HospitalDetails) getIntent().getSerializableExtra("HospitalDetails");

        dialog = new LoadingDialog(HospitalDoctorListActivity.this, "Loading...");
        dialog.show();

        doctorRef = FirebaseDatabase.getInstance().getReference().child("Hospital").child(hospital.getHospitalID()).child("DoctorList");

        nameTV = findViewById(R.id.admin_panel_hospital_doctor_toolbar_TV);
        doctorRecycler = findViewById(R.id.admin_panel_hospital_doctor_list_recycler);
        completedBtn = findViewById(R.id.completedBookingHospitalBtn);

        nameTV.setText(hospital.getName());

        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HospitalDoctorDetails hdd = d.child("Details").getValue(HospitalDoctorDetails.class);
                    list.add(hdd);
                }
                adapter = new HospitalDoctorListAdapter(HospitalDoctorListActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(HospitalDoctorListActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                doctorRecycler.setLayoutManager(llm);
                doctorRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalDoctorListActivity.this, HospitalCompletedBookingListActivity.class);
                intent.putExtra("HospitalDetails", hospital);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalDoctorListActivity.this, HospitalListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDoctorDetails(HospitalDoctorDetails doctorDetails) {
        Intent intent = new Intent(HospitalDoctorListActivity.this, HospitalDoctorPendingBookingActivity.class);
        intent.putExtra("HospitalDetails", hospital);
        intent.putExtra("HospitalDoctorDetails", doctorDetails);
        startActivity(intent);
        finish();
    }
}
