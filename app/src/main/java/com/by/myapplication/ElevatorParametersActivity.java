package com.by.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.by.myapplication.databinding.ActivityElevatorParametersBinding;

public class ElevatorParametersActivity extends AppCompatActivity {

    ActivityElevatorParametersBinding binding;
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
    }
}