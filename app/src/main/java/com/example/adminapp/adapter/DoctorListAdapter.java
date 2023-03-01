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
import com.example.adminapp.pojo.DoctorDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorListAdapterViewHolder> {

    private Context context;
    private List<DoctorDetails> detailsList;

    private DoctorListAdapterListener listener;

    public DoctorListAdapter(Context context, List<DoctorDetails> detailsList) {
        this.context = context;
        this.detailsList = detailsList;
        listener = (DoctorListAdapterListener) context;
    }

    @NonNull
    @Override
    public DoctorListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DoctorListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.doctor_list_recycler_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListAdapterViewHolder holder, int i) {
        final DoctorDetails details = detailsList.get(i);

        Picasso.get().load(Uri.parse(details.getImageURI())).into(holder.doctorImage);
        holder.doctorName.setText("Dr. "+details.getFirstName()+" "+details.getLastName());
        holder.doctorDegree.setText(details.getDegree());
        holder.doctorSpeciality.setText(details.getDepartment());
        holder.getDoctorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGetDoctorDetails(details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    class DoctorListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getDoctorDetails;
        ImageView doctorImage;
        TextView doctorName, doctorDegree, doctorSpeciality;
        public DoctorListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getDoctorDetails = itemView.findViewById(R.id.getDoctorDetails);

            doctorImage = itemView.findViewById(R.id.admin_panel_doctor_image);

            doctorName = itemView.findViewById(R.id.admin_panel_doctor_name);
            doctorDegree = itemView.findViewById(R.id.admin_panel_doctor_degree);
            doctorSpeciality = itemView.findViewById(R.id.admin_panel_doctor_speciality);
        }
    }

    public interface DoctorListAdapterListener{
        void onGetDoctorDetails(DoctorDetails details);
    }
}
