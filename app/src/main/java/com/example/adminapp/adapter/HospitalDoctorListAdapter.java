package com.example.adminapp.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adminapp.R;
import com.example.adminapp.pojo.HospitalDoctorDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HospitalDoctorListAdapter extends RecyclerView.Adapter<HospitalDoctorListAdapter.HospitalDoctorListAdapterViewHolder> {

    private Context context;
    private List<HospitalDoctorDetails> doctorDetailsList;

    private HospitalDoctorListAdapterListener listener;

    public HospitalDoctorListAdapter(Context context, List<HospitalDoctorDetails> doctorDetailsList) {
        this.context = context;
        this.doctorDetailsList = doctorDetailsList;
        listener = (HospitalDoctorListAdapterListener) context;
    }

    @NonNull
    @Override
    public HospitalDoctorListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HospitalDoctorListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.hospital_doctor_list_recycler_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalDoctorListAdapterViewHolder holder, int i) {
        final HospitalDoctorDetails doctor = doctorDetailsList.get(i);

        Uri photoUri = Uri.parse(doctor.getImage());
        Picasso.get().load(photoUri).into(holder.docImage);
        holder.docName.setText("Dr. "+doctor.getName());
        holder.docDegree.setText(doctor.getDegree());
        holder.docSpeciality.setText(doctor.getSpeciality());
        holder.docAppointment.setText(doctor.getPatientCount()+" Appointments");

        holder.getDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDoctorDetails(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorDetailsList.size();
    }


    class HospitalDoctorListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getDetailsLayout;

        ImageView docImage;
        TextView docName, docDegree, docSpeciality, docAppointment;
        public HospitalDoctorListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getDetailsLayout = itemView.findViewById(R.id.getDoctorDetails);

            docImage = itemView.findViewById(R.id.doctor_image);
            docName = itemView.findViewById(R.id.doctor_name);
            docDegree = itemView.findViewById(R.id.doctor_degree);
            docSpeciality = itemView.findViewById(R.id.doctor_speciality);
            docAppointment = itemView.findViewById(R.id.doctor_appointment_number);
        }
    }

    public interface HospitalDoctorListAdapterListener{
        void onDoctorDetails(HospitalDoctorDetails doctorDetails);
    }
}
