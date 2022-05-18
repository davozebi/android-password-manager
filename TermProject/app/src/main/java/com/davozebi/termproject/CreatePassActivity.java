package com.davozebi.termproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class CreatePassActivity extends AppCompatActivity {

    Button btnRandomPass;
    Button btnCreatePass;
    Button btnCancel;

    EditText etService;
    EditText etUserName;
    TextInputEditText etPass;
    TextInputEditText etConfirmPass;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass);

        userModel = (UserModel) getIntent().getSerializableExtra("userModel");

        btnRandomPass = findViewById(R.id.activity_create_pass_btn_random_pass);
        btnCreatePass = findViewById(R.id.activity_create_pass_btn_create_pass);
        btnCancel = findViewById(R.id.activity_create_pass_btn_cancel);

        etService = findViewById(R.id.activity_create_pass_et_service);
        etUserName = findViewById(R.id.activity_create_pass_et_user_name);
        etPass = findViewById(R.id.activity_create_pass_et_pass);
        etConfirmPass = findViewById(R.id.activity_create_pass_et_confirm_pass);

        btnRandomPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomPass(14);
            }
        });

        btnCreatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPass();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
    
    private void randomPass(int n) {
        final String SALT_CHARS = "0123456789"
                +"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                +"abcdefghijklmnopqrstuvwxyz";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; ++i) {
            int index = (int) (SALT_CHARS.length() * Math.random());
            sb.append(SALT_CHARS.charAt(index));
        }

        String pass = sb.toString();

        Toast.makeText(this, "Random password generated", Toast.LENGTH_SHORT).show();

        etPass.setText(pass);
        etConfirmPass.setText(pass);
    }

    private void createPass() {
        if (etService.getText().toString().isEmpty() || etUserName.getText().toString().isEmpty()
                || etPass.getText().toString().isEmpty() 
                || etConfirmPass.getText().toString().isEmpty()) {
            Toast.makeText(
                    CreatePassActivity.this,
                    "Error: empty field(s)",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (!etPass.getText().toString().equals(etConfirmPass.getText().toString())) {
            Toast.makeText(
                    CreatePassActivity.this,
                    "Error: non-matching passwords",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        PassModel passModel;
        try {
            passModel = new PassModel(
                    userModel.getEmail(),
                    etService.getText().toString(),
                    etUserName.getText().toString(),
                    etPass.getText().toString()
            );
        }
        catch (Exception e) {
            Toast.makeText(
                    CreatePassActivity.this,
                    "Error: password could not be created",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        DbHelper dbHelper = new DbHelper(CreatePassActivity.this);
        if (dbHelper.passModelExists(passModel)) {
            Toast.makeText(
                    CreatePassActivity.this,
                    "Error: entry already exists",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        boolean success = dbHelper.insertPassModel(passModel, userModel);
        dbHelper.close();
        if (!success) {
            Toast.makeText(
                    CreatePassActivity.this,
                    "Error: entry already exists",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("passModel", passModel);
        setResult(Activity.RESULT_OK, returnIntent);

        Toast.makeText(
                CreatePassActivity.this,
                "Password created",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}