package com.example.adminapp.foreignDoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.ForeignHospitalDoctorListAdapter;
import com.example.adminapp.adapter.HospitalDoctorListAdapter;
import com.example.adminapp.hospital.HospitalCompletedBookingListActivity;
import com.example.adminapp.hospital.HospitalDoctorListActivity;
import com.example.adminapp.pojo.ForeignDoctorDetails;
import com.example.adminapp.pojo.ForeignHospitalDetails;
import com.example.adminapp.pojo.HospitalDetails;
import com.example.adminapp.pojo.HospitalDoctorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ForeignHospitalDoctorListActivity extends AppCompatActivity implements ForeignHospitalDoctorListAdapter.ForeignHospitalDoctorListAdapterListener{

    private DatabaseReference doctorRef;

    private ForeignHospitalDetails hospital;
    private String country;

    private LoadingDialog dialog;

    private TextView nameTV;

    private RecyclerView doctorRecycler;

    private List<ForeignDoctorDetails> list = new ArrayList<ForeignDoctorDetails>();

    private ForeignHospitalDoctorListAdapter adapter;

    private Button completedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_hospital_doctor_list);

        hospital = (ForeignHospitalDetails) getIntent().getSerializableExtra("ForeignHospitalDetails");
        country = getIntent().getStringExtra("ForeignHospitalCountry");

        dialog = new LoadingDialog(ForeignHospitalDoctorListActivity.this, "Loading...");
        dialog.show();

        doctorRef = FirebaseDatabase.getInstance().getReference().child("Foreign Doctor").child("Country").child(country).child("Hospital").child(hospital.getHospitalID()).child("DoctorList");

        nameTV = findViewById(R.id.admin_panel_foreign_hospital_doctor_toolbar_TV);
        doctorRecycler = findViewById(R.id.admin_panel_foreign_hospital_doctor_list_recycler);
        completedBtn = findViewById(R.id.completedBookingForeignHospitalBtn);

        nameTV.setText(hospital.getName());

        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ForeignDoctorDetails hdd = d.child("details").getValue(ForeignDoctorDetails.class);
                    list.add(hdd);
                }
                adapter = new ForeignHospitalDoctorListAdapter(ForeignHospitalDoctorListActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalDoctorListActivity.this);
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
                Intent intent = new Intent(ForeignHospitalDoctorListActivity.this, ForeignHospitalCompletedBookingListActivity.class);
                intent.putExtra("ForeignHospitalDetails", hospital);
                intent.putExtra("ForeignHospitalCountry", country);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForeignHospitalDoctorListActivity.this, ForeignHospitalListActivity.class));
        finish();
    }

    @Override
    public void onDoctorDetails(ForeignDoctorDetails doctorDetails) {
        Intent intent = new Intent(ForeignHospitalDoctorListActivity.this, ForeignHospitalDoctorBookingListActivity.class);
        intent.putExtra("ForeignHospitalDetails", hospital);
        intent.putExtra("ForeignHospitalDoctorDetails", doctorDetails);
        intent.putExtra("ForeignHospitalCountry", country);
        startActivity(intent);
        finish();
    }
}
