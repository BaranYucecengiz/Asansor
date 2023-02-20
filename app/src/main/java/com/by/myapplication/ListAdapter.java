package com.by.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Elevator> {

    public ListAdapter(Context context, ArrayList<Elevator> elevatorsArrayList){
        super(context, R.layout.list_item, elevatorsArrayList);

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Elevator elevators = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.elev_pic);
        TextView elevator_id = convertView.findViewById(R.id.elevator_id);
        TextView elevator_address = convertView.findViewById(R.id.elevator_address);

        imageView.setImageResource(R.drawable.elev_logo);
        elevator_id.setText(elevators.getId());
        elevator_address.setText(elevators.getAddress());

        return  convertView;
    }
}
