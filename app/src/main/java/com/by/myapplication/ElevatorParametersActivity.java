package com.by.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.by.myapplication.databinding.ActivityElevatorParametersBinding;

import java.util.ArrayList;

public class ElevatorParametersActivity extends AppCompatActivity {

    ActivityElevatorParametersBinding binding;
    Spinner spinner_parent, spinner_child;

    ArrayList<String> arrayList_parent;
    ArrayAdapter<String> arrayAdapter_parent;

    ArrayList<String> arrayList_p, arrayList_d, arrayList_n;
    ArrayAdapter<String> arrayAdapter_child;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityElevatorParametersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent(); // Yollamış old intenti yakalıyor

        if(intent != null){
            String id = intent.getStringExtra("elevator_id");
            String address = intent.getStringExtra("elevator_adress");
            int imgId = intent.getIntExtra("image_id", R.drawable.elevator_logo);

            binding.elevatorId.setText(id);
            binding.elevatorAddress.setText(address);
            binding.logo.setImageResource(imgId);
        }
        //================= Parrent Spinner proc =================
        spinner_parent = (Spinner)findViewById(R.id.spinnerParent);


        arrayList_parent = new ArrayList<>();
        arrayList_parent.add("P");
        arrayList_parent.add("D");
        arrayList_parent.add("N");
        arrayAdapter_parent = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_parent);

        spinner_parent.setAdapter(arrayAdapter_parent);
        //================= Parrent Spinner END =================

        //================= Child Spinner proc =================

        spinner_child = (Spinner)findViewById(R.id.spinnerChild);
        arrayList_p = new ArrayList<>();
        arrayList_p.add("P01 - Çalışma Modu");
        arrayList_p.add("P02 - Motor Yönü");
        arrayList_p.add("P03 - Durdurma Modu");
        arrayList_p.add("P04 - Kapı kapanırken sıkışmayı önleme süresi");
        arrayList_d = new ArrayList<>();
        arrayList_d.add("D01 - Kapı açılışındaki başlangıç uzaklığı");
        arrayList_d.add("D02 - Kapı açılışındaki yavaşlama uzaklığı");
        arrayList_n = new ArrayList<>();
        arrayList_n.add("N01 - Frekans / Hız");
        arrayList_n.add("N02 - Akım");
        arrayList_n.add("N03 - Voltaj");


        spinner_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) {
                    arrayAdapter_child = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_p);
                }
                if (position == 1) {
                    arrayAdapter_child = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_d);
                }
                if (position == 2) {
                    arrayAdapter_child = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_n);
                }
                spinner_child.setAdapter(arrayAdapter_child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //================= Child Spinner END =================


    }
}