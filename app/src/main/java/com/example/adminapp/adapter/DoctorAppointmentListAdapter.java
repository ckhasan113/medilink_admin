package com.example.adminapp.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminapp.R;
import com.example.adminapp.pojo.DoctorAppointments;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DoctorAppointmentListAdapter extends RecyclerView.Adapter<DoctorAppointmentListAdapter.DoctorAppointmentListAdapterViewHolder> {

    private Context context;
    private List<DoctorAppointments> appointmentsList;

    public DoctorAppointmentListAdapter(Context context, List<DoctorAppointments> appointmentsList) {
        this.context = context;
        this.appointmentsList = appointmentsList;
    }

    @NonNull
    @Override
    public DoctorAppointmentListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DoctorAppointmentListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.doctor_appointment_row_model, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentListAdapterViewHolder holder, int i) {
        final DoctorAppointments appointment = appointmentsList.get(i);

        Uri photoUri = Uri.parse(appointment.getPatientImageURI());
        String firstName = appointment.getPatientFirstName();
        String lastName = appointment.getPatientLastName();
        String description = appointment.getDescription();
        String area = appointment.getChamberArea();
        String city = appointment.getChamberCity();

        Picasso.get().load(photoUri).into(holder.patientIV);
        holder.nameTV.setText("Mr. "+firstName+" "+lastName);
        holder.descriptionTV.setText("Description:\n"+description);
        holder.addressTV.setText(area+", "+city);
        holder.dateTV.setText(appointment.getAppointmentDate());

        //holder.addressTV.setText(appointment.getDoctorID()+", "+appointment.getAppointmentKey()+", "+appointment.getPatientID());

    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


    class DoctorAppointmentListAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView patientIV;
        TextView nameTV, descriptionTV, addressTV, dateTV;
        CardView appointmentLayout;

        public DoctorAppointmentListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            patientIV = itemView.findViewById(R.id.appointmentPatientIV);
            nameTV = itemView.findViewById(R.id.appointmentPatientNameTV);
            descriptionTV = itemView.findViewById(R.id.appointmentPatientDescriptionTV);
            addressTV = itemView.findViewById(R.id.appointmentChamberAddressTV);
            dateTV = itemView.findViewById(R.id.appointmentDateTV);
            appointmentLayout = itemView.findViewById(R.id.appointmentList);
        }
    }
}
