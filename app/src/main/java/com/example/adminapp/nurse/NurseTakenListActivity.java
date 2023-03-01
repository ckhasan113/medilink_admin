package com.example.adminapp.nurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.MyPhysiotherapistListAdapter;
import com.example.adminapp.adapter.NurseTakenListAdapter;
import com.example.adminapp.physiotherapist.PhysiotherapistBookingListActivity;
import com.example.adminapp.pojo.NurseAddtoCart;
import com.example.adminapp.pojo.NurseVendorDetails;
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

public class NurseTakenListActivity extends AppCompatActivity implements NurseTakenListAdapter.NurseTakenListAdapterListener {

    private TextView vendorNameTV;

    private RecyclerView recyclerView;

    private LoadingDialog dialog;

    private NurseVendorDetails vendorDetails;

    private NurseTakenListAdapter adapter;

    private List<NurseAddtoCart> list = new ArrayList<NurseAddtoCart>();

    private DatabaseReference nurseTakenRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_taken_list);

        vendorDetails = (NurseVendorDetails) getIntent().getSerializableExtra("NurseVendorDetails");

        dialog = new LoadingDialog(NurseTakenListActivity.this, "loading...");
        dialog.show();

        nurseTakenRef = FirebaseDatabase.getInstance().getReference().child("Nurse Vendor").child(vendorDetails.getVendorID()).child("Nurse Taken");

        vendorNameTV = findViewById(R.id.nurseVendorNameTV);
        recyclerView = findViewById(R.id.nurse_vendor_taken_list_recycler);

        vendorNameTV.setText(vendorDetails.getName());

        nurseTakenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    NurseAddtoCart pb = d.child("Details").getValue(NurseAddtoCart.class);
                    list.add(pb);
                }
                Collections.reverse(list);
                adapter = new NurseTakenListAdapter(NurseTakenListActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(NurseTakenListActivity.this);
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
        startActivity(new Intent(NurseTakenListActivity.this, NurseVendorListActivity.class));
        finish();
    }

    @Override
    public void getNurseTakenRequestDetails(NurseAddtoCart taken) {
        Intent intent = new Intent(NurseTakenListActivity.this, NurseTakenDetailsActivity.class);
        intent.putExtra("NurseVendorDetails", vendorDetails);
        intent.putExtra("NurseTakenDetails", taken);
        startActivity(intent);
        finish();
    }
}
