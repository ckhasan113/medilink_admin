package com.example.adminapp;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminapp.pojo.AdminInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference rootRef;
    private DatabaseReference adminRef;

    private EditText idEdt, passwordEdt;

    private String id, password;

    private CardView loginLayout;

    private LoadingDialog dialog;

    private TextView testTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        testTV = findViewById(R.id.test);

        rootRef = FirebaseDatabase.getInstance().getReference();
        adminRef = rootRef.child("Admin");

        dialog = new LoadingDialog(this, "Loading...");

        idEdt = findViewById(R.id.admin_log_in);
        passwordEdt = findViewById(R.id.password_log_in);
        loginLayout = findViewById(R.id.login_master);

        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = idEdt.getText().toString().trim();
                if (id.isEmpty()){
                    idEdt.setError(getString(R.string.required_field));
                    return;
                }

                password = passwordEdt.getText().toString().trim();
                if (password.isEmpty()){
                    passwordEdt.setError(getString(R.string.required_field));
                    return;
                }

                dialog.show();

                adminRef.child("Details").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        AdminInfo admin = dataSnapshot.getValue(AdminInfo.class);
                        if (id.equals(admin.getId()) && password.equals(admin.getPassword())){
                            String token = FirebaseInstanceId.getInstance().getToken();
                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("device_token", token);

                            rootRef.child("Users").child("adminID").child("Token").setValue(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    dialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "ID or Password not match", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
