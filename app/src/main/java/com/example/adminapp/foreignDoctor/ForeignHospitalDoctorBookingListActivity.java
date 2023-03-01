package com.example.adminapp.foreignDoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.ForeignHospitalBookingListAdapter;
import com.example.adminapp.adapter.HospitalBookingListAdapter;
import com.example.adminapp.hospital.HospitalDoctorListActivity;
import com.example.adminapp.hospital.HospitalDoctorPendingBookingActivity;
import com.example.adminapp.pojo.ForeignBooking;
import com.example.adminapp.pojo.ForeignDoctorDetails;
import com.example.adminapp.pojo.ForeignHospitalDetails;
import com.example.adminapp.pojo.HospitalBooking;
import com.example.adminapp.pojo.HospitalDetails;
import com.example.adminapp.pojo.HospitalDoctorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForeignHospitalDoctorBookingListActivity extends AppCompatActivity {

    private ForeignHospitalDetails hospital;
    private ForeignDoctorDetails doctorDetails;
    private String country;

    private TextView runningTV, completedTV, nameTV;

    private DatabaseReference rootRef;
    private DatabaseReference pendingRef;
    private DatabaseReference completedRef;

    private List<ForeignBooking> bookingList = new ArrayList<ForeignBooking>();

    private ForeignHospitalBookingListAdapter adapter;

    private RecyclerView recyclerView;

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foregin_hospital_doctor_booking_list);

        hospital = (ForeignHospitalDetails) getIntent().getSerializableExtra("ForeignHospitalDetails");
        doctorDetails = (ForeignDoctorDetails) getIntent().getSerializableExtra("ForeignHospitalDoctorDetails");
        country = getIntent().getStringExtra("ForeignHospitalCountry");

        dialog = new LoadingDialog(ForeignHospitalDoctorBookingListActivity.this, "Loading...");
        dialog.show();

        nameTV = findViewById(R.id.admin_panel_foreign_hospital_doctor_name_TV);
        runningTV = findViewById(R.id.foreignBookingPendingList);
        completedTV = findViewById(R.id.foreignBookingCompletedList);
        recyclerView = findViewById(R.id.admin_panel_foreign_hospital_completed_or_pending_list_recycler);

        nameTV.setText(doctorDetails.getName());

        rootRef = FirebaseDatabase.getInstance().getReference().child("Foreign Doctor").child("Country").child(country).child("Hospital").child(hospital.getHospitalID()).child("DoctorList").child(doctorDetails.getDoctorID());
        pendingRef = rootRef.child("Pending");
        completedRef = rootRef.child("complete");

        runningTV.setBackgroundColor(Color.parseColor("#FF5722"));
        completedTV.setBackgroundColor(Color.parseColor("#ECD0C8"));

        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ForeignBooking hb  = d.child("Value").getValue(ForeignBooking.class);
                    bookingList.add(hb);
                }
                Collections.reverse(bookingList);
                adapter = new ForeignHospitalBookingListAdapter(ForeignHospitalDoctorBookingListActivity.this, bookingList);
                LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalDoctorBookingListActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        runningTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runningTV.setBackgroundColor(Color.parseColor("#FF5722"));
                completedTV.setBackgroundColor(Color.parseColor("#ECD0C8"));

                pendingRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bookingList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            ForeignBooking hb  = d.child("Value").getValue(ForeignBooking.class);
                            bookingList.add(hb);
                        }
                        Collections.reverse(bookingList);
                        adapter = new ForeignHospitalBookingListAdapter(ForeignHospitalDoctorBookingListActivity.this, bookingList);
                        LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalDoctorBookingListActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setAdapter(adapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        completedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runningTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                completedTV.setBackgroundColor(Color.parseColor("#FF5722"));

                completedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bookingList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            ForeignBooking hb  = d.child("Value").getValue(ForeignBooking.class);
                            bookingList.add(hb);
                        }
                        Collections.reverse(bookingList);
                        adapter = new ForeignHospitalBookingListAdapter(ForeignHospitalDoctorBookingListActivity.this, bookingList);
                        LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalDoctorBookingListActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setAdapter(adapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForeignHospitalDoctorBookingListActivity.this, ForeignHospitalDoctorListActivity.class);
        intent.putExtra("ForeignHospitalDetails", hospital);
        intent.putExtra("ForeignHospitalCountry", country);
        startActivity(intent);
        finish();
    }
}
