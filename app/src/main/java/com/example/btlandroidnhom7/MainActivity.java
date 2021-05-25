package com.example.btlandroidnhom7;

import androidx.appcompat.app.AppCompatActivity;

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
    Button btnCreate;
    TextView textView;

    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUserName = findViewById(R.id.hoTen);
        btnCreate = findViewById(R.id.btnCreate);
        textView = findViewById(R.id.tvXinChao);

        loadUser();
        updateDataUser();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }


    public void loadUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(TABLE_ACCS, MODE_PRIVATE);
        userName = sharedPreferences.getString(USERNAME, "");
        if(userName!= ""){
            flag = true;
        }
    }

    public void updateDataUser(){
//        if(flag==true){
//            textView.setText("Xin chào: "+userName);
//        }
//        else {
//            textView.setText("chua co du lieu");
//        }
        textView.setText(userName);
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(TABLE_ACCS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, editUserName.getText().toString());

        editor.apply();
        Toast.makeText(this, "Đã tạo người dùng", Toast.LENGTH_LONG).show();
    }
}