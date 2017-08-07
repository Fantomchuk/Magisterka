package com.example.nazar.v102_l100;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nazar on 7/26/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    //Constants for the database
    public final static int DATABASE_WERSION = 1;
    //Name DB
    public final static String DATABASE_NAME = "Dyplom";
    //Table Name
    public static final String TABLE_NAME_1 = "collectionData";
    //Column names
    public final static String KEY_ID = "_id";
    public final static String KEY_T1_Ax = "Ax";
    public final static String KEY_T1_Ay = "Ay";
    public final static String KEY_T1_Az = "Az";
    public final static String KEY_T1_Wx = "Wx";
    public final static String KEY_T1_Wy = "Wy";
    public final static String KEY_T1_Wz = "Wz";
    public final static String KEY_T1_OPERATION = "operation";
    public final static String KEY_T1_STEP_TIME = "stepTime";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_WERSION);
        Log.d("qqqqq", "DBhelp : DBHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("qqqqq", "DBhelp : onCreate");
        sqLiteDatabase.execSQL("create table " + TABLE_NAME_1 + "(" + KEY_ID
                + " integer primary key," + KEY_T1_Ax + " real," + KEY_T1_Ay + " real,"
                + KEY_T1_Az + " real," + KEY_T1_Wx + " real," + KEY_T1_Wy + " real,"
                + KEY_T1_Wz + " real," + KEY_T1_OPERATION + " integer," + KEY_T1_STEP_TIME + " integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME_1);

        onCreate(sqLiteDatabase);
    }
}
