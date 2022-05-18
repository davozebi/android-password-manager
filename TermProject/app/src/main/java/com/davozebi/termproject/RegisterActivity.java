package com.davozebi.termproject;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    Button btnCreateAccount;
    Button btnCancel;

    EditText etEmail;
    TextInputEditText etPass;
    TextInputEditText etConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnCreateAccount = findViewById(R.id.activity_register_btn_create_account);
        btnCancel = findViewById(R.id.activity_register_btn_cancel);

        etEmail = findViewById(R.id.activity_register_et_email);
        etPass = findViewById(R.id.activity_register_et_pass);
        etConfirmPass = findViewById(R.id.activity_register_et_confirm_pass);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createAccount() {
        if (etEmail.getText().toString().isEmpty() || etPass.getText().toString().isEmpty()
                || etConfirmPass.getText().toString().isEmpty()) {
            Toast.makeText(
                    RegisterActivity.this,
                    "Error: empty field(s)",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            Toast.makeText(
                    RegisterActivity.this,
                    "Error: invalid email",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

//        if (etPass.getText().toString().length() < 8) {
//            Toast.makeText(
//                    RegisterActivity.this,
//                    "Error: password must be at least 8 characters",
//                    Toast.LENGTH_SHORT
//            ).show();
//            return;
//        }

        if (!etPass.getText().toString().equals(etConfirmPass.getText().toString())) {
            Toast.makeText(
                    RegisterActivity.this,
                    "Error: non-matching passwords",
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
                    RegisterActivity.this,
                    "Error: account could not be created",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        DbHelper dbHelper = new DbHelper(RegisterActivity.this);
        boolean success = dbHelper.insertUserModel(userModel);
        dbHelper.close();
        if (!success) {
            Toast.makeText(
                    RegisterActivity.this,
                    "Error: account already exists",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Toast.makeText(
                RegisterActivity.this,
                "Account created",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}