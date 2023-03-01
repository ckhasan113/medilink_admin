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
import com.example.adminapp.pojo.NurseAddtoCart;
import com.example.adminapp.pojo.PhysiotherapistBooking;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NurseTakenListAdapter extends RecyclerView.Adapter<NurseTakenListAdapter.NurseTakenListAdapterViewHolder> {

    private Context context;
    private List<NurseAddtoCart> list;
    private NurseTakenListAdapterListener listener;

    public NurseTakenListAdapter(Context context, List<NurseAddtoCart> list) {
        this.context = context;
        this.list = list;
        listener = (NurseTakenListAdapterListener) context;
    }

    @NonNull
    @Override
    public NurseTakenListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NurseTakenListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.my_physiotherapist_booking_list_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NurseTakenListAdapterViewHolder holder, int i) {

        final NurseAddtoCart pb = list.get(i);
        Picasso.get().load(Uri.parse(pb.getPatientDetails().getImageURI())).into(holder.image);

        holder.name.setText("Mr. "+pb.getPatientDetails().getFirstName()+" "+pb.getPatientDetails().getLastName());
        holder.packageName.setText("Package: "+pb.getPackageDetails().getPackageName());
        holder.price.setText("Price: "+pb.getPackageDetails().getPackagePrice());
        holder.getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getNurseTakenRequestDetails(pb);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NurseTakenListAdapterViewHolder extends RecyclerView.ViewHolder{

        CardView getDetails;
        ImageView image;
        TextView name, packageName, price;
        public NurseTakenListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getDetails = itemView.findViewById(R.id.getNurseTakenRequestDetailsAdminPanel);
            image = itemView.findViewById(R.id.physioImage);
            name = itemView.findViewById(R.id.physioVendorName);
            packageName = itemView.findViewById(R.id.physioPackageName);
            price = itemView.findViewById(R.id.physioPrice);
        }
    }

    public interface NurseTakenListAdapterListener{
        void getNurseTakenRequestDetails(NurseAddtoCart taken);
    }

}
