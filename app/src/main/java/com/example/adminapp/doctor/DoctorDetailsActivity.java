package com.example.adminapp.doctor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.ChamberAdapter;
import com.example.adminapp.adapter.DoctorAppointmentListAdapter;
import com.example.adminapp.chamber.ApprovedDisapprovedActivity;
import com.example.adminapp.chamber.ChamberDetails;
import com.example.adminapp.pojo.DoctorAppointments;
import com.example.adminapp.pojo.DoctorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DoctorDetailsActivity extends AppCompatActivity implements ChamberAdapter.GetChamberDetailsInfo {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private DatabaseReference rootRef;
    private DatabaseReference approvedRef;
    private DatabaseReference disapprovedRef;
    private DatabaseReference runningRef;
    private DatabaseReference completedRef;
    private DatabaseReference completedOnDateRef;

    private LoadingDialog dialog;

    private DoctorDetails details;

    private ImageView proImage;

    private TextView nameTV, degreeTV, specialityTV, appointmentTV, chamberTV, runningTV, completeTV, approvedTV, disapprovedTV, allTV, dateTV;

    private RecyclerView doctorRecycler;

    private LinearLayout appointLO, chamLO, dateLO;

    private List<ChamberDetails> chamberDetailsList = new ArrayList<ChamberDetails>();
    private List<DoctorAppointments> appointmentsList = new ArrayList<DoctorAppointments>();

    private ChamberAdapter chamberAdapter;
    private DoctorAppointmentListAdapter appointmentAdapter;

    private String selectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        details = (DoctorDetails) getIntent().getSerializableExtra("DoctorDetails");

        dialog = new LoadingDialog(DoctorDetailsActivity.this, "Loading...");
        dialog.show();

        rootRef = FirebaseDatabase.getInstance().getReference();
        approvedRef = rootRef.child("Doctor").child(details.getDoctorID()).child("ChamberList").child("Approved");
        disapprovedRef = rootRef.child("Doctor").child(details.getDoctorID()).child("ChamberList").child("Disapproved");

        runningRef = rootRef.child("Doctor").child(details.getDoctorID()).child("AppointmentList").child("Pending");
        completedRef = rootRef.child("Doctor").child(details.getDoctorID()).child("AppointmentList").child("Completed");

        appointLO = findViewById(R.id.visibleAppointment);
        chamLO = findViewById(R.id.visibleChamber);
        dateLO = findViewById(R.id.visibleDate);

        proImage = findViewById(R.id.doctorProfileImage);
        nameTV = findViewById(R.id.doctorName);
        degreeTV = findViewById(R.id.doctorDegree);
        specialityTV = findViewById(R.id.doctorSpeciality);

        appointmentTV = findViewById(R.id.appointmentList);
        chamberTV = findViewById(R.id.chamberList);
        runningTV = findViewById(R.id.runningAppointmentList);
        completeTV = findViewById(R.id.completeAppointmentList);
        approvedTV = findViewById(R.id.approvedChamberList);
        disapprovedTV = findViewById(R.id.disapprovedChamberList);
        allTV = findViewById(R.id.allCompletedList);
        dateTV = findViewById(R.id.pickDate);

        doctorRecycler = findViewById(R.id.doctor_details_base_recycler);

        Picasso.get().load(Uri.parse(details.getImageURI())).into(proImage);
        nameTV.setText("Dr. "+details.getFirstName()+" "+details.getLastName());
        degreeTV.setText(details.getDegree());
        specialityTV.setText(details.getDepartment());

        appointmentTV.setBackgroundColor(Color.parseColor("#FF5722"));
        chamberTV.setBackgroundColor(Color.parseColor("#ECD0C8"));

        appointLO.setVisibility(View.VISIBLE);
        chamLO.setVisibility(View.GONE);
        dateLO.setVisibility(View.GONE);

        runningTV.setBackgroundColor(Color.parseColor("#FF5722"));
        completeTV.setBackgroundColor(Color.parseColor("#ECD0C8"));

        runningRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    DoctorAppointments cd = d.child("Value").getValue(DoctorAppointments.class);
                    appointmentsList.add(cd);
                }
                Collections.reverse(appointmentsList);
                appointmentAdapter = new DoctorAppointmentListAdapter(DoctorDetailsActivity.this, appointmentsList);
                LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                doctorRecycler.setLayoutManager(llm);
                doctorRecycler.setAdapter(appointmentAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        appointmentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                appointmentTV.setBackgroundColor(Color.parseColor("#FF5722"));
                chamberTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                appointLO.setVisibility(View.VISIBLE);
                chamLO.setVisibility(View.GONE);
                dateLO.setVisibility(View.GONE);
                runningTV.setBackgroundColor(Color.parseColor("#FF5722"));
                completeTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                runningRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            DoctorAppointments cd = d.child("Value").getValue(DoctorAppointments.class);
                            appointmentsList.add(cd);
                        }
                        Collections.reverse(appointmentsList);
                        appointmentAdapter = new DoctorAppointmentListAdapter(DoctorDetailsActivity.this, appointmentsList);
                        LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        doctorRecycler.setLayoutManager(llm);
                        doctorRecycler.setAdapter(appointmentAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        chamberTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                appointmentTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                chamberTV.setBackgroundColor(Color.parseColor("#FF5722"));
                appointLO.setVisibility(View.GONE);
                chamLO.setVisibility(View.VISIBLE);
                dateLO.setVisibility(View.GONE);
                approvedTV.setBackgroundColor(Color.parseColor("#FF5722"));
                disapprovedTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                approvedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chamberDetailsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            ChamberDetails cd = d.child("Details").getValue(ChamberDetails.class);
                            chamberDetailsList.add(cd);
                        }
                        Collections.reverse(chamberDetailsList);
                        chamberAdapter = new ChamberAdapter(DoctorDetailsActivity.this, chamberDetailsList);
                        LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        doctorRecycler.setLayoutManager(llm);
                        doctorRecycler.setAdapter(chamberAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        runningTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dateLO.setVisibility(View.GONE);
                runningTV.setBackgroundColor(Color.parseColor("#FF5722"));
                completeTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                runningRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            DoctorAppointments cd = d.child("Value").getValue(DoctorAppointments.class);
                            appointmentsList.add(cd);
                        }
                        Collections.reverse(appointmentsList);
                        appointmentAdapter = new DoctorAppointmentListAdapter(DoctorDetailsActivity.this, appointmentsList);
                        LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        doctorRecycler.setLayoutManager(llm);
                        doctorRecycler.setAdapter(appointmentAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        completeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                dateLO.setVisibility(View.VISIBLE);
                runningTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                completeTV.setBackgroundColor(Color.parseColor("#FF5722"));
                completedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            DoctorAppointments cd = d.child("Value").getValue(DoctorAppointments.class);
                            appointmentsList.add(cd);
                        }
                        Collections.reverse(appointmentsList);
                        appointmentAdapter = new DoctorAppointmentListAdapter(DoctorDetailsActivity.this, appointmentsList);
                        LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        doctorRecycler.setLayoutManager(llm);
                        doctorRecycler.setAdapter(appointmentAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        allTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTV.setText("Select Date");
                dialog.show();
                completedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            DoctorAppointments cd = d.child("Value").getValue(DoctorAppointments.class);
                            appointmentsList.add(cd);
                        }
                        Collections.reverse(appointmentsList);
                        appointmentAdapter = new DoctorAppointmentListAdapter(DoctorDetailsActivity.this, appointmentsList);
                        LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        doctorRecycler.setLayoutManager(llm);
                        doctorRecycler.setAdapter(appointmentAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        final Calendar calendar = Calendar.getInstance();
        /*int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);*/

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                selectDate = simpleDateFormat.format(calendar.getTime());
                dateTV.setText(selectDate);
                getCompletedAppointmentList(selectDate);
            }

        };


        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DoctorDetailsActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        approvedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                approvedTV.setBackgroundColor(Color.parseColor("#FF5722"));
                disapprovedTV.setBackgroundColor(Color.parseColor("#ECD0C8"));

                approvedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chamberDetailsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            ChamberDetails cd = d.child("Details").getValue(ChamberDetails.class);
                            chamberDetailsList.add(cd);
                        }
                        Collections.reverse(chamberDetailsList);
                        chamberAdapter = new ChamberAdapter(DoctorDetailsActivity.this, chamberDetailsList);
                        LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        doctorRecycler.setLayoutManager(llm);
                        doctorRecycler.setAdapter(chamberAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        disapprovedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                approvedTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                disapprovedTV.setBackgroundColor(Color.parseColor("#FF5722"));

                disapprovedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chamberDetailsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            ChamberDetails cd = d.child("Details").getValue(ChamberDetails.class);
                            chamberDetailsList.add(cd);
                        }
                        Collections.reverse(chamberDetailsList);
                        chamberAdapter = new ChamberAdapter(DoctorDetailsActivity.this, chamberDetailsList);
                        LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        doctorRecycler.setLayoutManager(llm);
                        doctorRecycler.setAdapter(chamberAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void getCompletedAppointmentList(String selectDate) {
        dialog.show();
        completedOnDateRef = rootRef.child("Admin").child("Doctor Completed Appointment").child(details.getDoctorID()).child(selectDate);
        completedOnDateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    DoctorAppointments cd = d.child("Value").getValue(DoctorAppointments.class);
                    appointmentsList.add(cd);
                }
                Collections.reverse(appointmentsList);
                appointmentAdapter = new DoctorAppointmentListAdapter(DoctorDetailsActivity.this, appointmentsList);
                LinearLayoutManager llm = new LinearLayoutManager(DoctorDetailsActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                doctorRecycler.setLayoutManager(llm);
                doctorRecycler.setAdapter(appointmentAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DoctorDetailsActivity.this, DoctorInfoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void chamberDetailsInfo(ChamberDetails chamberDetails) {
        Intent intent = new Intent(DoctorDetailsActivity.this, ApprovedDisapprovedActivity.class);
        intent.putExtra("Chamber", chamberDetails);
        intent.putExtra("DoctorDetails",details);
        startActivity(intent);
        finish();
    }
}
