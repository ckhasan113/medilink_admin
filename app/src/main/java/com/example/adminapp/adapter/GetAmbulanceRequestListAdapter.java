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
import com.example.adminapp.pojo.AmbulanceTaken;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GetAmbulanceRequestListAdapter extends RecyclerView.Adapter<GetAmbulanceRequestListAdapter.GetAmbulanceRequestDetailsAdapterViewHolder> {

    private Context context;
    private List<AmbulanceTaken> takenList;
    private GetAmbulanceRequestDetailsAdapterListener listener;

    public GetAmbulanceRequestListAdapter(Context context, List<AmbulanceTaken> takenList) {
        this.context = context;
        this.takenList = takenList;
        listener = (GetAmbulanceRequestDetailsAdapterListener) context;
    }

    @NonNull
    @Override
    public GetAmbulanceRequestDetailsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetAmbulanceRequestDetailsAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.ambulance_list_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GetAmbulanceRequestDetailsAdapterViewHolder holder, int position) {

        final AmbulanceTaken ambulance = takenList.get(position);

        Picasso.get().load(Uri.parse(ambulance.getPatientDetails().getImageURI())).into(holder.image);
        holder.nameTV.setText("Mr. "+ambulance.getPatientDetails().getFirstName()+" "+ambulance.getPatientDetails().getLastName());
        holder.pickUpTV.setText("Pick Up From: "+ambulance.getFrom());
        holder.phoneTV.setText("Phone: "+ambulance.getPatientDetails().getPhone());
        holder.getDetailsLO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getAmbulanceRequestDetails(ambulance);
            }
        });

    }

    @Override
    public int getItemCount() {
        return takenList.size();
    }

    class GetAmbulanceRequestDetailsAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView nameTV, pickUpTV, phoneTV;
        LinearLayout getDetailsLO;

        public GetAmbulanceRequestDetailsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.ambulance_request_user_iv);
            nameTV = itemView.findViewById(R.id.ambulance_request_user_name_TV);
            pickUpTV = itemView.findViewById(R.id.ambulance_request_user_pickup_address_tv);
            phoneTV = itemView.findViewById(R.id.ambulance_request_user_phone_tv);
            getDetailsLO = itemView.findViewById(R.id.getAmbulanceRequestDetails);
        }
    }

    public interface GetAmbulanceRequestDetailsAdapterListener{
        void getAmbulanceRequestDetails(AmbulanceTaken ambulance);
    }
}
