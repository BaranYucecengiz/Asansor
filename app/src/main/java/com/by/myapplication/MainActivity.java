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
    private Users users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        searchView_elevator = (SearchView) findViewById(R.id.searchview_elevator);
        users = Users.getInstance();

        listAdapter = new ListAdapter(MainActivity.this, users.getUser_elevators());
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(MainActivity.this, ElevatorParametersActivity.class);
                i.putExtra("position", position);
                startActivity(i);

            }
        });
        // Searchview
        searchView_elevator.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterElevatorsById(newText.trim());
                return false;
            }
        });
    }

    public void filterElevatorsById(String id) {
        ArrayList<Elevator> filteredElevators;
        if(id.isEmpty()){
            filteredElevators = new ArrayList<>(users.getUser_elevators());
        }else{
            filteredElevators = new ArrayList<>();
            id = id.toLowerCase();
            for(Elevator elevator: users.getUser_elevators()){
                if(elevator.getId().toLowerCase().contains(id)){
                    filteredElevators.add(elevator);
                }
            }
        }
        listAdapter = new ListAdapter(MainActivity.this, filteredElevators);
        binding.listview.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

}