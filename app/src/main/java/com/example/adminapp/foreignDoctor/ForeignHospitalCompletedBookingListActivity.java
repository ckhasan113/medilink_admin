package com.example.adminapp.foreignDoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.ForeignHospitalBookingListAdapter;
import com.example.adminapp.adapter.HospitalBookingListAdapter;
import com.example.adminapp.hospital.HospitalCompletedBookingListActivity;
import com.example.adminapp.hospital.HospitalDoctorListActivity;
import com.example.adminapp.pojo.ForeignBooking;
import com.example.adminapp.pojo.ForeignHospitalDetails;
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

public class ForeignHospitalCompletedBookingListActivity extends AppCompatActivity {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private ForeignHospitalDetails hospital;
    private String country;

    private TextView nameTV, allTV, dateTV;

    private String date;

    private RecyclerView recyclerView;

    private LoadingDialog dialog;

    private DatabaseReference comRef;
    private DatabaseReference dateRef;

    private ForeignHospitalBookingListAdapter adapter;

    private List<ForeignBooking> bookingList = new ArrayList<ForeignBooking>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_hospital_completed_booking_list);

        hospital = (ForeignHospitalDetails) getIntent().getSerializableExtra("ForeignHospitalDetails");
        country = getIntent().getStringExtra("ForeignHospitalCountry");

        dialog = new LoadingDialog(ForeignHospitalCompletedBookingListActivity.this, "Loading...");
        dialog.show();

        comRef = FirebaseDatabase.getInstance().getReference().child("Foreign Doctor").child("Country").child(country).child("Hospital").child(hospital.getHospitalID()).child("complete");
        recyclerView = findViewById(R.id.admin_panel_foreign_hospital_completed_list_recycler);
        nameTV = findViewById(R.id.admin_panel_foreign_hospital_doctor_name_toolbar_TV);
        allTV = findViewById(R.id.allCompletedForeignHospitalBookingList);
        dateTV = findViewById(R.id.pickDateForeignHospitalBooking);

        nameTV.setText(hospital.getName());

        comRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ForeignBooking hb  = d.child("Value").getValue(ForeignBooking.class);
                    bookingList.add(hb);
                }

                Collections.reverse(bookingList);
                adapter = new ForeignHospitalBookingListAdapter(ForeignHospitalCompletedBookingListActivity.this, bookingList);
                LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalCompletedBookingListActivity.this);
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
                            ForeignBooking hb  = d.child("Value").getValue(ForeignBooking.class);
                            bookingList.add(hb);
                        }

                        Collections.reverse(bookingList);
                        adapter = new ForeignHospitalBookingListAdapter(ForeignHospitalCompletedBookingListActivity.this, bookingList);
                        LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalCompletedBookingListActivity.this);
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
                getCompletedAppointmentList();
            }

        };

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ForeignHospitalCompletedBookingListActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getCompletedAppointmentList() {
        dialog.show();
        dateRef = FirebaseDatabase.getInstance().getReference().child("Admin").child("Foreign Completed Booking List").child(hospital.getHospitalID()).child(date);
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookingList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ForeignBooking cd = d.child("Value").getValue(ForeignBooking.class);
                    bookingList.add(cd);
                }
                Collections.reverse(bookingList);
                adapter = new ForeignHospitalBookingListAdapter(ForeignHospitalCompletedBookingListActivity.this, bookingList);
                LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalCompletedBookingListActivity.this);
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
        Intent intent = new Intent(ForeignHospitalCompletedBookingListActivity.this, ForeignHospitalDoctorListActivity.class);
        intent.putExtra("ForeignHospitalDetails", hospital);
        intent.putExtra("ForeignHospitalCountry", country);
        startActivity(intent);
        finish();
    }
}
