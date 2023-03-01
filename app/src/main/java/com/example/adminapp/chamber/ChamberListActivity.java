package com.example.adminapp.chamber;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.ChamberAdapter;
import com.example.adminapp.doctor.DoctorInfoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChamberListActivity extends AppCompatActivity implements ChamberAdapter.GetChamberDetailsInfo {

    private DatabaseReference rootRef;
    private DatabaseReference newChamberRef;
    private DatabaseReference rejectedChamberRef;

    /*private TextView newChamberTV, rejectedTV;*/

    private RelativeLayout noChamberLO;
    private LinearLayout newRequestLayout;

    private RecyclerView chamberRecycler;

    private LoadingDialog dialog;

    private List<ChamberDetails> chamberDetailsList = new ArrayList<ChamberDetails>();

    private ChamberAdapter newChamberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamber_list);

        dialog = new LoadingDialog(ChamberListActivity.this,"Loading...");
        dialog.show();

        rootRef = FirebaseDatabase.getInstance().getReference();
        newChamberRef = rootRef.child("Admin").child("New Chamber");
        rejectedChamberRef = rootRef.child("Admin").child("Rejected Chamber");

        noChamberLO = findViewById(R.id.emptyNewChamberRequest);
        newRequestLayout = findViewById(R.id.newChamberRequestLayout);

        /*newChamberTV = findViewById(R.id.new_chamber);
        rejectedTV = findViewById(R.id.rejected_chamber);*/

        chamberRecycler = findViewById(R.id.chamber_base_recycler);

        /*newChamberTV.setBackgroundColor(Color.parseColor("#FF5722"));
        rejectedTV.setBackgroundColor(Color.parseColor("#ECD0C8"));*/

        newChamberRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chamberDetailsList.clear();
                for (DataSnapshot cd: dataSnapshot.getChildren()){
                    ChamberDetails chamber = cd.child("Details").getValue(ChamberDetails.class);
                    chamberDetailsList.add(chamber);
                }

                if (chamberDetailsList.size() == 0){
                    noChamberLO.setVisibility(View.VISIBLE);
                    newRequestLayout.setVisibility(View.GONE);
                    dialog.dismiss();
                    return;
                }else {
                    noChamberLO.setVisibility(View.GONE);
                    newRequestLayout.setVisibility(View.VISIBLE);
                }
                Collections.reverse(chamberDetailsList);
                newChamberAdapter = new ChamberAdapter(ChamberListActivity.this, chamberDetailsList);
                LinearLayoutManager llm = new LinearLayoutManager(ChamberListActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                chamberRecycler.setLayoutManager(llm);
                chamberRecycler.setAdapter(newChamberAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*newChamberTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newChamberTV.setBackgroundColor(Color.parseColor("#FF5722"));
                rejectedTV.setBackgroundColor(Color.parseColor("#ECD0C8"));

                dialog.show();
                newChamberRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chamberDetailsList.clear();
                        for (DataSnapshot cd: dataSnapshot.getChildren()){
                            ChamberDetails chamber = cd.child("Details").getValue(ChamberDetails.class);
                            chamberDetailsList.add(chamber);
                        }

                        newChamberAdapter = new ChamberAdapter(ChamberListActivity.this, chamberDetailsList);
                        LinearLayoutManager llm = new LinearLayoutManager(ChamberListActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        chamberRecycler.setLayoutManager(llm);
                        chamberRecycler.setAdapter(newChamberAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });*/

        /*rejectedTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newChamberTV.setBackgroundColor(Color.parseColor("#ECD0C8"));
                rejectedTV.setBackgroundColor(Color.parseColor("#FF5722"));

                dialog.show();
                rejectedChamberRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chamberDetailsList.clear();
                        for (DataSnapshot cd: dataSnapshot.getChildren()){
                            ChamberDetails chamber = cd.child("Details").getValue(ChamberDetails.class);
                            chamberDetailsList.add(chamber);
                        }

                        newChamberAdapter = new ChamberAdapter(ChamberListActivity.this, chamberDetailsList);
                        LinearLayoutManager llm = new LinearLayoutManager(ChamberListActivity.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        chamberRecycler.setLayoutManager(llm);
                        chamberRecycler.setAdapter(newChamberAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChamberListActivity.this, DoctorInfoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void chamberDetailsInfo(ChamberDetails chamberDetails) {
        Intent intent = new Intent(ChamberListActivity.this, ApprovedDisapprovedActivity.class);
        intent.putExtra("Chamber", chamberDetails);
        startActivity(intent);
        finish();
    }
}
