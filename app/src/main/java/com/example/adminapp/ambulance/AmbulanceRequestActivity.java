package com.example.adminapp.ambulance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapter.GetAmbulanceRequestListAdapter;
import com.example.adminapp.pojo.AmbulanceTaken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmbulanceRequestActivity extends AppCompatActivity implements GetAmbulanceRequestListAdapter.GetAmbulanceRequestDetailsAdapterListener {

    private Spinner typeIsp;

    private LoadingDialog dialog;

    private DatabaseReference rootRef;
    private DatabaseReference allRef;
    private DatabaseReference typeRef;

    private RadioButton newRequestRB, completedRB;

    private String divisionRB = "All Request", divisionISP = "Request", type;

    private boolean checked = false;

    private List<AmbulanceTaken> takenList = new ArrayList<AmbulanceTaken>();
    private RecyclerView ambulanceRecycler;
    private GetAmbulanceRequestListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_request);

        dialog = new LoadingDialog(AmbulanceRequestActivity.this, "Loading...");
        dialog.show();

        rootRef = FirebaseDatabase.getInstance().getReference();

        allRef = rootRef.child("Ambulance");
        typeRef = allRef.child("Ambulance Type");

        typeIsp = findViewById(R.id.isp_ambulance_type);
        newRequestRB = findViewById(R.id.newRequestRB);
        completedRB = findViewById(R.id.completedRB);
        ambulanceRecycler = findViewById(R.id.ambulance_admin_panel_recycler);

        ArrayAdapter spinnerTypeAdapter = ArrayAdapter.createFromResource(AmbulanceRequestActivity.this, R.array.ambulance_type, R.layout.spinner_item_select_model);
        spinnerTypeAdapter.setDropDownViewResource(R.layout.spinner_item_drop_down_model);
        typeIsp.setAdapter(spinnerTypeAdapter);

        rootRef.child("Ambulance").child("All Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                takenList.clear();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    AmbulanceTaken at = d.child("Details").getValue(AmbulanceTaken.class);
                    takenList.add(at);
                }
                Collections.reverse(takenList);
                adapter = new GetAmbulanceRequestListAdapter(AmbulanceRequestActivity.this, takenList);
                LinearLayoutManager llm = new LinearLayoutManager(AmbulanceRequestActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                ambulanceRecycler.setLayoutManager(llm);
                ambulanceRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newRequestRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                divisionRB = "All Request";
                divisionISP = "Request";

                getAmbulanceRequestList();
            }
        });

        completedRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                divisionRB = "All Completed";
                divisionISP = "Completed";

                getAmbulanceRequestList();
            }
        });

        typeIsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    checked = true;
                }else {
                    checked = false;
                    type = typeIsp.getSelectedItem().toString();
                }
                getAmbulanceRequestList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAmbulanceRequestList() {
        dialog.show();
        if (checked){
            allRef.child(divisionRB).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    takenList.clear();
                    for (DataSnapshot d:dataSnapshot.getChildren()){
                        AmbulanceTaken at = d.child("Details").getValue(AmbulanceTaken.class);
                        takenList.add(at);
                    }
                    Collections.reverse(takenList);
                    adapter = new GetAmbulanceRequestListAdapter(AmbulanceRequestActivity.this, takenList);
                    LinearLayoutManager llm = new LinearLayoutManager(AmbulanceRequestActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    ambulanceRecycler.setLayoutManager(llm);
                    ambulanceRecycler.setAdapter(adapter);
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            typeRef.child(type).child(divisionISP).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    takenList.clear();
                    for (DataSnapshot d:dataSnapshot.getChildren()){
                        AmbulanceTaken at = d.child("Details").getValue(AmbulanceTaken.class);
                        takenList.add(at);
                    }
                    Collections.reverse(takenList);
                    adapter = new GetAmbulanceRequestListAdapter(AmbulanceRequestActivity.this, takenList);
                    LinearLayoutManager llm = new LinearLayoutManager(AmbulanceRequestActivity.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    ambulanceRecycler.setLayoutManager(llm);
                    ambulanceRecycler.setAdapter(adapter);
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AmbulanceRequestActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void getAmbulanceRequestDetails(AmbulanceTaken ambulance) {
        Intent intent = new Intent(AmbulanceRequestActivity.this, AmbulanceRequestDetailsActivity.class);
        intent.putExtra("RequestDetails", ambulance);
        startActivity(intent);
        finish();
    }
}
