package com.example.btlandroidnhom7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.btlandroidnhom7.MainActivity.TABLE_ACCS;
import static com.example.btlandroidnhom7.MainActivity.USERNAME;

public class DanhSachSP extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner listDanhSach;
    private TextView tvXinChao;
    private RecyclerView listSanPham;
    private Button btnAddSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_s_p);

        listDanhSach = findViewById(R.id.spinner);
        tvXinChao = findViewById(R.id.tvXinChaoDSSP);
        listSanPham = findViewById(R.id.listSP);
        btnAddSP = findViewById(R.id.btnAddSP);

        SharedPreferences sharedPreferences = getSharedPreferences(TABLE_ACCS, MODE_PRIVATE);
        String userName = sharedPreferences.getString(USERNAME, "");

        addItemOnSpinner();

        btnAddSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachSP.this, AddSanPham.class);
                startActivity(intent);
            }
        });
    }

    private void addItemOnSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Tất cả");
        list.add("Điện thoại");
        list.add("Máy tính");
        list.add("Phụ kiện");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        listDanhSach.setAdapter(dataAdapter);
        listDanhSach.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                System.out.println("Tất cả");
                break;
            case 1:
                System.out.println("Điện thoại");
                break;
            case 2:
                System.out.println("Máy tính");
                break;
            case 3:
                System.out.println("Phụ kiện");
                break;
            case 4:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}