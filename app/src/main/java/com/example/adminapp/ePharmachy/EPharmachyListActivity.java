package com.example.adminapp.ePharmachy;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapter.EPharmacyListAdapter;
import com.example.adminapp.pojo.EPharmaDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EPharmachyListActivity extends AppCompatActivity implements EPharmacyListAdapter.EPharmacyListAdapterListener {

    private DatabaseReference pharmaRef;

    private RecyclerView pharmaRecycler;

    private List<EPharmaDetails> list = new ArrayList<EPharmaDetails>();
    private EPharmacyListAdapter adapter;

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epharmachy_list);

        dialog = new LoadingDialog(EPharmachyListActivity.this, "Loading...");
        dialog.show();

        pharmaRef = FirebaseDatabase.getInstance().getReference().child("Pharmacy");

        pharmaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    EPharmaDetails ed = d.child("Details").getValue(EPharmaDetails.class);
                    list.add(ed);
                }
                adapter = new EPharmacyListAdapter(EPharmachyListActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(EPharmachyListActivity.this);
                llm.setOrientation(RecyclerView.VERTICAL);
                pharmaRecycler.setLayoutManager(llm);
                pharmaRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pharmaRecycler = findViewById(R.id.admin_panel_pharmacy_list_recycler);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EPharmachyListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getPharmacyDetails(EPharmaDetails vendorDetails) {
        Intent intent = new Intent(EPharmachyListActivity.this, PharmacyOrderListActivity.class);
        intent.putExtra("VendorDetails", vendorDetails);
        startActivity(intent);
        finish();
    }
}
