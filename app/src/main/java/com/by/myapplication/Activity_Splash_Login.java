package com.by.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Activity_Splash_Login extends AppCompatActivity {
    // OBJECTS
    private Button login_bt;
    private EditText user_id;
    private TextView wrong_id_pass;
    private EditText user_password;

    // PHPMYADMIN CONNECTION

    private static final String url = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12598266";
    private static final String user = "sql12598266";
    private static final String pass = "vDZ9DK9rxI";
    private EditText emailText, passText;

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
                new MyTask().execute();
                }
        });

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        private String email = "", password = "";

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);

                if (con.isValid(5)){
                    System.out.println("Database e bağlanıldı");
                }else{
                    System.out.println("Database e bağlanılmadı");
                }


                Statement st = con.createStatement();
                String loginCheckCommand = "select * from User where name='"
                        + user_id.getText() + "' and password='" + user_password.getText() +"'";
                final java.sql.ResultSet rs = st.executeQuery(loginCheckCommand);
                if (rs.next()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wrong_id_pass.setText("Giriş Başarılı");
                            wrong_id_pass.setVisibility(View.VISIBLE);
                        }
                    });

                    email = String.valueOf(rs.getInt(1));
                    password = rs.getString(2);

                    Intent intent = new Intent(Activity_Splash_Login.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wrong_id_pass.setText("Kullanıcı adı veya şifre hatalı");
                            wrong_id_pass.setVisibility(View.VISIBLE);
                        }
                    });

                }

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //emailText.setText(email);
            //passText.setText(password);
            System.out.println(email + " | " + password);
            super.onPostExecute(unused);
        }
    }

}