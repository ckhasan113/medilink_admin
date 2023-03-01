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
import com.example.adminapp.pojo.ConfirmOrder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PharmaClientRequestAdapter extends RecyclerView.Adapter<PharmaClientRequestAdapter.PharmaClientRequestAdapterViewHolder> {

    private Context context;
    private List<ConfirmOrder> orderList;
    private PharmaClientRequestAdapterListener listener;

    public PharmaClientRequestAdapter(Context context, List<ConfirmOrder> orderList) {
        this.context = context;
        this.orderList = orderList;
        listener = (PharmaClientRequestAdapterListener) context;
    }

    @NonNull
    @Override
    public PharmaClientRequestAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PharmaClientRequestAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.client_pharma_request_row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PharmaClientRequestAdapterViewHolder holder, int position) {

        final ConfirmOrder cd = orderList.get(position);

        Picasso.get().load(Uri.parse(cd.getPatientDetails().getImageURI())).into(holder.image);
        holder.name.setText("Mr. "+cd.getPatientDetails().getFirstName()+" "+cd.getPatientDetails().getLastName());
        holder.address.setText(cd.getPatientDetails().getArea()+", "+cd.getPatientDetails().getCity());
        holder.price.setText("Price: "+cd.getTotalPlusVat());
        holder.getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getDetails(cd);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class PharmaClientRequestAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name, address, price;
        LinearLayout getDetails;
        public PharmaClientRequestAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            getDetails = itemView.findViewById(R.id.getRequestDetails);
            image = itemView.findViewById(R.id.request_user_iv);
            name = itemView.findViewById(R.id.request_user_name_TV);
            address = itemView.findViewById(R.id.request_user_address_tv);
            price = itemView.findViewById(R.id.request_price_tv);
        }
    }

    public interface PharmaClientRequestAdapterListener{
        void getDetails(ConfirmOrder confirmOrder);
    }
}
