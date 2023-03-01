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
import com.example.adminapp.pojo.ForeignDoctorDetails;
import com.example.adminapp.pojo.HospitalDoctorDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForeignHospitalDoctorListAdapter extends RecyclerView.Adapter<ForeignHospitalDoctorListAdapter.ForeignHospitalDoctorListAdapterViewHolder> {

    private Context context;
    private List<ForeignDoctorDetails> doctorDetailsList;

    private ForeignHospitalDoctorListAdapterListener listener;

    public ForeignHospitalDoctorListAdapter(Context context, List<ForeignDoctorDetails> doctorDetailsList) {
        this.context = context;
        this.doctorDetailsList = doctorDetailsList;
        listener = (ForeignHospitalDoctorListAdapterListener) context;
    }

    @NonNull
    @Override
    public ForeignHospitalDoctorListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ForeignHospitalDoctorListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.hospital_doctor_list_recycler_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForeignHospitalDoctorListAdapterViewHolder holder, int i) {
        final ForeignDoctorDetails doctor = doctorDetailsList.get(i);

        Uri photoUri = Uri.parse(doctor.getImageURI());
        Picasso.get().load(photoUri).into(holder.docImage);
        holder.docName.setText("Dr. "+doctor.getName());
        holder.docDegree.setText(doctor.getDegree());
        holder.docSpeciality.setText(doctor.getSpeciality());
        holder.docAppointment.setText(doctor.getPatient_count()+" Appointments");

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


    class ForeignHospitalDoctorListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getDetailsLayout;

        ImageView docImage;
        TextView docName, docDegree, docSpeciality, docAppointment;
        public ForeignHospitalDoctorListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getDetailsLayout = itemView.findViewById(R.id.getDoctorDetails);

            docImage = itemView.findViewById(R.id.doctor_image);
            docName = itemView.findViewById(R.id.doctor_name);
            docDegree = itemView.findViewById(R.id.doctor_degree);
            docSpeciality = itemView.findViewById(R.id.doctor_speciality);
            docAppointment = itemView.findViewById(R.id.doctor_appointment_number);
        }
    }

    public interface ForeignHospitalDoctorListAdapterListener{
        void onDoctorDetails(ForeignDoctorDetails doctorDetails);
    }
}
