package com.example.adminapp.hospital;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapter.HospitalListAdapter;
import com.example.adminapp.pojo.HospitalDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HospitalListActivity extends AppCompatActivity implements HospitalListAdapter.HospitalListAdapterListener {

    private DatabaseReference hospitalRef;

    private RecyclerView hospitalRecycler;

    private HospitalListAdapter adapter;

    private List<HospitalDetails> hospitalDetailsList = new ArrayList<HospitalDetails>();

    private LoadingDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        dialog = new LoadingDialog(HospitalListActivity.this, "Loading...");
        dialog.show();

        hospitalRef = FirebaseDatabase.getInstance().getReference().child("Hospital");

        hospitalRecycler = findViewById(R.id.admin_panel_hospital_list_recycler);

        hospitalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hospitalDetailsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HospitalDetails hd = d.child("Details").getValue(HospitalDetails.class);
                    hospitalDetailsList.add(hd);
                }

                adapter = new HospitalListAdapter(HospitalListActivity.this, hospitalDetailsList);
                LinearLayoutManager llm = new LinearLayoutManager(HospitalListActivity.this);
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
    public void onBackPressed() {
        Intent intent = new Intent(HospitalListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClickBooking(HospitalDetails hospital) {
        Intent intent = new Intent(HospitalListActivity.this, HospitalDoctorListActivity.class);
        intent.putExtra("HospitalDetails", hospital);
        startActivity(intent);
        finish();
    }
}
