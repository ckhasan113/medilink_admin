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
import com.example.adminapp.pojo.NurseAddtoCart;
import com.example.adminapp.pojo.NurseVendorDetails;
import com.example.adminapp.pojo.PhysiotherapistVendorDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NurseVendorListAdapter extends RecyclerView.Adapter<NurseVendorListAdapter.NurseVendorListAdapterViewHolder> {

    private Context context;
    private List<NurseVendorDetails> cartList;

    private NurseVendorListAdapterListener listener;

    public NurseVendorListAdapter(Context context, List<NurseVendorDetails> cartList) {
        this.context = context;
        this.cartList = cartList;
        listener = (NurseVendorListAdapterListener) context;
    }

    @NonNull
    @Override
    public NurseVendorListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NurseVendorListAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.physiotherapist_vendor_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NurseVendorListAdapterViewHolder holder, int i) {
        final NurseVendorDetails details = cartList.get(i);

        Picasso.get().load(Uri.parse(details.getImage())).into(holder.imageView);
        holder.nameTV.setText("Name: "+details.getName());
        holder.addressTV.setText("Area: "+details.getArea());

        holder.getPhysioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getNurseTakenList(details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class NurseVendorListAdapterViewHolder extends RecyclerView.ViewHolder{

        LinearLayout getPhysioLayout;

        TextView nameTV, addressTV;

        ImageView imageView;

        public NurseVendorListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            getPhysioLayout = itemView.findViewById(R.id.getPackage);
            nameTV = itemView.findViewById(R.id.physiotherapistName);
            addressTV = itemView.findViewById(R.id.physiotherapistAddress);
            imageView = itemView.findViewById(R.id.physiotherapistImage);
        }
    }

    public interface NurseVendorListAdapterListener{
        void getNurseTakenList(NurseVendorDetails vendorDetails);
    }
}
