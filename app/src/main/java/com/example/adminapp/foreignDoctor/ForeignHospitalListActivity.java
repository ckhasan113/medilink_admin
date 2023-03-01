package com.example.adminapp.foreignDoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapter.ForeignHospitalDoctorListAdapter;
import com.example.adminapp.adapter.ForeignHospitalListAdapter;
import com.example.adminapp.ambulance.AmbulanceRequestActivity;
import com.example.adminapp.hospital.HospitalDoctorListActivity;
import com.example.adminapp.hospital.HospitalListActivity;
import com.example.adminapp.pojo.ForeignHospitalDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForeignHospitalListActivity extends AppCompatActivity implements ForeignHospitalListAdapter.ForeignHospitalListAdapterListener{

    private RecyclerView hospitalRecycler;

    private DatabaseReference foreignRef;

    private Spinner countryIsp;

    private String country = "India";

    private LoadingDialog dialog;

    private ForeignHospitalListAdapter adapter;

    private List<ForeignHospitalDetails> detailsList = new ArrayList<ForeignHospitalDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_hospital_list);

        dialog = new LoadingDialog(ForeignHospitalListActivity.this, "Loading...");
        dialog.show();

        foreignRef = FirebaseDatabase.getInstance().getReference().child("Foreign Doctor").child("Country");

        hospitalRecycler = findViewById(R.id.admin_panel_foreign_hospital_list_recycler);
        countryIsp = findViewById(R.id.isp_country);

        ArrayAdapter spinnerCountryAdapter = ArrayAdapter.createFromResource(ForeignHospitalListActivity.this, R.array.country, R.layout.spinner_item_select_model);
        spinnerCountryAdapter.setDropDownViewResource(R.layout.spinner_item_drop_down_model);
        countryIsp.setAdapter(spinnerCountryAdapter);

        foreignRef.child(country).child("Hospital").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                detailsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ForeignHospitalDetails fhd = d.child("Details").getValue(ForeignHospitalDetails.class);
                    detailsList.add(fhd);
                }
                Collections.reverse(detailsList);

                adapter = new ForeignHospitalListAdapter(ForeignHospitalListActivity.this, detailsList, "India");
                LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalListActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                hospitalRecycler.setLayoutManager(llm);
                hospitalRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        countryIsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                country = countryIsp.getSelectedItem().toString();
                dialog.show();

                foreignRef.child(country).child("Hospital").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        detailsList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            ForeignHospitalDetails fhd = d.child("Details").getValue(ForeignHospitalDetails.class);
                            detailsList.add(fhd);
                        }
                        Collections.reverse(detailsList);

                        adapter = new ForeignHospitalListAdapter(ForeignHospitalListActivity.this, detailsList, "India");
                        LinearLayoutManager llm = new LinearLayoutManager(ForeignHospitalListActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        hospitalRecycler.setLayoutManager(llm);
                        hospitalRecycler.setAdapter(adapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForeignHospitalListActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onClickBooking(ForeignHospitalDetails hospital, String country) {
        Intent intent = new Intent(ForeignHospitalListActivity.this, ForeignHospitalDoctorListActivity.class);
        intent.putExtra("ForeignHospitalDetails", hospital);
        intent.putExtra("ForeignHospitalCountry", country);
        startActivity(intent);
        finish();
    }
}
