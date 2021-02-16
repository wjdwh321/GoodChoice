package com.example.testgc2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/*
 this program is made by 박정조,조성재
 this project provide restaurent search operation.
 the development date : from 2017-11-27 to 2017-12-06

 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "goodchoice.db";
    public static final String TABLE_NAME = "search_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "SEARCH";

    public static final String TABLE_NAME2 = "즐겨찾기";
    public static final String CO1 = "ID";
    public static final String CO2 = "NAME";
    public static final String CO3 = "ADDRESS";
    public static final String CO4 = "LAT";
    public static final String CO5 = "LNG";
    public static final String CO6 = "RATING";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " SEARCH TEXT UNIQUE)";

        db.execSQL(createTable);

        String createTable1 = "CREATE TABLE " + TABLE_NAME2 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT UNIQUE, ADDRESS TEXT, LAT TEXT, LNG TEXT, RATING TEXT)";
        db.execSQL(createTable1);



        Log.d("db","create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);

        onCreate(db);

    }

    public boolean addData(String search) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, search);

        long result = db.insert(TABLE_NAME, null, contentValues);

        Log.d("db","insert");
        //if data as inserted incorrectly return -1

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }


    public boolean addData2(String name, String address,String lat, String lng, String rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("ADDRESS", address);
        contentValues.put("LAT", lat);
        contentValues.put("LNG", lng);
        contentValues.put("RATING", rating);

        long result = db.insert(TABLE_NAME2, null, contentValues);

        Log.d("db","insert");
        //if data as inserted incorrectly return -1

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


        public Cursor getListContents()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME  ,null);
            return data;

        }
        public Cursor getListContents2()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME2  ,null);
            return data;
        }

        public Cursor getListSelect()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME2 + " ORDER BY RANDOM() LIMIT 1" , null);
            return data;
        }


        public void deletedata(String name)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + TABLE_NAME2 + " WHERE "
                    + CO2 + " = '" + name + "'" ;

            Log.d("db","delete");

            db.execSQL(query);
        }
    }








