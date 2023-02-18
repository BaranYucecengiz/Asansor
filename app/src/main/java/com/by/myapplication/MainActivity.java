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
        Intent intent = this.getIntent(); // Yollamış old intenti yakalıyor

        if(intent != null) {
            users = (Users) getIntent().getSerializableExtra("users");
        }

        // Databaseden çekilecek olan bilgiler
        /*String[] elevator_id = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] elevator_address = {"A Cad.", "B. Cad", "C. Cad", "D. Cad", "E. Cad", "F. Cad", "G. Cad", "H. Cad", "I. Cad", "J. Cad",};
        int lenght = 10;
        //-------------------------------------
        ArrayList<Elevators> elevatorsArrayList = new ArrayList<>();
        for (int i= 0; i < lenght; i++){
            Elevators elevators = new Elevators(users.user_elevators.get(i).getId(), users.user_elevators.get(i).getAddress());
            elevatorsArrayList.add(elevators);
        }*/
        ArrayList<Elevators> elev = users.getUser_elevators();
        listAdapter = new ListAdapter(MainActivity.this, users.getUser_elevators());
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(MainActivity.this, ElevatorParametersActivity.class);
                i.putExtra("elevator_id", users.user_elevators.get(position).getId());
                i.putExtra("elevator_adress", users.user_elevators.get(position).getAddress());
                i.putExtra("users", users);
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
        ArrayList<Elevators> filteredElevators;
        if(id.isEmpty()){
            filteredElevators = new ArrayList<>(users.getUser_elevators());
        }else{
            filteredElevators = new ArrayList<>();
            id = id.toLowerCase();
            for(Elevators elevator: users.getUser_elevators()){
                if(elevator.id.toLowerCase().contains(id)){
                    filteredElevators.add(elevator);
                }
            }
        }
        listAdapter = new ListAdapter(MainActivity.this, filteredElevators);
        binding.listview.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

}