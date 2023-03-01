package com.example.adminapp.hospital;

import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.HospitalBookingListAdapter;
import com.example.adminapp.pojo.HospitalBooking;
import com.example.adminapp.pojo.HospitalDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HospitalCompletedBookingListActivity extends AppCompatActivity {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private HospitalDetails hospital;

    private TextView nameTV, allTV, dateTV;

    private String date;

    private RecyclerView recyclerView;

    private LoadingDialog dialog;

    private DatabaseReference comRef;
    private DatabaseReference dateRef;

    private HospitalBookingListAdapter adapter;

    private List<HospitalBooking> bookingList = new ArrayList<HospitalBooking>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_completed_booking_list);

        hospital = (HospitalDetails) getIntent().getSerializableExtra("HospitalDetails");

        dialog = new LoadingDialog(HospitalCompletedBookingListActivity.this, "Loading...");
        dialog.show();

        comRef = FirebaseDatabase.getInstance().getReference().child("Hospital").child(hospital.getHospitalID()).child("Completed");
        recyclerView = findViewById(R.id.admin_panel_hospital_completed_list_recycler);
        nameTV = findViewById(R.id.admin_panel_hospital_doctor_name_toolbar_TV);
        allTV = findViewById(R.id.allCompletedHospitalBookingList);
        dateTV = findViewById(R.id.pickDateHospitalBooking);

        nameTV.setText(hospital.getName());

        comRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HospitalBooking hb  = d.child("Value").getValue(HospitalBooking.class);
                    bookingList.add(hb);
                }

                Collections.reverse(bookingList);
                adapter = new HospitalBookingListAdapter(HospitalCompletedBookingListActivity.this, bookingList);
                LinearLayoutManager llm = new LinearLayoutManager(HospitalCompletedBookingListActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        allTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTV.setText("Select date");
                comRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bookingList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            HospitalBooking hb  = d.child("Value").getValue(HospitalBooking.class);
                            bookingList.add(hb);
                        }

                        Collections.reverse(bookingList);
                        adapter = new HospitalBookingListAdapter(HospitalCompletedBookingListActivity.this, bookingList);
                        LinearLayoutManager llm = new LinearLayoutManager(HospitalCompletedBookingListActivity.this);
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

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                date = simpleDateFormat.format(calendar.getTime());
                dateTV.setText(date);
                getCompletedAppointmentList(date);
            }

        };

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(HospitalCompletedBookingListActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    private void getCompletedAppointmentList(String selectDate) {
        dialog.show();
        dateRef = FirebaseDatabase.getInstance().getReference().child("Admin").child("Hospital Completed Booking List").child(hospital.getHospitalID()).child(date);
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HospitalBooking cd = d.child("Value").getValue(HospitalBooking.class);
                    bookingList.add(cd);
                }
                Collections.reverse(bookingList);
                adapter = new HospitalBookingListAdapter(HospitalCompletedBookingListActivity.this, bookingList);
                LinearLayoutManager llm = new LinearLayoutManager(HospitalCompletedBookingListActivity.this);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HospitalCompletedBookingListActivity.this, HospitalDoctorListActivity.class);
        intent.putExtra("HospitalDetails", hospital);
        startActivity(intent);
        finish();
    }
}
