package com.example.myclinic.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myclinic.Details;
import com.example.myclinic.Models.MainListModel;
import com.example.myclinic.R;

import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    Context context;
    List<MainListModel> list;

    public MainListAdapter(Context context, List<MainListModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_spots_main_list,parent,false));


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final MainListModel temp = list.get(position);

        holder.imageView.setImageResource(temp.getImage());
        holder.name.setText(temp.getName());
        holder.address.setText(temp.getAddress());
        holder.price.setText("€" + temp.getPrice());
        holder.disp.setText(temp.getDisp());
        if(holder.disp.getText().toString().equals("Not Available")){
            holder.disp.setText(temp.getDisp());
            holder.disp.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corner_not_ava));
            holder.disp.setTextColor(Color.parseColor("#FF3E1D"));
        }else if(holder.disp.getText().toString().equals("Available Now")){
            holder.disp.setText(temp.getDisp());
            holder.disp.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_corner_ava_now));
            holder.disp.setTextColor(Color.parseColor("#A8E42D"));
        }



        holder.buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Details.class);
                intent.putExtra("estacionamento_id", temp.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Button buttonDetails;
        TextView name, address, price, disp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            buttonDetails = itemView.findViewById(R.id.btnDetails);
            imageView = itemView.findViewById(R.id.imgTourismHr);
            name = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.title3);
            price = itemView.findViewById(R.id.title4);
            disp = itemView.findViewById(R.id.textViewDisp);

        }
    }


}
