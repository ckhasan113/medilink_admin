package com.example.adminapp.heathtips;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.MainActivity;
import com.example.adminapp.R;
import com.example.adminapp.adapter.HealthTipsAdapter;
import com.example.adminapp.pojo.HealthTips;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HealthTipsListActivity extends AppCompatActivity implements HealthTipsAdapter.HealthTipsAdapterListener {

    private Button newTipBtn;

    private RecyclerView tipRecycler;

    private LoadingDialog dialog;

    private DatabaseReference rootRef;
    private DatabaseReference tipRef;

    private List<HealthTips> tipsList = new ArrayList<HealthTips>();

    private HealthTipsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips_list);

        dialog = new LoadingDialog(HealthTipsListActivity.this, "Loading...");
        dialog.show();

        rootRef = FirebaseDatabase.getInstance().getReference();
        tipRef = rootRef.child("Admin").child("Health Tips");

        newTipBtn = findViewById(R.id.newTipBtn);
        tipRecycler = findViewById(R.id.health_tips_recycler);

        tipRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tipsList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    HealthTips health = d.child("Value").getValue(HealthTips.class);
                    tipsList.add(health);
                }
                Collections.reverse(tipsList);
                adapter = new HealthTipsAdapter(HealthTipsListActivity.this, tipsList);
                LinearLayoutManager llm = new LinearLayoutManager(HealthTipsListActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                tipRecycler.setLayoutManager(llm);
                tipRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        newTipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HealthTipsListActivity.this, AddHealthTipsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HealthTipsListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTipDetails(HealthTips ht) {
        Intent intent = new Intent(HealthTipsListActivity.this, AddHealthTipsActivity.class);
        intent.putExtra("HealthTips", ht);
        startActivity(intent);
        finish();
    }
}
