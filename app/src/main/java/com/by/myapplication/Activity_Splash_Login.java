package com.by.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_Splash_Login extends AppCompatActivity {
    // OBJECTS
    private Button login_bt;
    private EditText user_id;
    private TextView wrong_id_pass;
    private EditText user_password;

    //DUMMY
    private String dummy_id = "meftun";
    private String dummy_sifre = "q";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        user_id = (EditText)findViewById(R.id.user_id);
        user_password = (EditText)findViewById(R.id.user_password);
        login_bt = (Button)findViewById(R.id.login_bt);
        wrong_id_pass = (TextView)findViewById(R.id.wrong_id_pass);

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user_id.getText().toString().equals(dummy_id) && user_password.getText().toString().equals(dummy_sifre)) {
                    wrong_id_pass.setVisibility(View.VISIBLE);
                    wrong_id_pass.setText("SUCCESSFUL");

                    Intent intent = new Intent(Activity_Splash_Login.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    wrong_id_pass.setVisibility(View.VISIBLE);
                    wrong_id_pass.setText("Wrong Password or ID");
                }
            }
        });


    }
}