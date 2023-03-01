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
import com.example.adminapp.pojo.PhysiotherapistVendorDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhysiotherapistListAdapter extends RecyclerView.Adapter<PhysiotherapistListAdapter.PhysiotherapistListAdapterViewHolder> {

    private Context context;
    private List<PhysiotherapistVendorDetails> vendorDetailsList;

    private PhysiotherapistListAdapterListener listener;

    public PhysiotherapistListAdapter(Context context, List<PhysiotherapistVendorDetails> vendorDetailsList) {
        this.context = context;
        this.vendorDetailsList = vendorDetailsList;
        listener = (PhysiotherapistListAdapterListener) context;
    }

    @NonNull
    @Override
    public PhysiotherapistListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PhysiotherapistListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.physiotherapist_vendor_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhysiotherapistListAdapterViewHolder holder, int i) {
        final PhysiotherapistVendorDetails details = vendorDetailsList.get(i);

        Picasso.get().load(Uri.parse(details.getImage())).into(holder.imageView);
        holder.nameTV.setText("Name: "+details.getName());
        holder.addressTV.setText("Address: "+details.getArea()+", "+details.getCity());

        holder.getPhysioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getPhysiotherapist(details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorDetailsList.size();
    }

    class PhysiotherapistListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getPhysioLayout;

        TextView nameTV, addressTV;

        ImageView imageView;

        public PhysiotherapistListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getPhysioLayout = itemView.findViewById(R.id.getPackage);
            nameTV = itemView.findViewById(R.id.physiotherapistName);
            addressTV = itemView.findViewById(R.id.physiotherapistAddress);
            imageView = itemView.findViewById(R.id.physiotherapistImage);
        }
    }

    public interface PhysiotherapistListAdapterListener{
        void getPhysiotherapist(PhysiotherapistVendorDetails vendorDetails);
    }
}
