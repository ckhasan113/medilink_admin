package com.example.adminapp.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adminapp.R;
import com.example.adminapp.chamber.ChamberDetails;

import java.util.List;

public class ChamberAdapter extends RecyclerView.Adapter<ChamberAdapter.NewChamberAdapterViewHolder> {

    private Context context;
    private List<ChamberDetails> chamberList;
    private GetChamberDetailsInfo chamberDetailsInfo;

    public ChamberAdapter(Context context, List<ChamberDetails> chamberList) {
        this.context = context;
        this.chamberList = chamberList;
        chamberDetailsInfo = (GetChamberDetailsInfo) context;
    }

    @NonNull
    @Override
    public NewChamberAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NewChamberAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.chamber_list_row_model, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewChamberAdapterViewHolder holder, int i) {

        final ChamberDetails chamber = chamberList.get(i);

        holder.addressTV.setText(chamber.getArea()+"\n"+chamber.getCity()+", House No. "+chamber.getHouse());
        holder.timeTV.setText(chamber.getStart()+" - "+chamber.getEnd());

        holder.chamberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamberDetailsInfo.chamberDetailsInfo(chamber);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chamberList.size();
    }

    class NewChamberAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView addressTV, timeTV;
        CardView chamberLayout;

        public NewChamberAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTV = itemView.findViewById(R.id.chamber_schedule_addressTV);
            timeTV = itemView.findViewById(R.id.chamber_schedule_timeTV);
            chamberLayout = itemView.findViewById(R.id.chamber);
        }
    }

    public interface GetChamberDetailsInfo{
        void chamberDetailsInfo(ChamberDetails chamberDetails);
    }
}
