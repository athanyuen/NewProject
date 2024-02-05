package com.example.newproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private Context context;
    private ArrayList<Vehicle> vehicleList;

    public VehicleAdapter(Context context, ArrayList<Vehicle> vehicleList){
        this.context = context;
        this.vehicleList = vehicleList;
    }

    public void setVehicleList(ArrayList<Vehicle> vehicleList){
        this.vehicleList = vehicleList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.vehicle_item, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position){
        Vehicle vehicle = vehicleList.get(position);

        holder.vehicleTypeTextView.setText("Vehicle Type: " + vehicle.getVehicleType());
        holder.capacityTextView.setText("Capacity: " + vehicle.getCapacity());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VehicleProfileActivity.class);

                intent.putExtra("selectedVehicle", vehicle.getVehicleID());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return vehicleList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {

        public TextView vehicleTypeTextView, capacityTextView;

        public VehicleViewHolder(@NonNull View itemView){
            super(itemView);
            vehicleTypeTextView = itemView.findViewById(R.id.vehicleTypeTextView);
            capacityTextView = itemView.findViewById(R.id.capacityTextView);
        }

    }


}

