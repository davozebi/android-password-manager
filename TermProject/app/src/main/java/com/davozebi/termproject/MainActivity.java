package com.davozebi.termproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;

    EditText etEmail;
    TextInputEditText etPass;

    @Override
    protected void onResume() {
        super.onResume();
        etEmail.getText().clear();
        etPass.getText().clear();
        etEmail.clearFocus();
        etPass.clearFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.activity_main_btn_login);
        btnRegister = findViewById(R.id.activity_main_btn_register);

        etEmail = findViewById(R.id.activity_main_et_email);
        etPass = findViewById(R.id.activity_main_et_pass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        // For Night mode later
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    private void login() {
        if (etEmail.getText().toString().isEmpty()|| etPass.getText().toString().isEmpty()) {
            Toast.makeText(
                    MainActivity.this,
                    "Error: empty field(s)",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            Toast.makeText(
                    MainActivity.this,
                    "Error: invalid email",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        UserModel userModel;
        try {
            userModel = new UserModel(
                    etEmail.getText().toString(),
                    etPass.getText().toString()
            );
        }
        catch (Exception e) {
            Toast.makeText(
                    MainActivity.this,
                    "Error: account could not be created",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        DbHelper dbHelper = new DbHelper(MainActivity.this);
        boolean success = dbHelper.userModelExists(userModel);
        dbHelper.close();
        if (!success) {
            Toast.makeText(
                    MainActivity.this,
                    "Error: account does not exist",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Intent i = new Intent(this, ManagerActivity.class);
        i.putExtra("userModel", userModel);

        startActivity(i);
    }
}