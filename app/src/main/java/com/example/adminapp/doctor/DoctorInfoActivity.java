package com.example.adminapp.doctor;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapter.DoctorListAdapter;
import com.example.adminapp.chamber.ChamberListActivity;
import com.example.adminapp.pojo.DoctorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorInfoActivity extends AppCompatActivity implements DoctorListAdapter.DoctorListAdapterListener {

    private Button chamberRequestBtn;

    private RecyclerView doctorRecycler;

    private DatabaseReference doctorRef;

    private List<DoctorDetails> doctorDetailsList = new ArrayList<DoctorDetails>();

    private DoctorListAdapter adapter;

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        dialog = new LoadingDialog(DoctorInfoActivity.this, "Loading....");
        dialog.show();
        doctorRef = FirebaseDatabase.getInstance().getReference().child("Doctor");

        chamberRequestBtn = findViewById(R.id.chamberRequestBtn);
        doctorRecycler = findViewById(R.id.admin_panel_doctor_recycler);

        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorDetailsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    DoctorDetails doc = d.child("Details").getValue(DoctorDetails.class);
                    doctorDetailsList.add(doc);
                }
                adapter = new DoctorListAdapter(DoctorInfoActivity.this, doctorDetailsList);
                LinearLayoutManager llm = new LinearLayoutManager(DoctorInfoActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                doctorRecycler.setLayoutManager(llm);
                doctorRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chamberRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorInfoActivity.this, ChamberListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoctorInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGetDoctorDetails(DoctorDetails details) {
        Intent intent = new Intent(DoctorInfoActivity.this, DoctorDetailsActivity.class);
        intent.putExtra("DoctorDetails", details);
        startActivity(intent);
        finish();
    }
}
