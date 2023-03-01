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
import com.example.adminapp.pojo.HospitalBooking;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HospitalBookingListAdapter extends RecyclerView.Adapter<HospitalBookingListAdapter.BookingListAdapterViewHolder> {

    private Context context;
    private List<HospitalBooking> bookingList;

    public HospitalBookingListAdapter(Context context, List<HospitalBooking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.hospital_booking_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingListAdapterViewHolder holder, int position) {

        final HospitalBooking hb = bookingList.get(position);

        holder.patientNameTV.setText("Mr. "+hb.getPatientFirstName()+" "+hb.getPatientLastName());
        holder.descriptionTV.setText("Description:\n"+hb.getDescription());
        holder.dateTV.setText(hb.getBookingDate());
        Picasso.get().load(Uri.parse(hb.getPatientImage())).into(holder.patientImageIV);


    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class BookingListAdapterViewHolder extends RecyclerView.ViewHolder{

        CardView bookingLayout;
        ImageView patientImageIV;
        TextView patientNameTV, descriptionTV, dateTV;

        public BookingListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            bookingLayout = itemView.findViewById(R.id.bookingList);
            patientImageIV = itemView.findViewById(R.id.bookingPatientIV);
            patientNameTV = itemView.findViewById(R.id.bookingPatientNameTV);
            descriptionTV = itemView.findViewById(R.id.bookingPatientDescriptionTV);
            dateTV = itemView.findViewById(R.id.bookingDateTV);
        }
    }
}
