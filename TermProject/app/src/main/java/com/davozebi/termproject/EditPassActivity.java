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

public class EditPassActivity extends AppCompatActivity {

    Button btnRandomPass;
    Button btnUpdatePass;
    Button btnCancel;

    EditText etService;
    EditText etUserName;
    TextInputEditText etPass;
    TextInputEditText etConfirmPass;

    PassModel oldPassModel;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);

        oldPassModel = (PassModel) getIntent().getSerializableExtra("oldPassModel");
        position = getIntent().getIntExtra("position", -1);

        btnRandomPass = findViewById(R.id.activity_edit_pass_btn_random_pass);
        btnUpdatePass = findViewById(R.id.activity_edit_pass_btn_update_pass);
        btnCancel = findViewById(R.id.activity_edit_pass_btn_cancel);

        etService = findViewById(R.id.activity_edit_pass_et_service);
        etUserName = findViewById(R.id.activity_edit_pass_et_user_name);
        etPass = findViewById(R.id.activity_edit_pass_et_pass);
        etConfirmPass = findViewById(R.id.activity_edit_pass_et_confirm_pass);

        etService.setText(oldPassModel.getService());
        etUserName.setText(oldPassModel.getUserName());
        etPass.setText(oldPassModel.getPass());
        etConfirmPass.setText(oldPassModel.getPass());

        btnRandomPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomPass(14);
            }
        });

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPass();
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

    private void editPass() {
        if (etPass.getText().toString().isEmpty() || etConfirmPass.getText().toString().isEmpty()) {
            Toast.makeText(
                    EditPassActivity.this,
                    "Error: empty field(s)",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (!etPass.getText().toString().equals(etConfirmPass.getText().toString())) {
            Toast.makeText(
                    EditPassActivity.this,
                    "Error: non-matching passwords",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        PassModel newPassModel;
        try {
            newPassModel = new PassModel(
                    oldPassModel.getFkEmail(),
                    etService.getText().toString(),
                    etUserName.getText().toString(),
                    etPass.getText().toString()
            );
        }
        catch (Exception e) {
            Toast.makeText(
                    EditPassActivity.this,
                    "Error: password could not be updated",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        DbHelper dbHelper = new DbHelper(EditPassActivity.this);
        if (dbHelper.passModelExists(newPassModel)) {
            Toast.makeText(
                    EditPassActivity.this,
                    "Error: entry already exists",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        boolean success = dbHelper.updatePassModel(oldPassModel, newPassModel);
        dbHelper.close();
        if (!success) {
            Toast.makeText(
                    EditPassActivity.this,
                    "Error: entry already exists",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("passModel", newPassModel);
        returnIntent.putExtra("position", position);
        setResult(Activity.RESULT_OK, returnIntent);

        Toast.makeText(
                EditPassActivity.this,
                "Password updated",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}