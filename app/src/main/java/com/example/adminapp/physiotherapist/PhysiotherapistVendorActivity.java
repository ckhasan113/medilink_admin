package com.example.adminapp.physiotherapist;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapter.PhysiotherapistListAdapter;
import com.example.adminapp.pojo.PhysiotherapistVendorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PhysiotherapistVendorActivity extends AppCompatActivity implements PhysiotherapistListAdapter.PhysiotherapistListAdapterListener {

    private DatabaseReference physioRef;

    private RecyclerView recyclerView;

    private PhysiotherapistListAdapter adapter;

    private List<PhysiotherapistVendorDetails> list = new ArrayList<PhysiotherapistVendorDetails>();

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physiotherapist_vendor);

        dialog = new LoadingDialog(PhysiotherapistVendorActivity.this, "Loading...");
        dialog.show();

        recyclerView = findViewById(R.id.physiotherapist_vendor_list_recycler);

        physioRef = FirebaseDatabase.getInstance().getReference().child("Physiotherapist");

        physioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    PhysiotherapistVendorDetails pvd = d.child("Details").getValue(PhysiotherapistVendorDetails.class);
                    list.add(pvd);
                }
                adapter = new PhysiotherapistListAdapter(PhysiotherapistVendorActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(PhysiotherapistVendorActivity.this);
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
        Intent intent = new Intent(PhysiotherapistVendorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getPhysiotherapist(PhysiotherapistVendorDetails vendorDetails) {
        Intent intent = new Intent(PhysiotherapistVendorActivity.this, PhysiotherapistBookingListActivity.class);
        intent.putExtra("PhysiotherapistVendorDetails", vendorDetails);
        startActivity(intent);
        finish();
    }
}
