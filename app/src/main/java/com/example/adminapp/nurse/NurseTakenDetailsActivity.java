package com.example.adminapp.nurse;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adminapp.LoadingDialog;
import com.example.adminapp.R;
import com.example.adminapp.pojo.NurseAddtoCart;
import com.example.adminapp.pojo.NurseVendorDetails;
import com.squareup.picasso.Picasso;

public class NurseTakenDetailsActivity extends AppCompatActivity {

    private TextView packageNameTV, packagePriceTV, packageDateTV, patientNameTV, patientPhoneTV, patientEmailTV, doctorNameTV;

    private ImageView prescriptionIV;

    private LinearLayout callLO, emailLO;

    private NurseAddtoCart clientRequest;

    private NurseVendorDetails vendorDetails;

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_taken_details);

        vendorDetails = (NurseVendorDetails) getIntent().getSerializableExtra("NurseVendorDetails");
        clientRequest = (NurseAddtoCart) getIntent().getSerializableExtra("NurseTakenDetails");

        dialog = new LoadingDialog(NurseTakenDetailsActivity.this,"Loading...");
        dialog.show();

        packageNameTV = findViewById(R.id.request_details_nurse_admin_panel_pack_name);
        packagePriceTV = findViewById(R.id.request_details_nurse_admin_panel_pack_price);
        packageDateTV = findViewById(R.id.request_details_nurse_admin_panel_show_date);
        patientNameTV = findViewById(R.id.request_details_nurse_admin_panel_pat_name);
        patientPhoneTV = findViewById(R.id.request_details_nurse_admin_panel_pat_phone);
        patientEmailTV = findViewById(R.id.request_details_nurse_admin_panel_pat_email);
        doctorNameTV = findViewById(R.id.request_details_nurse_admin_panel_doc_ref);

        callLO = findViewById(R.id.request_details_nurse_admin_panel_callLO);
        emailLO = findViewById(R.id.request_details_nurse_admin_panel_mailLO);
        prescriptionIV = findViewById(R.id.request_details_nurse_admin_panel_prescription_view);

        packageNameTV.setText(clientRequest.getPackageDetails().getPackageName());
        packagePriceTV.setText(clientRequest.getPackageDetails().getPackagePrice());
        packageDateTV.setText(clientRequest.getNurseTakenDate());
        patientNameTV.setText("Name: "+clientRequest.getPatientDetails().getFirstName()+" "+clientRequest.getPatientDetails().getLastName());
        patientPhoneTV.setText(clientRequest.getPatientDetails().getPhone());
        patientEmailTV.setText(clientRequest.getPatientDetails().getEmail());
        doctorNameTV.setText(clientRequest.getNurseTakenDoctorRef());

        Picasso.get().load(Uri.parse(clientRequest.getNurseTakenPrectionImage())).into(prescriptionIV);

        dialog.dismiss();

        callLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + clientRequest.getPatientDetails().getPhone().trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                startActivity(intent);
            }
        });

        emailLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:"+clientRequest.getPatientDetails().getEmail());
                intent.setData(data);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NurseTakenDetailsActivity.this, NurseTakenListActivity.class);
        intent.putExtra("NurseVendorDetails", vendorDetails);
        startActivity(intent);
        finish();
    }
}
