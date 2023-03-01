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
import com.example.adminapp.pojo.EPharmaDetails;
import com.example.adminapp.pojo.PhysiotherapistVendorDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EPharmacyListAdapter extends RecyclerView.Adapter<EPharmacyListAdapter.EPharmacyListAdapterViewHolder> {

    private Context context;
    private List<EPharmaDetails> list;
    private EPharmacyListAdapterListener listener;

    public EPharmacyListAdapter(Context context, List<EPharmaDetails> list) {
        this.context = context;
        this.list = list;
        listener = (EPharmacyListAdapterListener) context;
    }

    @NonNull
    @Override
    public EPharmacyListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EPharmacyListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.physiotherapist_vendor_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EPharmacyListAdapterViewHolder holder, int position) {

        final EPharmaDetails ed = list.get(position);

        holder.getDetailsTV.setText("Get Pharmacy Order List");

        Picasso.get().load(Uri.parse(ed.getImage())).into(holder.imageView);
        holder.nameTV.setText(ed.getFarmName());
        holder.addressTV.setText(ed.getArea()+", "+ed.getCity());
        holder.getPhysioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getPharmacyDetails(ed);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EPharmacyListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getPhysioLayout;

        TextView nameTV, addressTV, getDetailsTV;

        ImageView imageView;

        public EPharmacyListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getPhysioLayout = itemView.findViewById(R.id.getPackage);
            nameTV = itemView.findViewById(R.id.physiotherapistName);
            addressTV = itemView.findViewById(R.id.physiotherapistAddress);
            imageView = itemView.findViewById(R.id.physiotherapistImage);
            getDetailsTV = itemView.findViewById(R.id.getDetailsTV);
        }
    }

    public interface EPharmacyListAdapterListener{
        void getPharmacyDetails(EPharmaDetails vendorDetails);
    }
}
