package com.by.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.ResultSet;


public class Activity_Splash_Login extends AppCompatActivity {
    // OBJECTS
    private Button login_bt;
    private EditText user_id;
    private TextView wrong_id_pass;
    private EditText user_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        user_id = (EditText) findViewById(R.id.user_id);
        user_password = (EditText) findViewById(R.id.user_password);
        login_bt = (Button) findViewById(R.id.login_bt);
        wrong_id_pass = (TextView) findViewById(R.id.wrong_id_pass);
        login_bt.setOnClickListener(new View.OnClickListener() {
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
                ResultSet resultSet = db.isAvailable(user_id.getText().toString(), user_password.getText().toString());
                if (resultSet.next()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wrong_id_pass.setText("Giriş Başarılı");
                            wrong_id_pass.setVisibility(View.VISIBLE);
                        }
                    });
                    db.setUserInformation(db, resultSet);

                    Intent intent = new Intent(Activity_Splash_Login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wrong_id_pass.setText("Kullanıcı adı veya şifre hatalı");
                            wrong_id_pass.setVisibility(View.VISIBLE);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}