package com.davozebi.termproject;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManagerActivity extends AppCompatActivity
        implements MyRecyclerViewAdapter.ItemClickListener {

    RecyclerView rvServices;
    MyRecyclerViewAdapter adapter;

    Button btnCreatePass;
    Button btnLogout;
    Button btnDeleteAccount;

    UserModel userModel;

    List<PassModel> listPassModels;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logout();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.btn_copy_pass) {
            copyToClipboard(position);
        }
        if (view.getId() == R.id.btn_edit) {
            editPass(position);
        }
        if (view.getId() == R.id.btn_remove) {
            removePass(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Toast.makeText(
                ManagerActivity.this,
                "Logged in",
                Toast.LENGTH_SHORT
        ).show();

        DbHelper dbHelper = new DbHelper(ManagerActivity.this);
        listPassModels = dbHelper.getPassModels(userModel);
        dbHelper.close();

        rvServices = findViewById(R.id.activity_manager_rv_services);
        rvServices.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        adapter = new MyRecyclerViewAdapter(this, listPassModels);
        adapter.setClickListener(this);
        rvServices.setAdapter(adapter);
        rvServices.setLayoutManager(new LinearLayoutManager(this));

        btnCreatePass = findViewById(R.id.activity_manager_btn_create_pass);
        btnLogout = findViewById(R.id.activity_manager_btn_logout);
        btnDeleteAccount = findViewById(R.id.activity_manager_btn_delete_account);

        btnCreatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPass();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });
    }

    void deleteAccount() {
        DbHelper dbHelper = new DbHelper(ManagerActivity.this);
        boolean success = dbHelper.deleteUserModel(userModel);
        dbHelper.close();
        if (!success) {
            Toast.makeText(
                    ManagerActivity.this,
                    "Error: could not delete account",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        listPassModels.clear();
        adapter.notifyDataSetChanged();

        Toast.makeText(
                ManagerActivity.this,
                "Account deleted",
                Toast.LENGTH_SHORT
        ).show();

        logout();
    }

    void editPass(int position) {
        Intent i = new Intent(ManagerActivity.this, EditPassActivity.class);
        PassModel passModel = adapter.getItem(position);
        i.putExtra("oldPassModel", passModel);
        i.putExtra("position", position);
        startActivityForResult(i, 456);
    }

    void removePass(int position) {
        PassModel passModel = adapter.getItem(position);
        DbHelper dbHelper = new DbHelper(ManagerActivity.this);
        boolean success = dbHelper.deletePassModel(passModel);
        dbHelper.close();
        if (!success) {
            Toast.makeText(
                    ManagerActivity.this,
                    "Error: could not remove password",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        listPassModels.remove(position);
        adapter.notifyItemRemoved(position);

        Toast.makeText(
                ManagerActivity.this,
                "Password removed",
                Toast.LENGTH_SHORT
        ).show();
    }

    void createPass() {
        Intent i = new Intent(ManagerActivity.this, CreatePassActivity.class);
        i.putExtra("userModel", userModel);
        startActivityForResult(i, 123);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 123) {
            listPassModels.add((PassModel) data.getSerializableExtra("passModel"));
            int position = listPassModels.size() - 1;
            adapter.notifyItemInserted(position);
            rvServices.scrollToPosition(position);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 456) {
            int position = data.getIntExtra("position", -1);
            listPassModels.set(position, (PassModel) data.getSerializableExtra("passModel"));
            adapter.notifyItemChanged(position);
        }
    }

    void logout() {
        Toast.makeText(
                ManagerActivity.this,
                "Logged out",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }

    void copyToClipboard(int position) {
        Toast.makeText(this, "Password copied to clipboard", Toast.LENGTH_SHORT).show();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("pass", adapter.getItem(position).getPass());
        clipboard.setPrimaryClip(clip);
    }
}