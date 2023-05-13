package com.example.car_app;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DbActivity  extends AppCompatActivity {

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception {
        SQLiteDatabase db;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/car.db",
                null
        );
        Toast.makeText(getApplicationContext(), getFilesDir().getPath() + "/car.db",
                Toast.LENGTH_LONG).show();
        Log.d("DIRLOCATION", getFilesDir().getPath() + "/car.db");
        if (args == null) {
            db.execSQL(SQL);
        } else {
            db.execSQL(SQL, args);
        }
        db.close();
        if (success != null)
            success.OnSuccess();
    }

    protected void InitDB() throws Exception {
        ExecSQL(
                "CREATE TABLE if not exists CAR( " +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Name text not null, " +
                        "RegistrationNumber text not null, " +
                        "BrandAndModel text not null, " +
                        "unique(RegistrationNumber, Name) " +
                        "); ",
                null,
                () -> Toast.makeText(getApplicationContext(),
                        "CREATE TABLE SUCCESS",
                        Toast.LENGTH_LONG
                ).show()
        );


    }
    protected void SelectSQL(String SelectQ, String[] args, OnSelectSuccess
            success)
            throws Exception
    {
        SQLiteDatabase db;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/car.db",
                null
        );
        Toast.makeText(getApplicationContext(), getFilesDir().getPath() + "/car.db",
                Toast.LENGTH_LONG).show();
        Log.d("DIRLOCATION", getFilesDir().getPath() + "/car.db");
        Cursor cursor = db.rawQuery(SelectQ, args);
        while (cursor.moveToNext()){
            @SuppressLint("Range")
            String ID= cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range")
            String Name= cursor.getString(cursor.getColumnIndex("Name"));
            @SuppressLint("Range")
            String RegistrationNumber= cursor.getString(cursor.getColumnIndex("RegistrationNumber"));
            @SuppressLint("Range")
            String BrandAndModel= cursor.getString(cursor.getColumnIndex("BrandAndModel"));
            success.OnElementSelected(ID, Name, RegistrationNumber, BrandAndModel);
        }
        db.close();
    }

    protected interface OnQuerySuccess {
        public void OnSuccess();
    }
    protected interface OnSelectSuccess{
        public void
        OnElementSelected(String ID, String Name, String RegistrationNumber, String BrandAndModel);

    }

}
