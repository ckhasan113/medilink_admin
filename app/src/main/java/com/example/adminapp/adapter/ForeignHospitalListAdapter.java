package com.example.adminapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.pojo.ForeignHospitalDetails;
import com.example.adminapp.pojo.HospitalDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForeignHospitalListAdapter extends RecyclerView.Adapter<ForeignHospitalListAdapter.ForeignHospitalListAdapterViewHolder> {

    private Context context;
    private List<ForeignHospitalDetails> detailsList;
    private ForeignHospitalListAdapterListener listener;
    private int count = 0;
    private String country;

    private DatabaseReference doctorRef;

    public ForeignHospitalListAdapter(Context context, List<ForeignHospitalDetails> detailsList, String country) {
        this.context = context;
        this.detailsList = detailsList;
        this.country = country;
        listener = (ForeignHospitalListAdapterListener) context;
    }

    @NonNull
    @Override
    public ForeignHospitalListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ForeignHospitalListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.hospital_list_recycler_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ForeignHospitalListAdapterViewHolder holder, int i) {
        final ForeignHospitalDetails hd = detailsList.get(i);

        doctorRef = FirebaseDatabase.getInstance().getReference().child("Foreign Doctor").child("Country").child(country).child("Hospital").child(hd.getHospitalID()).child("DoctorList");
        Uri photoUri = Uri.parse(hd.getImage());
        Picasso.get().load(photoUri).into(holder.hospitalImageIV);

        holder.hospitalNameTV.setText(hd.getName());
        holder.hospitalAddressTV.setText(hd.getArea()+", "+hd.getCity());
        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = 0;
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    count++;
                }
                holder.hospitalDoctorTV.setText(String.valueOf(count)+" Doctor available");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.hospitalDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickBooking(hd, country);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    class ForeignHospitalListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout hospitalDetailsLayout;
        ImageView hospitalImageIV;
        TextView hospitalNameTV, hospitalAddressTV, hospitalDoctorTV;
        public ForeignHospitalListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            hospitalDetailsLayout = itemView.findViewById(R.id.hospitalDetails);
            hospitalImageIV = itemView.findViewById(R.id.hospitalImageIV);
            hospitalNameTV = itemView.findViewById(R.id.hospitalNameTV);
            hospitalAddressTV = itemView.findViewById(R.id.hospitalAddressTV);
            hospitalDoctorTV = itemView.findViewById(R.id.hospitalDoctorNumberTV);
        }
    }

    public interface ForeignHospitalListAdapterListener{
        void onClickBooking(ForeignHospitalDetails hospital, String country);
    }
}
