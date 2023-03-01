package com.example.adminapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.pojo.ForeignBooking;
import com.example.adminapp.pojo.HospitalBooking;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForeignHospitalBookingListAdapter extends RecyclerView.Adapter<ForeignHospitalBookingListAdapter.ForeignHospitalBookingListAdapterViewHolder> {

    private Context context;
    private List<ForeignBooking> bookingList;

    public ForeignHospitalBookingListAdapter(Context context, List<ForeignBooking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ForeignHospitalBookingListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForeignHospitalBookingListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.hospital_booking_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForeignHospitalBookingListAdapterViewHolder holder, int position) {

        final ForeignBooking hb = bookingList.get(position);

        holder.patientNameTV.setText("Mr. "+hb.getPatientDetails().getFirstName()+" "+hb.getPatientDetails().getLastName());
        holder.descriptionTV.setText("Description:\n"+hb.getDescription());
        holder.dateTV.setText(hb.getBookingDate());
        Picasso.get().load(Uri.parse(hb.getPatientDetails().getImageURI())).into(holder.patientImageIV);


    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class ForeignHospitalBookingListAdapterViewHolder extends RecyclerView.ViewHolder{

        CardView bookingLayout;
        ImageView patientImageIV;
        TextView patientNameTV, descriptionTV, dateTV;

        public ForeignHospitalBookingListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            bookingLayout = itemView.findViewById(R.id.bookingList);
            patientImageIV = itemView.findViewById(R.id.bookingPatientIV);
            patientNameTV = itemView.findViewById(R.id.bookingPatientNameTV);
            descriptionTV = itemView.findViewById(R.id.bookingPatientDescriptionTV);
            dateTV = itemView.findViewById(R.id.bookingDateTV);
        }
    }
}
