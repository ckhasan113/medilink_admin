package com.example.adminapp.ePharmachy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.adapter.PharmaClientRequestAdapter;
import com.example.adminapp.doctor.DoctorDetailsActivity;
import com.example.adminapp.pojo.ConfirmOrder;
import com.example.adminapp.pojo.EPharmaDetails;
import com.example.adminapp.pojo.OrderChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PharmacyOrderListActivity extends AppCompatActivity implements PharmaClientRequestAdapter.PharmaClientRequestAdapterListener {

    private TextView pharmaNameTV;

    private EPharmaDetails ed;

    private RecyclerView orderRecycler;
    private PharmaClientRequestAdapter adapter;

    private List<ConfirmOrder> chartList = new ArrayList<ConfirmOrder>();

    private DatabaseReference orderRef;

    private LoadingDialog dialog;

    private String date;

    private TextView allTV, pickDateTV;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_order_list);

        ed = (EPharmaDetails) getIntent().getSerializableExtra("VendorDetails");

        dialog = new LoadingDialog(PharmacyOrderListActivity.this, "Loading...");
        dialog.show();

        orderRef = FirebaseDatabase.getInstance().getReference().child("Pharmacy").child(ed.getId()).child("Order");

        pharmaNameTV = findViewById(R.id.admin_panel_ePharmacy_name_tv);
        orderRecycler = findViewById(R.id.admin_panel_pharmacy_order_list_recycler);
        allTV = findViewById(R.id.allPharmacyOrderList);
        pickDateTV = findViewById(R.id.pickDateForPharmacy);

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chartList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ConfirmOrder cd = d.child("Details").getValue(ConfirmOrder.class);
                    chartList.add(cd);
                }
                adapter = new PharmaClientRequestAdapter(PharmacyOrderListActivity.this, chartList);
                LinearLayoutManager llm = new LinearLayoutManager(PharmacyOrderListActivity.this);
                llm.setOrientation(RecyclerView.VERTICAL);
                orderRecycler.setLayoutManager(llm);
                orderRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pharmaNameTV.setText(ed.getFarmName());

        allTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDateTV.setText("Select date");
                dialog.show();
                orderRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chartList.clear();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            ConfirmOrder cd = d.child("Details").getValue(ConfirmOrder.class);
                            chartList.add(cd);
                        }
                        adapter = new PharmaClientRequestAdapter(PharmacyOrderListActivity.this, chartList);
                        LinearLayoutManager llm = new LinearLayoutManager(PharmacyOrderListActivity.this);
                        llm.setOrientation(RecyclerView.VERTICAL);
                        orderRecycler.setLayoutManager(llm);
                        orderRecycler.setAdapter(adapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                date = simpleDateFormat.format(calendar.getTime());
                pickDateTV.setText(date);
                getOrderListOnSelectDate(date);
            }

        };


        pickDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PharmacyOrderListActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getOrderListOnSelectDate(final String date) {
        dialog.show();
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chartList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    ConfirmOrder cd = d.child("Details").getValue(ConfirmOrder.class);
                    if (cd.getDate().equals(date)){
                        chartList.add(cd);
                    }
                }
                adapter = new PharmaClientRequestAdapter(PharmacyOrderListActivity.this, chartList);
                LinearLayoutManager llm = new LinearLayoutManager(PharmacyOrderListActivity.this);
                llm.setOrientation(RecyclerView.VERTICAL);
                orderRecycler.setLayoutManager(llm);
                orderRecycler.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PharmacyOrderListActivity.this, EPharmachyListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getDetails(ConfirmOrder confirmOrder) {
        Intent intent = new Intent(PharmacyOrderListActivity.this, PharmacyRequestDetailsActivity.class);
        intent.putExtra("VendorDetails", ed);
        intent.putExtra("ConfirmOrder", confirmOrder);
        startActivity(intent);
        finish();
    }
}
