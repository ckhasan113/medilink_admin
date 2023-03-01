package com.example.adminapp.physiotherapist;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.MyPhysiotherapistListAdapter;
import com.example.adminapp.pojo.PhysiotherapistBooking;
import com.example.adminapp.pojo.PhysiotherapistVendorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhysiotherapistBookingListActivity extends AppCompatActivity {

    private TextView vendorNameTV;

    private RecyclerView recyclerView;

    private LoadingDialog dialog;

    private PhysiotherapistVendorDetails vendorDetails;

    private MyPhysiotherapistListAdapter adapter;

    private List<PhysiotherapistBooking> list = new ArrayList<PhysiotherapistBooking>();

    private DatabaseReference physioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physiotherapist_booking_list);

        vendorDetails = (PhysiotherapistVendorDetails) getIntent().getSerializableExtra("PhysiotherapistVendorDetails");

        dialog = new LoadingDialog(PhysiotherapistBookingListActivity.this, "loading...");
        dialog.show();

        physioRef = FirebaseDatabase.getInstance().getReference().child("Physiotherapist").child(vendorDetails.getVendorID()).child("ClientRequest");

        vendorNameTV = findViewById(R.id.physioVendorNameTV);
        recyclerView = findViewById(R.id.physiotherapist_vendor_booking_list_recycler);

        vendorNameTV.setText(vendorDetails.getName());

        physioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    PhysiotherapistBooking pb = d.child("Details").getValue(PhysiotherapistBooking.class);
                    list.add(pb);
                }
                Collections.reverse(list);
                adapter = new MyPhysiotherapistListAdapter(PhysiotherapistBookingListActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(PhysiotherapistBookingListActivity.this);
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
        Intent intent = new Intent(PhysiotherapistBookingListActivity.this, PhysiotherapistVendorActivity.class);
        startActivity(intent);
        finish();
    }
}
