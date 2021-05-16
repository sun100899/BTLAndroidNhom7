package com.example.btlandroidnhom7.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btlandroidnhom7.MainActivity;
import com.example.btlandroidnhom7.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout edtName, edtEmail, edtPasswd;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsers;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        mAuth = FirebaseAuth.getInstance();
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });

    }

    private void registerNewUser() {
        final String name = edtName.getEditText().getText().toString().trim();
        String email = edtEmail.getEditText().getText().toString().trim();
        String passwd = edtPasswd.getEditText().getText().toString().trim();
        if(name.isEmpty()||email.isEmpty()||passwd.isEmpty()) {
            Snackbar.make(btnRegister, "Please complete all information!", Snackbar.LENGTH_LONG).show();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Starting sign up...");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, passwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mUsers.child(mAuth.getCurrentUser().getUid()).child("Name").setValue(name)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                if(task.isSuccessful()) {
                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Snackbar.make(btnRegister, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(btnRegister, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    private void initView() {
        edtName = findViewById(R.id.input_reg_name);
        edtEmail = findViewById(R.id.input_reg_email);
        edtPasswd = findViewById(R.id.input_reg_pass);
        btnRegister = findViewById(R.id.btn_reg);
    }
}
