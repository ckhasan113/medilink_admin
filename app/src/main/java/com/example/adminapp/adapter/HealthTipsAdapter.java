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
import com.example.adminapp.pojo.HealthTips;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HealthTipsAdapter extends RecyclerView.Adapter<HealthTipsAdapter.HealthTipsAdapterViewHolder> {

    private Context context;
    private List<HealthTips> tipsList;

    private HealthTipsAdapterListener listener;

    public HealthTipsAdapter(Context context, List<HealthTips> tipsList) {
        this.context = context;
        this.tipsList = tipsList;
        listener = (HealthTipsAdapterListener) context;
    }

    @NonNull
    @Override
    public HealthTipsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HealthTipsAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.health_tips_list_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HealthTipsAdapterViewHolder holder, int i) {

        final HealthTips tips = tipsList.get(i);

        if (tips.getImage().equals("null")){
            holder.emptyTV.setVisibility(View.VISIBLE);
            holder.tipImage.setVisibility(View.GONE);
        }else {
            Picasso.get().load(Uri.parse(tips.getImage())).into(holder.tipImage);
        }
        holder.tipTV.setText(tips.getTitle());
        holder.getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTipDetails(tips);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tipsList.size();
    }

    class HealthTipsAdapterViewHolder extends RecyclerView.ViewHolder{
        CardView getDetails;
        ImageView tipImage;
        TextView emptyTV, tipTV;
        public HealthTipsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            getDetails = itemView.findViewById(R.id.getTipsDetails);
            tipImage = itemView.findViewById(R.id.tipListImage);
            emptyTV = itemView.findViewById(R.id.emptySMS_TV);
            tipTV = itemView.findViewById(R.id.health_tips_textTV);
        }
    }

    public interface HealthTipsAdapterListener{
        void onTipDetails(HealthTips ht);
    }
}
