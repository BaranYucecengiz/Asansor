package com.by.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.by.myapplication.databinding.ActivityElevatorParametersBinding;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ElevatorParametersActivity extends AppCompatActivity {

    ActivityElevatorParametersBinding binding;
    ParameterObject parameterObject;
    Spinner spinner_parent, spinner_child;

    ArrayList<String> arrayList_parent;
    ArrayAdapter<String> arrayAdapter_parent;

    ArrayList<String> arrayList_child_p, arrayList_child_d, arrayList_child_n;
    ArrayAdapter<String> arrayAdapter_child;
    private Users users;
    private int elevator_position;
    private String code_text;
    private String subCode_text;
    TextView elevator_parameters;
    TextView parameter_slider_text;
    Slider parameter_slider;
    private Button update_button;
    PDNParameter pdnParameter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityElevatorParametersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        users = Users.getInstance();
        parameter_slider = (Slider) findViewById(R.id.parameter_slider);
        parameter_slider_text = (TextView) findViewById(R.id.parameter_slider_text);
        Intent intent = this.getIntent(); // Yollamış old intenti yakalıyor

        if (intent != null) {
            elevator_position = intent.getIntExtra("position", 0);

            binding.elevatorId.setText(users.getUser_elevators().get(elevator_position).getId());
            binding.elevatorAddress.setText(users.getUser_elevators().get(elevator_position).getAddress());
            binding.logo.setImageResource(R.drawable.elevator_logo);
        }
        parameterObject = jsonToObject();
        //================= Spinner proc ================= // Hepsi json dosyasından okunacak
        spinner_parent = (Spinner) findViewById(R.id.spinnerParent);
        spinner_child = (Spinner) findViewById(R.id.spinnerChild);
        arrayList_parent = new ArrayList<>();
        arrayList_child_p = new ArrayList<>();
        arrayList_child_d = new ArrayList<>();
        arrayList_child_n = new ArrayList<>();
        arrayList_parent.add("P");
        arrayList_parent.add("D");
        arrayList_parent.add("N");
        parameterObject.getD().get(0);
        for (Map.Entry<String, PDNParameter> paramObject : parameterObject.getD().entrySet()) {
            arrayList_child_d.add(paramObject.getKey() + "-" + paramObject.getValue().getDescription_tr());
        }

        for (Map.Entry<String, PDNParameter> paramObject : parameterObject.getN().entrySet()) {
            arrayList_child_n.add(paramObject.getKey() + "-" + paramObject.getValue().getDescription_tr());
        }

        for (Map.Entry<String, PDNParameter> paramObject : parameterObject.getP().entrySet()) {
            arrayList_child_p.add(paramObject.getKey() + "-" + paramObject.getValue().getDescription_tr());
        }
        arrayAdapter_parent = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_parent);
        spinner_parent.setAdapter(arrayAdapter_parent);
        //================= Spinner END =================


        spinner_parent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position == 0) {
                    arrayAdapter_child = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_child_p);

                }
                if (position == 1) {
                    arrayAdapter_child = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_child_d);
                }
                if (position == 2) {
                    arrayAdapter_child = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_child_n);
                }
                spinner_child.setAdapter(arrayAdapter_child);

                spinner_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        code_text = spinner_parent.getSelectedItem().toString();
                        subCode_text = spinner_child.getSelectedItem().toString().substring(0, 3);

                        if (code_text == "P")
                            pdnParameter = parameterObject.getP().get(subCode_text);
                        if (code_text == "D")
                            pdnParameter = parameterObject.getD().get(subCode_text);
                        if (code_text == "N")
                            pdnParameter = parameterObject.getN().get(subCode_text);


                        String text_all = "";
                        try {
                            for (Map.Entry<String, EnumObject> paramObject : pdnParameter.getEnums().entrySet()) {
                                text_all += paramObject.getKey() + ":  " + paramObject.getValue().getDescription_tr() + "\n";
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        elevator_parameters = (TextView) findViewById(R.id.elevator_parameters);
                        elevator_parameters.setText(text_all);
                        try {
                            ((LinearLayout) findViewById(R.id.linlay_bar)).setVisibility(View.VISIBLE);
                            parameter_slider.setValueFrom(pdnParameter.getRange_start());
                            parameter_slider.setValue(pdnParameter.getDefault_val());
                            parameter_slider_text.setText(pdnParameter.getDefault_val().toString());
                            parameter_slider.setValueTo(pdnParameter.getRange_end());
                            parameter_slider.setStepSize(1);


                        } catch (Exception e) {
                            ((LinearLayout) findViewById(R.id.linlay_bar)).setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        parameter_slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                value = (int) value;
                parameter_slider_text.setText(Float.toString(value));
                System.out.println("Before : " + subCode_text);
                if(code_text == "D" || code_text == "N")
                    subCode_text = subCode_text.toLowerCase();
                System.out.println("After : " + subCode_text);
                users.getUser_elevators().get(elevator_position).getElevator(code_text).replace(subCode_text, (int) parameter_slider.getValue());

            }

        });

        update_button = (Button) findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute();
            }
        });
    }
    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                DB db = new DB();
                db.updateElevatorWithPropertyMap(users.getUser_elevators().get(elevator_position).getId(),
                        users.getUser_elevators().get(elevator_position).getD(),
                        "D");
                db.updateElevatorWithPropertyMap(users.getUser_elevators().get(elevator_position).getId(),
                        users.getUser_elevators().get(elevator_position).getP(),
                        "P");
                db.updateElevatorWithPropertyMap(users.getUser_elevators().get(elevator_position).getId(),
                        users.getUser_elevators().get(elevator_position).getN(),
                        "N");

            } catch (Exception e) {

            }
            return null;
        }
    }
    private ParameterObject jsonToObject() {
        Gson gson = new Gson();
        String json = "{\n" +
                "    \"D\": {\n" +
                "        \"D01\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Starting Distance Of Door Opening\",\n" +
                "            \"description_tr\": \"Kapı Açılışındaki Başlangıç Uzaklığı\",\n" +
                "            \"range_end\": 200,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D02\": {\n" +
                "            \"default_val\": 120,\n" +
                "            \"description_eng\": \"Deceleration Distance Of Door Opening\",\n" +
                "            \"description_tr\": \"Kapı Açılışındaki Yavaşlama Uzaklığı\",\n" +
                "            \"range_end\": 300,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D03\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Buffer Distance Of Door Opening In Place\",\n" +
                "            \"description_tr\": \"Kapı Açılışı Aralık Mesafesi\",\n" +
                "            \"range_end\": 200,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D04\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Keep Distance Of Open Door\",\n" +
                "            \"description_tr\": \"Kapı Açılışında Mesafe\",\n" +
                "            \"range_end\": 200,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D05\": {\n" +
                "            \"default_val\": 30,\n" +
                "            \"description_eng\": \"Keep Time Of Open Door\",\n" +
                "            \"description_tr\": \"Kapının Açılış Süresi\",\n" +
                "            \"range_end\": 1000,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D06\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"D07\": {\n" +
                "            \"default_val\": 40,\n" +
                "            \"description_eng\": \"Starting Speed Of Door Opening\",\n" +
                "            \"description_tr\": \"Kapı Açılışının Başlangıç Hızı\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D08\": {\n" +
                "            \"default_val\": 100,\n" +
                "            \"description_eng\": \"Maximum Opening Speed\",\n" +
                "            \"description_tr\": \"Maksimum Açılış Hızı\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D09\": {\n" +
                "            \"default_val\": 20,\n" +
                "            \"description_eng\": \"Speed Of Door Opening In Place\",\n" +
                "            \"description_tr\": \"Kapının Açılma Hızı\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D10\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Keep Speed Of Open Door\",\n" +
                "            \"description_tr\": \"Kapı Açılışı Tutma Mesafesi\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D11\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Hold Speed Of Door\",\n" +
                "            \"description_tr\": \"Kapıyı Tutma Hızı\",\n" +
                "            \"range_end\": 80,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D12\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Accelerate Step Of Open Door\",\n" +
                "            \"description_tr\": \"Kapı Açılışının Hızlanma Artışı Miktarı\",\n" +
                "            \"range_end\": 40,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D13\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Decelerate Step Of Open Door\",\n" +
                "            \"description_tr\": \"Kapı Açılışının Yavaşlama Artışı Miktarı\",\n" +
                "            \"range_end\": 40,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D14\": {\n" +
                "            \"default_val\": 200,\n" +
                "            \"description_eng\": \"Starting Current Of Opening Door\",\n" +
                "            \"description_tr\": \"Kapı Açılışının Başlangıç Akımı\",\n" +
                "            \"range_end\": 300,\n" +
                "            \"range_start\": 5,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D15\": {\n" +
                "            \"default_val\": 200,\n" +
                "            \"description_eng\": \"Current Of Opening Door Anti Pinch\",\n" +
                "            \"description_tr\": \"Kapı Açılışının Sıkışma Engelleme Modundaki Akımı\",\n" +
                "            \"range_end\": 300,\n" +
                "            \"range_start\": 5,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D16\": {\n" +
                "            \"default_val\": 20,\n" +
                "            \"description_eng\": \"Hold Current Of Door Opening In Place\",\n" +
                "            \"description_tr\": \"Kapı Açılışının Tutma Frekansı\",\n" +
                "            \"range_end\": 100,\n" +
                "            \"range_start\": 5,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D17\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Starting Distance Of Door Closing\",\n" +
                "            \"description_tr\": \"Kapı Kapanışının Başlangıç Uzaklığı\",\n" +
                "            \"range_end\": 200,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D18\": {\n" +
                "            \"default_val\": 120,\n" +
                "            \"description_eng\": \"Deceleration Distance Of Door Closing\",\n" +
                "            \"description_tr\": \"Kapı Açılışındaki Yavaşlama Uzaklığı\",\n" +
                "            \"range_end\": 300,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D19\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Buffer Distance Of Door Opening In Place\",\n" +
                "            \"description_tr\": \"Kapı Açılışı Aralık Mesafesi\",\n" +
                "            \"range_end\": 200,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D20\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Distance Between Door Closing And Locking\",\n" +
                "            \"description_tr\": \"Kapının Kapanma Ve Kilitlenme Arasındaki Mesafe\",\n" +
                "            \"range_end\": 200,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D21\": {\n" +
                "            \"default_val\": 30,\n" +
                "            \"description_eng\": \"Time Between Door Closing And Locking\",\n" +
                "            \"description_tr\": \"Kapının Kapanma Ve Kilitlenme Arasındaki Zaman\",\n" +
                "            \"range_end\": 1000,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D22\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"D23\": {\n" +
                "            \"default_val\": 40,\n" +
                "            \"description_eng\": \"Starting Speed Of Door Closing\",\n" +
                "            \"description_tr\": \"Kapı Kapanışının Hızı\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D24\": {\n" +
                "            \"default_val\": 100,\n" +
                "            \"description_eng\": \"Maximum Closing Speed\",\n" +
                "            \"description_tr\": \"Maksimum Kapanış Hızı\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D25\": {\n" +
                "            \"default_val\": 20,\n" +
                "            \"description_eng\": \"Speed Of Door Closing In Place\",\n" +
                "            \"description_tr\": \"Kapının Kapanış Hızı\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D26\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Lock Door Speed\",\n" +
                "            \"description_tr\": \"Kilitli Kapı Hızı\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D27\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Hold Speed Of Closing Door In Place\",\n" +
                "            \"description_tr\": \"Kapının Kapanışının Tutma Hızı\",\n" +
                "            \"range_end\": 80,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D28\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Accelerate Of Closing\",\n" +
                "            \"description_tr\": \"Kapanışın Hızlanması\",\n" +
                "            \"range_end\": 40,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D29\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Decelerate Of Closing\",\n" +
                "            \"description_tr\": \"Kapanışın Yavaşlaması\",\n" +
                "            \"range_end\": 40,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D30\": {\n" +
                "            \"default_val\": 200,\n" +
                "            \"description_eng\": \"Starting Current  Of Closing\",\n" +
                "            \"description_tr\": \"Kapanışın Başlama Akımı\",\n" +
                "            \"range_end\": 300,\n" +
                "            \"range_start\": 5,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D31\": {\n" +
                "            \"default_val\": 200,\n" +
                "            \"description_eng\": \"Current Of Closing Door Anti Pinch\",\n" +
                "            \"description_tr\": \"Kapanışta Sıkışma Önleyici Akım\",\n" +
                "            \"range_end\": 300,\n" +
                "            \"range_start\": 5,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D32\": {\n" +
                "            \"default_val\": 70,\n" +
                "            \"description_eng\": \"Hold Current Of Closing Door In Place\",\n" +
                "            \"description_tr\": \"Kapı Kapanışının Tutma Frekansı\",\n" +
                "            \"range_end\": 300,\n" +
                "            \"range_start\": 5,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D33\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"D34\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"D35\": {\n" +
                "            \"default_val\": 0,\n" +
                "            \"description_eng\": \"Holding Time In Place\",\n" +
                "            \"description_tr\": \"Tutuş Süresi\",\n" +
                "            \"range_end\": 999,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D36\": {\n" +
                "            \"default_val\": 20,\n" +
                "            \"description_eng\": \"Starting Distance / Time\",\n" +
                "            \"description_tr\": \"Başlangıç Mesafesi / Zaman\",\n" +
                "            \"range_end\": 100,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D37\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"D38\": {\n" +
                "            \"default_val\": 30,\n" +
                "            \"description_eng\": \"Action Speed When Power On\",\n" +
                "            \"description_tr\": \"Güç Açıkken Harekete Geçme Hızı\",\n" +
                "            \"range_end\": 80,\n" +
                "            \"range_start\": 5,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D39\": {\n" +
                "            \"default_val\": 30,\n" +
                "            \"description_eng\": \"Gate Amplitude Measurement Speed\",\n" +
                "            \"description_tr\": \"Kapı Genişliği Ölçüm Hızı\",\n" +
                "            \"range_end\": 80,\n" +
                "            \"range_start\": 5,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"D40\": {\n" +
                "            \"default_val\": 4445,\n" +
                "            \"description_eng\": \"Gate Width Measurement\",\n" +
                "            \"description_tr\": \"Kapı Genişlik Ölçüsü\",\n" +
                "            \"range_end\": 9999,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"N\": {\n" +
                "        \"N01\": {\n" +
                "            \"description_eng\": \"Frequency / Speed\",\n" +
                "            \"description_tr\": \"Frekans / Hız\"\n" +
                "        },\n" +
                "        \"N02\": {\n" +
                "            \"description_eng\": \"Current\",\n" +
                "            \"description_tr\": \"Akım\"\n" +
                "        },\n" +
                "        \"N03\": {\n" +
                "            \"description_eng\": \"Voltage\",\n" +
                "            \"description_tr\": \"Voltaj\"\n" +
                "        },\n" +
                "        \"N04\": {\n" +
                "            \"description_eng\": \"Encoder Value\",\n" +
                "            \"description_tr\": \"Encoder Değeri\"\n" +
                "        },\n" +
                "        \"N05\": {\n" +
                "            \"description_eng\": \"Encoder Value Of Opening In Place\",\n" +
                "            \"description_tr\": \"Kapı Açılışının Encoder Değeri\"\n" +
                "        },\n" +
                "        \"N06\": {\n" +
                "            \"description_eng\": \"Encoder Value Of Closing In Place\",\n" +
                "            \"description_tr\": \"Kapı Kapanışının Encoder Değeri\"\n" +
                "        },\n" +
                "        \"N07\": {\n" +
                "            \"description_eng\": \"Times Of Door Opening And Closing\",\n" +
                "            \"description_tr\": \"Kapının Açılış Ve Kapanış Zamanları\"\n" +
                "        },\n" +
                "        \"N08\": {\n" +
                "            \"description_eng\": \"Door Position\",\n" +
                "            \"description_tr\": \"Kapı Pozisyonu\"\n" +
                "        },\n" +
                "        \"N09\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"N10\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"N11\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"N12\": {\n" +
                "            \"description_eng\": \"Error Code\",\n" +
                "            \"description_tr\": \"Hata Kodu\"\n" +
                "        },\n" +
                "        \"N13\": {\n" +
                "            \"description_eng\": \"Error Code\",\n" +
                "            \"description_tr\": \"Hata Kodu\"\n" +
                "        },\n" +
                "        \"N14\": {\n" +
                "            \"description_eng\": \"Error Code\",\n" +
                "            \"description_tr\": \"Hata Kodu\"\n" +
                "        },\n" +
                "        \"N15\": {\n" +
                "            \"description_eng\": \"Error Code\",\n" +
                "            \"description_tr\": \"Hata Kodu\"\n" +
                "        },\n" +
                "        \"N16\": {\n" +
                "            \"description_eng\": \"Input Status\",\n" +
                "            \"description_tr\": \"Girdi Durumu\"\n" +
                "        },\n" +
                "        \"N17\": {\n" +
                "            \"description_eng\": \"Output Status\",\n" +
                "            \"description_tr\": \"Çıktı Durumu\"\n" +
                "        },\n" +
                "        \"N18\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"N19\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"N20\": {\n" +
                "            \"description_eng\": \"Software Version\",\n" +
                "            \"description_tr\": \"Yazılım Versiyonu\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"P\": {\n" +
                "        \"P01\": {\n" +
                "            \"default_val\": 0,\n" +
                "            \"description_eng\": \"Work Mode\",\n" +
                "            \"description_tr\": \"Çalışma Modu\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Panel Inching Operation\",\n" +
                "                    \"description_tr\": \"Panel Adım Hareket Operasyonu\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Encoder External Contol Mode\",\n" +
                "                    \"description_tr\": \"Enkoder Harici Kontrol Modu\"\n" +
                "                },\n" +
                "                \"2\": {\n" +
                "                    \"description_eng\": \"Encoder Demo Mode\",\n" +
                "                    \"description_tr\": \"Enkoder Demo Modu\"\n" +
                "                },\n" +
                "                \"3\": {\n" +
                "                    \"description_eng\": \"Gate Width Measurement Control\",\n" +
                "                    \"description_tr\": \"Kapı Genişliği Ölçüm Modu\"\n" +
                "                },\n" +
                "                \"4\": {\n" +
                "                    \"description_eng\": \"Position Switch External Control Mode\",\n" +
                "                    \"description_tr\": \"Konum Değişimi Ölçüm Modu\"\n" +
                "                },\n" +
                "                \"5\": {\n" +
                "                    \"description_eng\": \"Position Switch Demo\",\n" +
                "                    \"description_tr\": \"Pozisyon Değişimi Demo Modu\"\n" +
                "                },\n" +
                "                \"6\": {\n" +
                "                    \"description_eng\": \"Initial Angle Measurement\",\n" +
                "                    \"description_tr\": \"İlk Açı Ölçümü\"\n" +
                "                },\n" +
                "                \"7\": {\n" +
                "                    \"description_eng\": \"Internal Debugging\",\n" +
                "                    \"description_tr\": \"Dahili Hata Ayıklama \"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 7,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P02\": {\n" +
                "            \"default_val\": 1,\n" +
                "            \"description_eng\": \"Motor Direction\",\n" +
                "            \"description_tr\": \"Motor Yönü\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Forward\",\n" +
                "                    \"description_tr\": \"İleri\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Reverse\",\n" +
                "                    \"description_tr\": \"Geri\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 1,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P03\": {\n" +
                "            \"default_val\": 1,\n" +
                "            \"description_eng\": \"Stop Mode\",\n" +
                "            \"description_tr\": \"Durdurma Modu\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Inertia Stop\",\n" +
                "                    \"description_tr\": \"Atalet Durdurumu\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Decelerate And Stop\",\n" +
                "                    \"description_tr\": \"Yavaşla Ve Dur\"\n" +
                "                },\n" +
                "                \"2\": {\n" +
                "                    \"description_eng\": \"Deceleration Brake Stop\",\n" +
                "                    \"description_tr\": \"Yavaşlama Freni Durdurma\"\n" +
                "                },\n" +
                "                \"3\": {\n" +
                "                    \"description_eng\": \"Quick Deceleration Stop\",\n" +
                "                    \"description_tr\": \"Hızlı Yavaşlama Durdurma\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 3,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P04\": {\n" +
                "            \"default_val\": 50,\n" +
                "            \"description_eng\": \"Judgement Time Of Door Closing Anti Pinch (Ws)\",\n" +
                "            \"description_tr\": \"Kapı Kapanırken Sıkışmayı Önleme Süresi\",\n" +
                "            \"range_end\": 500,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P05\": {\n" +
                "            \"default_val\": 50,\n" +
                "            \"description_eng\": \"Judgement Time Of Door Opening Anti Pinch (Ws)\",\n" +
                "            \"description_tr\": \"Kapı Açılırken Sıkışmayı Önleme Süresi\",\n" +
                "            \"range_end\": 500,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P06\": {\n" +
                "            \"default_val\": 0,\n" +
                "            \"description_eng\": \"Effective Mode Of Door Command Signal\",\n" +
                "            \"description_tr\": \"Kapı Komut Sinyalinin Etkili Modu\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Level Trigger Mode\",\n" +
                "                    \"description_tr\": \"Seviye Tetikleme Modu\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Pulse Trigger Mode\",\n" +
                "                    \"description_tr\": \"Atım Tetikleme Modu\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 1,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P07\": {\n" +
                "            \"default_val\": 27,\n" +
                "            \"description_eng\": \"Effective Level Of Input Signal\",\n" +
                "            \"description_tr\": \"Etkili Giriş Sinyali Seviyesi\",\n" +
                "            \"range_end\": 31,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P08\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Waiting Time For Demo Mode To Open\",\n" +
                "            \"description_tr\": \"Demo Modunun Açılması Için Bekleme Süresi\",\n" +
                "            \"range_end\": 30,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P09\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Waiting Time For Demo Code To Close\",\n" +
                "            \"description_tr\": \"Demo Modunun Kapanması Için Bekleme Süresi\",\n" +
                "            \"range_end\": 30,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P10\": {\n" +
                "            \"default_val\": 1,\n" +
                "            \"description_eng\": \"Acceleration And Deceleration Control Mode Selection\",\n" +
                "            \"description_tr\": \"Hızlanma Ve Yavaşlama Kontrol Modu Seçimi\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Conic\",\n" +
                "                    \"description_tr\": \"Konik\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Quadratic Curve, Namely S Curve\",\n" +
                "                    \"description_tr\": \"İkinci Dereceden Eğri, Yani S Eğrisi\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 1,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P11\": {\n" +
                "            \"default_val\": 0,\n" +
                "            \"description_eng\": \"Encoder Mode Closing In Place Signal Setting\",\n" +
                "            \"description_tr\": \"Enkoder Modun Sinyale Göre Kapanması\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"According To The Position Of Encoder, The Door Is Closed In Place\",\n" +
                "                    \"description_tr\": \"Encoderın Pozisyonuna Göre Kap Kapanışı\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"According To The Position Switch To Judge The Close Position Output\",\n" +
                "                    \"description_tr\": \"Yakın Konum Çıkışını Yargılamak Için Konum Anahtarına Göre\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 1,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P12\": {\n" +
                "            \"default_val\": 1,\n" +
                "            \"description_eng\": \"Lower Limit Speed\",\n" +
                "            \"description_tr\": \"Alt Hız Sınırı\",\n" +
                "            \"range_end\": 99,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P13\": {\n" +
                "            \"default_val\": 200,\n" +
                "            \"description_eng\": \"Upper Limit Speed\",\n" +
                "            \"description_tr\": \"Üst Hız Sınırı\",\n" +
                "            \"range_end\": 500,\n" +
                "            \"range_start\": 100,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P14\": {\n" +
                "            \"default_val\": 4,\n" +
                "            \"description_eng\": \"Safety Signal Filtering Time (Ms)\",\n" +
                "            \"description_tr\": \"Güvenlik Sinyali Filtreleme Süresi\",\n" +
                "            \"range_end\": 40,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P15\": {\n" +
                "            \"default_val\": 4,\n" +
                "            \"description_eng\": \"Filtering Time Of Arrival Signal (Ms)\",\n" +
                "            \"description_tr\": \"Varış Sinyal Sinyalinin Filtreleme Süresi\",\n" +
                "            \"range_end\": 40,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P16\": {\n" +
                "            \"default_val\": 7,\n" +
                "            \"description_eng\": \"Relay Ry3 Function Selection\",\n" +
                "            \"description_tr\": \"Ry3 Rölesinin İşlev Seçimi\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Door Opening Process Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Açılış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Door Closing Process Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Kapanış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"2\": {\n" +
                "                    \"description_eng\": \"Invalid\",\n" +
                "                    \"description_tr\": \"Geçersiz\"\n" +
                "                },\n" +
                "                \"3\": {\n" +
                "                    \"description_eng\": \"Invalid\",\n" +
                "                    \"description_tr\": \"Geçersiz\"\n" +
                "                },\n" +
                "                \"4\": {\n" +
                "                    \"description_eng\": \"Door Opening In Place Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Açılış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"5\": {\n" +
                "                    \"description_eng\": \"Door Closing In Place Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Kapnış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"6\": {\n" +
                "                    \"description_eng\": \"Clip In Detection Indicator / Output\",\n" +
                "                    \"description_tr\": \"Algılama Göstergesinde / Çıkışında Klips\"\n" +
                "                },\n" +
                "                \"7\": {\n" +
                "                    \"description_eng\": \"Exception Indication / Output\",\n" +
                "                    \"description_tr\": \"İstisna Göstergesi/Çıktısı\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 7,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P17\": {\n" +
                "            \"default_val\": 4,\n" +
                "            \"description_eng\": \"Relay Ry2 Function Selection\",\n" +
                "            \"description_tr\": \"Ry2 Rölesinin İşlev Seçimi\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Door Opening Process Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Açılış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Door Closing Process Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Kapanış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"2\": {\n" +
                "                    \"description_eng\": \"Invalid\",\n" +
                "                    \"description_tr\": \"Geçersiz\"\n" +
                "                },\n" +
                "                \"3\": {\n" +
                "                    \"description_eng\": \"Invalid\",\n" +
                "                    \"description_tr\": \"Geçersiz\"\n" +
                "                },\n" +
                "                \"4\": {\n" +
                "                    \"description_eng\": \"Door Opening In Place Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Açılış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"5\": {\n" +
                "                    \"description_eng\": \"Door Closing In Place Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Kapnış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"6\": {\n" +
                "                    \"description_eng\": \"Clip In Detection Indicator / Output\",\n" +
                "                    \"description_tr\": \"Algılama Göstergesinde / Çıkışında Klips\"\n" +
                "                },\n" +
                "                \"7\": {\n" +
                "                    \"description_eng\": \"Exception Indication / Output\",\n" +
                "                    \"description_tr\": \"İstisna Göstergesi/Çıktısı\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 7,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P18\": {\n" +
                "            \"default_val\": 5,\n" +
                "            \"description_eng\": \"Relay Ry1 Function Selection\",\n" +
                "            \"description_tr\": \"Ry1 Rölesinin İşlev Seçimi\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Door Opening Process Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Açılış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Door Closing Process Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Kapanış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"2\": {\n" +
                "                    \"description_eng\": \"Invalid\",\n" +
                "                    \"description_tr\": \"Geçersiz\"\n" +
                "                },\n" +
                "                \"3\": {\n" +
                "                    \"description_eng\": \"Invalid\",\n" +
                "                    \"description_tr\": \"Geçersiz\"\n" +
                "                },\n" +
                "                \"4\": {\n" +
                "                    \"description_eng\": \"Door Opening In Place Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Açılış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"5\": {\n" +
                "                    \"description_eng\": \"Door Closing In Place Indication / Output\",\n" +
                "                    \"description_tr\": \"Kapı Kapnış Göstergesi/Çıktısı\"\n" +
                "                },\n" +
                "                \"6\": {\n" +
                "                    \"description_eng\": \"Clip In Detection Indicator / Output\",\n" +
                "                    \"description_tr\": \"Algılama Göstergesinde / Çıkışında Klips\"\n" +
                "                },\n" +
                "                \"7\": {\n" +
                "                    \"description_eng\": \"Exception Indication / Output\",\n" +
                "                    \"description_tr\": \"İstisna Göstergesi/Çıktısı\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 7,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P19\": {\n" +
                "            \"default_val\": 3,\n" +
                "            \"description_eng\": \"Restore Factory Parameters\",\n" +
                "            \"description_tr\": \"Fabrika Parametrelerini Geri Yükle\",\n" +
                "            \"enums\": {\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Restore The Position Switch Mode Of Asynchronous Motor\",\n" +
                "                    \"description_tr\": \"Asenkron Motorun Konum Değiştirme Modunu Geri Yüklemek\"\n" +
                "                },\n" +
                "                \"2\": {\n" +
                "                    \"description_eng\": \"Restore Asynchronous Motor Encoder Mode\",\n" +
                "                    \"description_tr\": \"Asenkron Motor Encoder Modunu Geri Yükleme\"\n" +
                "                },\n" +
                "                \"3\": {\n" +
                "                    \"description_eng\": \"Restore The Synchronous Motor To Factor\",\n" +
                "                    \"description_tr\": \"Senkron Motoru Faktöre Geri Yükleme\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 3,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P20\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"P30\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"P31\": {\n" +
                "            \"default_val\": 0,\n" +
                "            \"description_eng\": \"Motor Type\",\n" +
                "            \"description_tr\": \"Motor Türü\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Asynchronous Motor\",\n" +
                "                    \"description_tr\": \"Asenkron Motor\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Synchronous Motor\",\n" +
                "                    \"description_tr\": \"Senkron Motor\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 1,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P32\": {\n" +
                "            \"default_val\": 8,\n" +
                "            \"description_eng\": \"Motor Poles\",\n" +
                "            \"description_tr\": \"Motor Kutpu\",\n" +
                "            \"range_end\": 256,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P33\": {\n" +
                "            \"default_val\": 1024,\n" +
                "            \"description_eng\": \"Encoder Cpr\",\n" +
                "            \"description_tr\": \"Enkoder Cpr\",\n" +
                "            \"range_end\": 4096,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P34\": {\n" +
                "            \"default_val\": 15,\n" +
                "            \"description_eng\": \"Carrier Frequency\",\n" +
                "            \"description_tr\": \"Taşıyıcı Frekansı\",\n" +
                "            \"range_end\": 20,\n" +
                "            \"range_start\": 8,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P35\": {\n" +
                "            \"default_val\": 40,\n" +
                "            \"description_eng\": \"Diameter Of Synchronous Wheel\",\n" +
                "            \"description_tr\": \"Senkron Tekerleğin Çapı\",\n" +
                "            \"range_end\": 200,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P36\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"P39\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"P40\": {\n" +
                "            \"default_val\": 50,\n" +
                "            \"description_eng\": \"V/F\",\n" +
                "            \"description_tr\": \"V/F\",\n" +
                "            \"range_end\": 60,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P41\": {\n" +
                "            \"default_val\": 100,\n" +
                "            \"description_eng\": \"Maximum Output Frequency Of Motor\",\n" +
                "            \"description_tr\": \"Motorun Maksimum Çıkış Frekansı\",\n" +
                "            \"range_end\": 100,\n" +
                "            \"range_start\": 50,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P42\": {\n" +
                "            \"default_val\": 20,\n" +
                "            \"description_eng\": \"Base Frequency\",\n" +
                "            \"description_tr\": \"Baz Frekansı\",\n" +
                "            \"range_end\": 400,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P43\": {\n" +
                "            \"default_val\": 98,\n" +
                "            \"description_eng\": \"V / F Voltage Maximum\",\n" +
                "            \"description_tr\": \"Maksimum V/F Voltajı\",\n" +
                "            \"range_end\": 100,\n" +
                "            \"range_start\": 51,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P44\": {\n" +
                "            \"default_val\": 15,\n" +
                "            \"description_eng\": \"V / F Voltage Minimum\",\n" +
                "            \"description_tr\": \"Minimum V/F Voltajı\",\n" +
                "            \"range_end\": 50,\n" +
                "            \"range_start\": 10,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P45\": {\n" +
                "            \"default_val\": 5,\n" +
                "            \"description_eng\": \"Detection Frequency Of High Speed Locked Rotor Slip Of Asynchronous Motor (%)\",\n" +
                "            \"description_tr\": \"Asenkron Motorun Yüksek Hızlı Kilitli Rotor Kaymasının Algılama Frekansı (%)\",\n" +
                "            \"range_end\": 10,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P46\": {\n" +
                "            \"default_val\": 5,\n" +
                "            \"description_eng\": \"Detection Frequency Of Low Speed Locked Rotor Slip Of Asynchronous Motor (%)\",\n" +
                "            \"description_tr\": \"Asenkron Motorun Düşük Hızlı Kilitli Rotor Kaymasının Algılama Frekansı (%)\",\n" +
                "            \"range_end\": 10,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P47\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"P50\": {\n" +
                "            \"description_eng\": \"Backup\",\n" +
                "            \"description_tr\": \"Yedek\"\n" +
                "        },\n" +
                "        \"P51\": {\n" +
                "            \"default_val\": 200,\n" +
                "            \"description_eng\": \"Open Speed P\",\n" +
                "            \"description_tr\": \"Açılma Hızı P\",\n" +
                "            \"range_end\": 9999,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P52\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Open Speed I\",\n" +
                "            \"description_tr\": \"Açılma Hızı I\",\n" +
                "            \"range_end\": 9999,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P53\": {\n" +
                "            \"default_val\": 200,\n" +
                "            \"description_eng\": \"Close Speed P\",\n" +
                "            \"description_tr\": \"Kapanma Hızı P\",\n" +
                "            \"range_end\": 9999,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P54\": {\n" +
                "            \"default_val\": 10,\n" +
                "            \"description_eng\": \"Close Speed I\",\n" +
                "            \"description_tr\": \"Kapanma Hızı I\",\n" +
                "            \"range_end\": 9999,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P55\": {\n" +
                "            \"default_val\": 250,\n" +
                "            \"description_eng\": \"Current P\",\n" +
                "            \"description_tr\": \"Akım P\",\n" +
                "            \"range_end\": 9999,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P56\": {\n" +
                "            \"default_val\": 30,\n" +
                "            \"description_eng\": \"Current I\",\n" +
                "            \"description_tr\": \"Akım I\",\n" +
                "            \"range_end\": 9999,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P57\": {\n" +
                "            \"default_val\": 5,\n" +
                "            \"description_eng\": \"Open Door Overload Detection Speed\",\n" +
                "            \"description_tr\": \"Açık Kapı Aşırı Yük Algılama Hızı\",\n" +
                "            \"range_end\": 40,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P58\": {\n" +
                "            \"default_val\": 5,\n" +
                "            \"description_eng\": \"Closing Overload Detection Speed\",\n" +
                "            \"description_tr\": \"Kapanışın Aşırı Yük Algılama Hızı\",\n" +
                "            \"range_end\": 40,\n" +
                "            \"range_start\": 1,\n" +
                "            \"type\": \"Bar\"\n" +
                "        },\n" +
                "        \"P59\": {\n" +
                "            \"default_val\": 0,\n" +
                "            \"description_eng\": \"Selection Of Open And Closed Loop Operation Of Synchronous Motor\",\n" +
                "            \"description_tr\": \"Senkron Motorun Açık Ve Kapalı Çevrim Çalışmasının Seçimi\",\n" +
                "            \"enums\": {\n" +
                "                \"0\": {\n" +
                "                    \"description_eng\": \"Closed Loop\",\n" +
                "                    \"description_tr\": \"Kapalı Döngü\"\n" +
                "                },\n" +
                "                \"1\": {\n" +
                "                    \"description_eng\": \"Open Loop\",\n" +
                "                    \"description_tr\": \"Açık Döngü\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"range_end\": 1,\n" +
                "            \"range_start\": 0,\n" +
                "            \"type\": \"enums\"\n" +
                "        },\n" +
                "        \"P60\": {\n" +
                "            \"description_eng\": \"Initial Angle Of Motor\",\n" +
                "            \"description_tr\": \"Motorun Başlangıç Açısı\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        ParameterObject parameterObject = gson.fromJson(json, ParameterObject.class);
        System.out.println("sa");
        return parameterObject;
    }
}