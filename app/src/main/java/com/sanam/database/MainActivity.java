package com.sanam.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etAddress;
    Button btnAdd;
    DbHelper dbHelper;

    Button btnSelect;
    Button btnSingleRow;
    Button btnDelete;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.etName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        dbHelper = new DbHelper(this);

        btnSelect = (Button) findViewById(R.id.btnGet);
        btnSingleRow = (Button) findViewById(R.id.btnGetSingle);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetail();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String retrieved = getAllData();
                Log.e("data", retrieved);

            }
        });

        btnSingleRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String single = getSingleRow(name);
                etAddress.setVisibility(View.INVISIBLE);
                Log.e("Single Data", single + "\n");
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counter = update();
                Log.e("counter", counter + "\n");
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int counterDeleted = remove();
                Log.e("counterDeleted", counterDeleted + "\n");
            }
        });
    }

    private int remove() {
        //delete * from tablename where name="abc";
        String deleting = etName.getText().toString().trim();
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String[] whereArgs = {deleting};
        int deletedNoRows = sqLiteDatabase.delete(DbHelper.TABLE_NAME, DbHelper.NAME + "=?", whereArgs);
        return deletedNoRows;
    }

    private int update() {
        String newName = "Saturday";
        String oldName = "Friday";
        String newAddress = "Sat Address";
        String oldAddress = "FridayAddrss";
        //update table set name="jack" where Name="sanam"
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME, newName);
        contentValues.put(DbHelper.ADDRESS, newAddress);

        String[] whereArgs = {oldName, oldAddress};
        int changedrow = db.update(DbHelper.TABLE_NAME, contentValues,
                DbHelper.NAME + "=?" + "AND " +
                        DbHelper.ADDRESS + "=?",
                whereArgs);
        return changedrow;
    }

    private String getSingleRow(String n) {
        //select name and id where name = jack
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //column you want to select
        String[] columns = {DbHelper.NAME, DbHelper.ADDRESS};
        //""
        // DbHelper.NAME +"="
        // DbHelper.NAME +"= ''"
        // DbHelper.NAME +"= '+"n"+'"
        Cursor cursor = db.query(DbHelper.TABLE_NAME, columns,
                DbHelper.NAME + "='" + n + "'",
                null, null, null, null);
        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(DbHelper.NAME);
            int index2 = cursor.getColumnIndex(DbHelper.ADDRESS);
            String name = cursor.getString(index1);
            String address = cursor.getString(index2);

            buffer.append(name + address);
        }
        return buffer.toString();

    }


    private String getAllData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //Name of the columns
        String[] columns = {DbHelper.UID, DbHelper.NAME, DbHelper.ADDRESS};

        Cursor cursor = db.query(DbHelper.TABLE_NAME, columns, null, null, null, null, null);

        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()) {

         /*int id = cursor.getColumnIndex(DbHelper.UID);
           int nameId= cursor.getColumnIndex(DbHelper.NAME);
          int addressId = cursor.getColumnIndex(DbHelper.ADDRESS);*/

            int columnId = cursor.getInt(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            buffer.append(columnId + "  " + name + " " + address + "\n");
        }

        return buffer.toString();
    }

    private void addDetail() {

        //INSERT INTO table_name VALUES (value1,value2,value3,...);
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        String result = "Name :" + name + "\n" + "Address:" + address;
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME, name);
        contentValues.put(DbHelper.ADDRESS, address);

        //if something goes wrong id  is negative else positive
        // id indicate the row id of the colum that was succesfully inserted
        long id = db.insert(DbHelper.TABLE_NAME, null, contentValues);
        //long id = db.insert(DbHelper.TABLE_NAME, DbHelper.NAME, contentValues);

        Log.e("mero data yo colm ma ", id + " ");

    }
}
