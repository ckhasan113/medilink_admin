package com.example.adminapp.nurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapter.NurseVendorListAdapter;
import com.example.adminapp.adapter.PhysiotherapistListAdapter;
import com.example.adminapp.physiotherapist.PhysiotherapistVendorActivity;
import com.example.adminapp.pojo.NurseAddtoCart;
import com.example.adminapp.pojo.NurseVendorDetails;
import com.example.adminapp.pojo.PhysiotherapistVendorDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NurseVendorListActivity extends AppCompatActivity implements NurseVendorListAdapter.NurseVendorListAdapterListener{

    private DatabaseReference nurseRef;

    private RecyclerView recyclerView;

    private NurseVendorListAdapter adapter;

    private List<NurseVendorDetails> list = new ArrayList<NurseVendorDetails>();

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_vendor_list);

        dialog = new LoadingDialog(NurseVendorListActivity.this, "Loading...");
        dialog.show();

        recyclerView = findViewById(R.id.nurse_vendor_list_recycler);

        nurseRef = FirebaseDatabase.getInstance().getReference().child("Nurse Vendor");

        nurseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    NurseVendorDetails nvd = d.child("Details").getValue(NurseVendorDetails.class);
                    list.add(nvd);
                }
                adapter = new NurseVendorListAdapter(NurseVendorListActivity.this, list);
                LinearLayoutManager llm = new LinearLayoutManager(NurseVendorListActivity.this);
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
        startActivity(new Intent(NurseVendorListActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void getNurseTakenList(NurseVendorDetails vendorDetails) {
        Intent intent = new Intent(NurseVendorListActivity.this, NurseTakenListActivity.class);
        intent.putExtra("NurseVendorDetails", vendorDetails);
        startActivity(intent);
        finish();

    }
}
