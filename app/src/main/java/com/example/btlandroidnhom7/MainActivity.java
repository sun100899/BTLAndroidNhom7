package com.example.btlandroidnhom7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TABLE_ACCS = "tableAccs";
    public static final String USERNAME = "userName";
    public static Boolean flag = false;

    EditText editUserName;
    Button btnCreate, btnDanhSach;
    TextView textView;

    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUserName = findViewById(R.id.hoTen);
        btnCreate = findViewById(R.id.btnCreate);
        btnDanhSach = findViewById(R.id.btnDanhSach);
        textView = findViewById(R.id.tvXinChao);

        loadUser();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        btnDanhSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DanhSachSP.class);
                startActivity(intent);
            }
        });
    }


    public void loadUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(TABLE_ACCS, MODE_PRIVATE);
        userName = sharedPreferences.getString(USERNAME, "");
        if(userName!= ""){
           textView.setText("Xin chào: "+userName);
        }
        else {
            textView.setText("Chưa có user nào!!Vui lòng tạo user");
        }
    }



    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(TABLE_ACCS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, editUserName.getText().toString());

        editor.apply();
        String name = sharedPreferences.getString(USERNAME, "");
        textView.setText("Xin chào: "+ name);
        Toast.makeText(this, "Đã tạo người dùng", Toast.LENGTH_LONG).show();
    }
}