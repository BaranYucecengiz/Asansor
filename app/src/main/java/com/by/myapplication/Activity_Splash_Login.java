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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Activity_Splash_Login extends AppCompatActivity{
    // OBJECTS
    private Button login_bt;
    private EditText user_id;
    private TextView wrong_id_pass;
    private EditText user_password;
    private Users users;

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

    private class MyTask extends AsyncTask<Void, Void, Void>{
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

                    users = addUsers(users);
                    Intent intent = new Intent(Activity_Splash_Login.this, MainActivity.class);
                    intent.putExtra("users", users);
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
    public Users addUsers(Users users){
        // Json file dan çekilecek şuan manuel
        users = new Users("15");
        users.addElevator(new Elevators("1", "A. Cad"));
        users.user_elevators.get(0).add_Parameters(new Parameters("P", "P01", "Çalışma Modu","enum", 0, 0, 7));
        users.user_elevators.get(0).parameters.get(0).add_parameters_name("Panel Adım Harekete Operasyonu");
        users.user_elevators.get(0).parameters.get(0).add_parameters_name("Enkoder Harici Kontrol Modu");
        users.user_elevators.get(0).parameters.get(0).add_parameters_name("Enkoder Demo Modu");
        users.user_elevators.get(0).parameters.get(0).add_parameters_name("Kapı Genişliği Ölçüm Modu");
        users.user_elevators.get(0).parameters.get(0).add_parameters_name("Konum Değişimi Ölçüm Modu");
        users.user_elevators.get(0).parameters.get(0).add_parameters_name("Pozisyon Değişimi Demo Modu");
        users.user_elevators.get(0).parameters.get(0).add_parameters_name("İlk Açı Ölçümü");
        users.user_elevators.get(0).parameters.get(0).add_parameters_name("Dahili Hata Ayıklama ");
        users.user_elevators.get(0).add_Parameters(new Parameters("P", "P02", "Motor Yönü","enum", 1, 0, 1));
        users.user_elevators.get(0).parameters.get(1).add_parameters_name("İleri");
        users.user_elevators.get(0).parameters.get(1).add_parameters_name("Geri");
        users.user_elevators.get(0).add_Parameters(new Parameters("P", "P03", "Durdurma Modu","enum", 1, 0, 1));
        users.user_elevators.get(0).parameters.get(1).add_parameters_name("Inertia stop");
        users.user_elevators.get(0).parameters.get(1).add_parameters_name("Decelerate and stop");
        users.user_elevators.get(0).parameters.get(1).add_parameters_name("Deceleration brake stop");
        users.user_elevators.get(0).add_Parameters(new Parameters("D", "D01", "Kapı açılışındaki yavaşlama uzaklığı","bar", 120, 10, 300));
        users.user_elevators.get(0).add_Parameters(new Parameters("D", "D01", "Kapı açılışının başlangıç hızı","bar", 40, 1, 400));
        users.user_elevators.get(0).add_Parameters(new Parameters("N", "N01", "Frekans / Hız","", 0, 0, 0));


        users.addElevator(new Elevators("2", "B. Cad"));
        users.user_elevators.get(1).add_Parameters(new Parameters("P", "P01", "Çalışma Modu","enum", 0, 0, 7));
        users.user_elevators.get(1).parameters.get(0).add_parameters_name("Panel Adım Harekete Operasyonu");
        users.user_elevators.get(1).parameters.get(0).add_parameters_name("Enkoder Harici Kontrol Modu");
        users.user_elevators.get(1).parameters.get(0).add_parameters_name("Enkoder Demo Modu");

        users.user_elevators.get(1).add_Parameters(new Parameters("P", "P02", "Motor Yönü","enum", 1, 0, 1));
        users.user_elevators.get(1).parameters.get(1).add_parameters_name("İleri");
        users.user_elevators.get(1).parameters.get(1).add_parameters_name("Geri");

        users.user_elevators.get(1).add_Parameters(new Parameters("D", "D01", "Kapı açılışındaki yavaşlama uzaklığı","bar", 120, 10, 300));
        users.user_elevators.get(1).add_Parameters(new Parameters("D", "D01", "Kapı açılışının başlangıç hızı","bar", 40, 1, 400));
        users.user_elevators.get(1).add_Parameters(new Parameters("N", "N01", "Frekans / Hız","", 0, 0, 0));

        return users;
    }
}