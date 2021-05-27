package com.example.btlandroidnhom7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddSanPham extends AppCompatActivity {

    Button btnAdd, btnRedo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_san_pham);


        btnAdd = findViewById(R.id.btnAdd);
        btnRedo = findViewById(R.id.btnRedo);

        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSanPham.this, DanhSachSP.class);
                startActivity(intent);
            }
        });
    }
}