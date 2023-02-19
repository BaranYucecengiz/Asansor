package com.by.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Activity_Splash_Login extends AppCompatActivity{
    // OBJECTS
    private Button login_bt;
    private EditText user_id;
    private TextView wrong_id_pass;
    private EditText user_password;
    private Users users;
    private String id, password;
    // PHPMYADMIN CONNECTION

    private static final String url = "jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12598266";
    private static final String user = "sql12598266";
    private static final String pass = "vDZ9DK9rxI";

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
            public void onClick(View view){
                new MyTask().execute();
            }
        });

    }

    private class MyTask extends AsyncTask<Void, Void, Void>{


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
                String loginCheckCommand = "select * from User where id='"
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

                    id = String.valueOf(rs.getInt(1));
                    password = rs.getString(4);
                    users = Users.getInstance();
                    users.setUser_id(id);
                    users.setUser_password(password);
                    createDb_and_Elevators(id);

                    System.out.println("sa");
                    //addUsers();
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

    }
    /*public void addUsers(){
        // Json file dan çekilecek şuan manuel
        Users users = Users.getInstance();
        users.addElevator(new Elevator("1", "A. Cad"));
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
        users.user_elevators.get(0).parameters.get(2).add_parameters_name("Inertia stop");
        users.user_elevators.get(0).parameters.get(2).add_parameters_name("Decelerate and stop");
        users.user_elevators.get(0).parameters.get(2).add_parameters_name("Deceleration brake stop");
        users.user_elevators.get(0).add_Parameters(new Parameters("D", "D01", "Kapı açılışındaki yavaşlama uzaklığı","bar", 120, 10, 300));
        users.user_elevators.get(0).add_Parameters(new Parameters("D", "D02", "Kapı açılışının başlangıç hızı","bar", 40, 1, 400));
        users.user_elevators.get(0).add_Parameters(new Parameters("N", "N01", "Frekans / Hız","", 0, 0, 0));


        users.addElevator(new Elevator("2", "B. Cad"));
        users.user_elevators.get(1).add_Parameters(new Parameters("P", "P01", "Çalışma Modu","enum", 0, 0, 7));
        users.user_elevators.get(1).parameters.get(0).add_parameters_name("Panel Adım Harekete Operasyonu");
        users.user_elevators.get(1).parameters.get(0).add_parameters_name("Enkoder Harici Kontrol Modu");
        users.user_elevators.get(1).parameters.get(0).add_parameters_name("Enkoder Demo Modu");
        users.user_elevators.get(1).add_Parameters(new Parameters("P", "P02", "Motor Yönü","enum", 1, 0, 1));
        users.user_elevators.get(1).parameters.get(1).add_parameters_name("İleri");
        users.user_elevators.get(1).parameters.get(1).add_parameters_name("Geri");

        users.user_elevators.get(1).add_Parameters(new Parameters("D", "D01", "Kapı açılışındaki yavaşlama uzaklığı","bar", 120, 10, 300));
        users.user_elevators.get(1).add_Parameters(new Parameters("D", "D02", "Kapı açılışının başlangıç hızı","bar", 40, 1, 400));

    }*/
    public void createDb_and_Elevators(String owner_id) throws SQLException, ClassNotFoundException {
        Users user = Users.getInstance();
        DB db = new DB();
        List<String> responsibleElevatorIds;
        responsibleElevatorIds = db.getResponsibleElevatorIds(owner_id);

        for(String id: responsibleElevatorIds){
            System.out.println(id);
        }
        Map<String, Elevator> elevators = db.getAllResponsibleElevators(responsibleElevatorIds, false);
        user.setUser_elevators(new ArrayList<Elevator>(elevators.values()));
    }

}