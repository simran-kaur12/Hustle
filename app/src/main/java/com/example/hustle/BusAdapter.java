package com.example.hustle;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;

    Activity activity;

    ArrayList<MainModel> alm;

    public BusAdapter(RecyclerViewInterface recyclerViewInterface, Activity activity, ArrayList<MainModel> alm) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.activity =activity;
        this.alm = alm;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_list,parent,false);

        return new BusViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {

        MainModel model = alm.get(position);

        holder.ar_time.setText(model.getArrival_Time());

        holder.travelCom.setText(model.getTravel_Agency());

        holder.Category.setText(model.getCategory());

        holder.price.setText(model.getFare().concat("/-"));

        holder.duration.setText(model.getDuration());

        holder.bus_num.setText(model.getBus_No());

    }

    @Override
    public int getItemCount() {
        return alm.size();
    }


    static class BusViewHolder extends RecyclerView.ViewHolder{

        TextView ar_time,travelCom,Category,price,duration,bus_num;

        public BusViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            ar_time = itemView.findViewById(R.id.arrival_time);

            travelCom = itemView.findViewById(R.id.travel_com);

            Category = itemView.findViewById(R.id.ac_nonAC);

            price = itemView.findViewById(R.id.fare);

            duration = itemView.findViewById(R.id.duration);

            bus_num = itemView.findViewById(R.id.busNo);

            itemView.setOnClickListener(view -> {

                if(recyclerViewInterface != null){

                    int pos = getAbsoluteAdapterPosition();

                    if(pos!= RecyclerView.NO_POSITION)

                        recyclerViewInterface.onItemClick(pos);


                }
            });
        }
    }
}
