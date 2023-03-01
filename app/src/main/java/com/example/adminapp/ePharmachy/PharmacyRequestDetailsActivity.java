package com.example.adminapp.ePharmachy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adminapp.R;
import com.example.adminapp.adapter.PharmacyOrderListAdapter;
import com.example.adminapp.pojo.ConfirmOrder;
import com.example.adminapp.pojo.EPharmaDetails;
import com.example.adminapp.pojo.OrderChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PharmacyRequestDetailsActivity extends AppCompatActivity {

    private ConfirmOrder cd;
    private EPharmaDetails ed;

    private TextView nameTV, statusTV, addressTV, priceTV, totalTV;

    private RecyclerView productRecycler;

    private List<OrderChart> chartList = new ArrayList<OrderChart>();

    private PharmacyOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_request_details);

        ed = (EPharmaDetails) getIntent().getSerializableExtra("VendorDetails");
        cd = (ConfirmOrder) getIntent().getSerializableExtra("ConfirmOrder");

        chartList = cd.getChartList();
        Collections.reverse(chartList);

        nameTV = findViewById(R.id.admin_panel_pharmacy_request_details_user_name_tv);
        statusTV = findViewById(R.id.admin_panel_pharmacy_req_details_status_tv);
        addressTV = findViewById(R.id.admin_panel_pharmacy_req_details_user_address_tv);
        priceTV = findViewById(R.id.admin_panel_pharmacy_showPrice);
        totalTV = findViewById(R.id.admin_panel_pharmacy_showPriceF);

        productRecycler = findViewById(R.id.admin_panel_pharmacy_request_details_product_recycler);

        nameTV.setText("Mr. "+cd.getPatientDetails().getFirstName()+" "+cd.getPatientDetails().getLastName());
        statusTV.setText("Status: order is "+cd.getStatus());
        addressTV.setText("Address: "+cd.getPatientDetails().getArea()+", "+cd.getPatientDetails().getCity());
        priceTV.setText(" "+cd.getTotalPrice()+"Tk.");
        totalTV.setText(" "+cd.getTotalPlusVat()+"Tk.");

        adapter = new PharmacyOrderListAdapter(PharmacyRequestDetailsActivity.this, chartList);
        LinearLayoutManager llm = new LinearLayoutManager(PharmacyRequestDetailsActivity.this);
        llm.setOrientation(RecyclerView.VERTICAL);
        productRecycler.setLayoutManager(llm);
        productRecycler.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PharmacyRequestDetailsActivity.this, PharmacyOrderListActivity.class);
        intent.putExtra("VendorDetails", ed);
        startActivity(intent);
        finish();
    }
}
