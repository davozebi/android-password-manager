package com.davozebi.termproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_USER_PASS = "USER_PASS";

    public static final String PASS_TABLE = "PASS_TABLE";
    public static final String COLUMN_FK_EMAIL = "FK_EMAIL";
    public static final String COLUMN_SERVICE = "SERVICE";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_PASS = "PASS";

    public DbHelper(@Nullable Context context) {
        super(context, "my.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + USER_TABLE + " ("
                + COLUMN_EMAIL + " TEXT NOT NULL, "
                + COLUMN_USER_PASS + " TEXT NOT NULL, "
                + "PRIMARY KEY (" + COLUMN_EMAIL + ")"
                + ");";
        db.execSQL(sql);

        sql = "CREATE TABLE " + PASS_TABLE + " ("
                + COLUMN_FK_EMAIL + " TEXT NOT NULL, "
                + COLUMN_SERVICE + " TEXT NOT NULL, "
                + COLUMN_USER_NAME + " TEXT NOT NULL, "
                + COLUMN_PASS + " TEXT NOT NULL, "
                + "PRIMARY KEY ("
                + COLUMN_FK_EMAIL + ", " + COLUMN_SERVICE + ", "
                + COLUMN_USER_NAME
                + "), "
                + "FOREIGN KEY (" + COLUMN_FK_EMAIL + ")"
                + "REFERENCES " + USER_TABLE + "(" + COLUMN_EMAIL + ") ON DELETE CASCADE"
                + ");";
        db.execSQL(sql);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public boolean insertUserModel(UserModel userModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_USER_PASS, userModel.getPass());

        long insert = db.insert(USER_TABLE, null, cv);
        db.close();
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean deleteUserModel(UserModel userModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        long delete = db.delete(
                USER_TABLE,
                COLUMN_EMAIL + " = ?" + " AND " + COLUMN_USER_PASS + " = ?",
                new String[]{userModel.getEmail(), userModel.getPass()});
        db.close();
        if (delete == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertPassModel(PassModel passModel, UserModel userModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FK_EMAIL, userModel.getEmail());
        cv.put(COLUMN_SERVICE, passModel.getService());
        cv.put(COLUMN_USER_NAME, passModel.getUserName());
        cv.put(COLUMN_PASS, passModel.getPass());

        long insert = db.insert(PASS_TABLE, null, cv);
        db.close();
        if (insert == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean userModelExists(UserModel userModel) {

        String sql = "SELECT *"
                + " FROM " + USER_TABLE
                + " WHERE " + COLUMN_EMAIL + " = '" + userModel.getEmail() + "'"
                + " AND " + COLUMN_USER_PASS + " = '" + userModel.getPass() + "'" + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if (count != 1) {
            return false;
        }
        else {
            return true;
        }
    }

    public List<PassModel> getPassModels(UserModel userModel) {

        List<PassModel> list = new ArrayList<>();

        String sql = "SELECT *"
                + " FROM " + PASS_TABLE
                + " WHERE " + COLUMN_FK_EMAIL + " = '" + userModel.getEmail() + "'" + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String fkEmail = cursor.getString(0);
                String service = cursor.getString(1);
                String userName = cursor.getString(2);
                String pass = cursor.getString(3);

                PassModel passModel = new PassModel(fkEmail, service, userName, pass);
                list.add(passModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean passModelExists(PassModel passModel) {

        String sql = "SELECT *"
                + " FROM " + PASS_TABLE
                + " WHERE " + COLUMN_FK_EMAIL + " = '" + passModel.getFkEmail() + "'"
                + "AND " + COLUMN_SERVICE + " = '" + passModel.getService() + "'"
                + "AND " + COLUMN_USER_NAME + " = '" + passModel.getUserName() + "'"
                + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        boolean passExists = false;
        if (cursor.moveToFirst()) {
            passExists = true;
        }

        cursor.close();
        db.close();
        return passExists;
    }

    public boolean updatePassModel(PassModel oldPassModel, PassModel newPassModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SERVICE, newPassModel.getService());
        cv.put(COLUMN_USER_NAME, newPassModel.getUserName());
        cv.put(COLUMN_PASS, newPassModel.getPass());

        long update = db.update(PASS_TABLE, cv,
                COLUMN_FK_EMAIL + " = ?"
                + " AND " + COLUMN_SERVICE + " = ?"
                + " AND " + COLUMN_USER_NAME + " = ?",
                new String[]{
                        oldPassModel.getFkEmail(), oldPassModel.getService(),
                        oldPassModel.getUserName()});
        db.close();
        if (update == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean deletePassModel(PassModel passModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        long delete = db.delete(
                PASS_TABLE,
                COLUMN_FK_EMAIL + " = ?"
                        + " AND " + COLUMN_SERVICE + " = ?"
                        + " AND " + COLUMN_USER_NAME + " = ?"
                        + " AND " + COLUMN_PASS + " = ?",
                new String[]{
                        passModel.getFkEmail(), passModel.getService(),
                        passModel.getUserName(), passModel.getPass()});
        db.close();
        if (delete == -1) {
            return false;
        }
        else {
            return true;
        }
    }
}
