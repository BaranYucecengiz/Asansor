package com.by.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.by.myapplication.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SearchView searchView_elevator;
    ListAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imgId = {R.drawable.elevator_logo};
        // Databaseden çekilecek olan bilgiler
        String[] elevator_id = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] elevator_address = {"A Cad.", "B. Cad", "C. Cad", "D. Cad", "E. Cad", "F. Cad", "G. Cad", "H. Cad", "I. Cad", "J. Cad",};
        int lenght = 10;
        //-------------------------------------
        ArrayList<Elevators> elevatorsArrayList = new ArrayList<>();
        for (int i= 0; i < lenght; i++){
            Elevators elevators = new Elevators(elevator_id[i], elevator_address[i], imgId[0]);
            elevatorsArrayList.add(elevators);
        }

        listAdapter = new ListAdapter(MainActivity.this, elevatorsArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(MainActivity.this, ElevatorParametersActivity.class);
                i.putExtra("elevator_id", elevator_id[position]);
                i.putExtra("elevator_adress", elevator_address[position]);
                i.putExtra("image_id", imgId[0]);
                startActivity(i);

            }
        });
        // Searchview

    }
}