package com.example.adminapp.chamber;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminapp.R;
import com.example.adminapp.doctor.DoctorDetailsActivity;
import com.example.adminapp.pojo.DoctorDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ApprovedDisapprovedActivity extends AppCompatActivity {

    private SimpleDateFormat selectedDateOfMonth = new SimpleDateFormat("dd", Locale.getDefault());

    private DatabaseReference rootRef;
    private DatabaseReference adminRef;
    private DatabaseReference doctorRef;
    private DatabaseReference doctorChamberRef;
    private DatabaseReference doctorAvailableRef;

    private TextView hospitalTV, daysTV, stTimeTV, endTimeTV, patient_countTV, cityTV, areaTV, blockTV, roadTV, houseTV, floorTV, roomTV, zipTV, countryTV, feeTV;
    private Button approvedBtn, disapprovedTV;

    private ChamberDetails chamber;

    private String ch_id, chamberName, stTime, endTime, patient_count, city, area, block, road, house, floor, room, zip, country, fee, status, doctorID, dayListString;

    private List<String> days = new ArrayList<String>();

    private DoctorDetails doctorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_disapproved);

        chamber = (ChamberDetails) getIntent().getSerializableExtra("Chamber");
        doctorDetails = (DoctorDetails) getIntent().getSerializableExtra("DoctorDetails");

        ch_id = chamber.getAdd_ch_id();
        doctorID = chamber.getDoctorID();

        rootRef = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRef.child("Admin");
        doctorRef = rootRef.child("Doctor").child(doctorID);
        doctorChamberRef = doctorRef.child("ChamberList");

        daysTV = findViewById(R.id.dates);

        approvedBtn = findViewById(R.id.approvedBtn);
        disapprovedTV = findViewById(R.id.disapproved);

        hospitalTV = findViewById(R.id.ch_hospital);
        hospitalTV.setText(chamber.getChamberName());
        chamberName = chamber.getChamberName();

        stTimeTV = findViewById(R.id.stTime);
        stTimeTV.setText(chamber.getStart());
        stTime = chamber.getStart();

        endTimeTV = findViewById(R.id.endTime);
        endTimeTV.setText(chamber.getEnd());
        endTime = chamber.getEnd();

        patient_countTV = findViewById(R.id.patient_count);
        patient_countTV.setText(chamber.getPatient_count());
        patient_count = chamber.getPatient_count();

        cityTV = findViewById(R.id.ch_city);
        cityTV.setText(chamber.getCity());
        city = chamber.getCity();

        areaTV = findViewById(R.id.ch_area);
        areaTV.setText(chamber.getArea());
        area = chamber.getArea();

        blockTV = findViewById(R.id.ch_block);
        blockTV.setText(chamber.getBlock());
        block = chamber.getBlock();

        roadTV = findViewById(R.id.ch_road);
        roadTV.setText(chamber.getRoad());
        road = chamber.getRoad();

        houseTV = findViewById(R.id.ch_house);
        houseTV.setText(chamber.getHouse());
        house = chamber.getHouse();

        floorTV = findViewById(R.id.ch_floor);
        floorTV.setText(chamber.getFloor());
        floor = chamber.getFloor();

        roomTV = findViewById(R.id.ch_room);
        roomTV.setText(chamber.getRoom());
        room = chamber.getRoom();

        zipTV = findViewById(R.id.ch_zip);
        zipTV.setText(chamber.getZip());
        zip = chamber.getZip();

        countryTV = findViewById(R.id.ch_country_id);
        countryTV.setText(chamber.getCountry_id());
        country = chamber.getCountry_id();

        feeTV = findViewById(R.id.ch_fee);
        feeTV.setText(chamber.getFees());
        fee = chamber.getFees();

        days = chamber.getDateList();
        for (String ds: days){
            daysTV.append(ds+", ");
        }

        approvedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "accepted";
                ChamberDetails chamber = new ChamberDetails(ch_id, chamberName, days, stTime, endTime, patient_count, house, floor, room, block, road, area, city, zip, country, fee, status, doctorID);
                doctorChamberRef.child("All").child(ch_id).child("Details").setValue(chamber);
                doctorChamberRef.child("Approved").child(ch_id).child("Details").setValue(chamber);
                adminRef.child("New Chamber").child(ch_id).removeValue();
                adminRef.child("Rejected Chamber").child(ch_id).removeValue();

                for (String ds: days){
                    doctorAvailableRef = doctorRef.child("DoctorDates").child(ds);
                    Map<String, Object> dateMap = new HashMap<>();
                    dateMap.put("chamberID", ch_id);
                    dateMap.put("date", ds);

                    doctorAvailableRef.child("Chambers").child(ch_id).child("Details").setValue(chamber);
                    doctorAvailableRef.child("Dates").setValue(dateMap);
                }

                Toast.makeText(ApprovedDisapprovedActivity.this, "Approved", Toast.LENGTH_SHORT).show();
                if (doctorDetails == null){
                    Intent intent = new Intent(ApprovedDisapprovedActivity.this, ChamberListActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(ApprovedDisapprovedActivity.this, DoctorDetailsActivity.class);
                    intent.putExtra("DoctorDetails",doctorDetails);
                    startActivity(intent);
                    finish();
                }
            }
        });

        disapprovedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "rejected";
                ChamberDetails chamber = new ChamberDetails(ch_id, chamberName, days, stTime, endTime, patient_count, house, floor, room, block, road, area, city, zip, country, fee, status, doctorID);
                doctorChamberRef.child("All").child(ch_id).child("Details").setValue(chamber);
                adminRef.child("New Chamber").child(ch_id).removeValue();
                doctorChamberRef.child("Disapproved").child(ch_id).child("Details").setValue(chamber);
                Toast.makeText(ApprovedDisapprovedActivity.this, "Disapproved", Toast.LENGTH_SHORT).show();
                if (doctorDetails == null){
                    Intent intent = new Intent(ApprovedDisapprovedActivity.this, ChamberListActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(ApprovedDisapprovedActivity.this, DoctorDetailsActivity.class);
                    intent.putExtra("DoctorDetails",doctorDetails);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doctorDetails == null){
            Intent intent = new Intent(ApprovedDisapprovedActivity.this, ChamberListActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(ApprovedDisapprovedActivity.this, DoctorDetailsActivity.class);
            intent.putExtra("DoctorDetails",doctorDetails);
            startActivity(intent);
            finish();
        }
    }
}
