package com.example.adminapp.hospital;

import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.HospitalBookingListAdapter;
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

public class HospitalDoctorPendingBookingActivity extends AppCompatActivity {

    private HospitalDetails hospital;
    private HospitalDoctorDetails doctorDetails;

    private TextView runningTV, completedTV, nameTV;

    private DatabaseReference rootRef;
    private DatabaseReference pendingRef;
    private DatabaseReference completedRef;

    private List<HospitalBooking> bookingList = new ArrayList<HospitalBooking>();

    private HospitalBookingListAdapter adapter;

    private RecyclerView recyclerView;

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_doctor_pending_booking);

        hospital = (HospitalDetails) getIntent().getSerializableExtra("HospitalDetails");
        doctorDetails = (HospitalDoctorDetails) getIntent().getSerializableExtra("HospitalDoctorDetails");

        dialog = new LoadingDialog(HospitalDoctorPendingBookingActivity.this, "Loading...");
        dialog.show();

        nameTV = findViewById(R.id.admin_panel_hospital_doctor_name_toolbar_TV);
        runningTV = findViewById(R.id.bookingPendingList);
        completedTV = findViewById(R.id.bookingCompletedList);
        recyclerView = findViewById(R.id.admin_panel_hospital_completed_or_pending_list_recycler);

        nameTV.setText(doctorDetails.getName());

        rootRef = FirebaseDatabase.getInstance().getReference().child("Hospital").child(hospital.getHospitalID()).child("DoctorList").child(doctorDetails.getDoctorID()).child("Booking");
        pendingRef = rootRef.child("Pending");
        completedRef = rootRef.child("Completed");

        runningTV.setBackgroundColor(Color.parseColor("#FF5722"));
        completedTV.setBackgroundColor(Color.parseColor("#ECD0C8"));

        pendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HospitalBooking hb  = d.child("Value").getValue(HospitalBooking.class);
                    bookingList.add(hb);
                }
                Collections.reverse(bookingList);
                adapter = new HospitalBookingListAdapter(HospitalDoctorPendingBookingActivity.this, bookingList);
                LinearLayoutManager llm = new LinearLayoutManager(HospitalDoctorPendingBookingActivity.this);
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
                            HospitalBooking hb  = d.child("Value").getValue(HospitalBooking.class);
                            bookingList.add(hb);
                        }
                        Collections.reverse(bookingList);
                        adapter = new HospitalBookingListAdapter(HospitalDoctorPendingBookingActivity.this, bookingList);
                        LinearLayoutManager llm = new LinearLayoutManager(HospitalDoctorPendingBookingActivity.this);
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
                            HospitalBooking hb  = d.child("Value").getValue(HospitalBooking.class);
                            bookingList.add(hb);
                        }
                        Collections.reverse(bookingList);
                        adapter = new HospitalBookingListAdapter(HospitalDoctorPendingBookingActivity.this, bookingList);
                        LinearLayoutManager llm = new LinearLayoutManager(HospitalDoctorPendingBookingActivity.this);
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
        Intent intent = new Intent(HospitalDoctorPendingBookingActivity.this, HospitalDoctorListActivity.class);
        intent.putExtra("HospitalDetails", hospital);
        startActivity(intent);
        finish();
    }
}
