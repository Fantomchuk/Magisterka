package com.example.nazar.v102_l100;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Nazar on 7/26/2017.
 */

public class DBFunctions {
    private final Context context;

    private DBHelper mDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DBFunctions(Context mCtx) {
        Log.d("qqqqq", "DBFUN : DBFunctions");
        this.context = mCtx;
    }

    public void open() {
        mDBHelper = new DBHelper(context);
        sqLiteDatabase = mDBHelper.getWritableDatabase();
        Log.d("qqqqq", "DBFUN : open");
    }

    public void close() {
        if (mDBHelper != null)
            mDBHelper.close();
        Log.d("qqqqq", "DBFUN : close");
    }

    public ArrayList<MyOnePosition> getAllPositions(){
        ArrayList<MyOnePosition> positions = new ArrayList<>();
        Cursor cursor;
        String str_sql = "";
        str_sql = "select c.Ax, c.Ay, c.Az, c.Wx, c.Wy, c.Wz, c.operation, c.stepTime " +
                "from collectionData as c ";
        cursor = sqLiteDatabase.rawQuery(str_sql, null);
        MyOnePosition onePosition;
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                int idAx = cursor.getColumnIndex(DBHelper.KEY_T1_Ax);
                int idAy = cursor.getColumnIndex(DBHelper.KEY_T1_Ay);
                int idAz = cursor.getColumnIndex(DBHelper.KEY_T1_Az);
                int idWx = cursor.getColumnIndex(DBHelper.KEY_T1_Wx);
                int idWy = cursor.getColumnIndex(DBHelper.KEY_T1_Wy);
                int idWz = cursor.getColumnIndex(DBHelper.KEY_T1_Wz);
                int idOperation = cursor.getColumnIndex(DBHelper.KEY_T1_OPERATION);
                int idTime = cursor.getColumnIndex(DBHelper.KEY_T1_STEP_TIME);

                do {
                    double ax = cursor.getDouble(idAx);
                    double ay = cursor.getDouble(idAy);
                    double az = cursor.getDouble(idAz);
                    double wx = cursor.getDouble(idWx);
                    double wy = cursor.getDouble(idWy);
                    double wz = cursor.getDouble(idWz);
                    int operation = cursor.getInt(idOperation);
                    int stepTime= cursor.getInt(idTime);
                    onePosition = new MyOnePosition(ax, ay, az, wx, wy, wz, stepTime, operation);
                    positions.add(onePosition);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return positions;
    }

    public void addPositionToDataBase(double _ax, double _ay, double _az, double _wx, double _wy, double _wz, int _stepTime, int _operation){
        //створюємо контейнер який буде зберігати 1 повний запис
        ContentValues contentValues = new ContentValues();

        //заповнюємо таблицю 1
        contentValues.clear();
        contentValues.put(DBHelper.KEY_T1_Ax, _ax);
        contentValues.put(DBHelper.KEY_T1_Ay, _ay);
        contentValues.put(DBHelper.KEY_T1_Az, _az);
        contentValues.put(DBHelper.KEY_T1_Wx, _wx);
        contentValues.put(DBHelper.KEY_T1_Wy, _wy);
        contentValues.put(DBHelper.KEY_T1_Wz, _wz);
        contentValues.put(DBHelper.KEY_T1_STEP_TIME, _stepTime);
        contentValues.put(DBHelper.KEY_T1_OPERATION, _operation);
        //переносимо дані з контейнера в БД
        sqLiteDatabase.insert(DBHelper.TABLE_NAME_1, null, contentValues);
    }

    public int lastOperationDataBase(){
        Cursor cursor;
        String str_sql = "SELECT c.operation " +
                    "FROM collectionData as c " +
                    "ORDER BY c.operation DESC " +
                    "LIMIT 1";
        cursor = sqLiteDatabase.rawQuery(str_sql, null);
        if (cursor.getCount() != 0) {
            int idOperation = cursor.getColumnIndex(DBHelper.KEY_T1_OPERATION);
            if (cursor.moveToFirst()) {
                int operation = cursor.getInt(idOperation);
                cursor.close();
                return operation;
            }
        }
        cursor.close();
        return -1;
    }

    public int lastIndexTable1(){
        Cursor cursor;
        String str_sql = "SELECT c._id " +
                "FROM collectionData as c " +
                "ORDER BY c._id DESC " +
                "LIMIT 1";
        cursor = sqLiteDatabase.rawQuery(str_sql, null);
        if (cursor.getCount() != 0) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(idIndex);
                cursor.close();
                return id;
            }
        }
        cursor.close();
        return -1;
    }

    public ArrayList<MyOnePosition> getPositionsToEnd(int startPosition){
        ArrayList<MyOnePosition> positions = new ArrayList<>();
        Cursor cursor;
        String str_sql = "select c.Ax, c.Ay, c.Az, c.Wx, c.Wy, c.Wz, c.operation, c.stepTime " +
                "from collectionData as c " +
                "WHERE c._id>" + startPosition;
        cursor = sqLiteDatabase.rawQuery(str_sql, null);
        MyOnePosition onePosition;
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                int idAx = cursor.getColumnIndex(DBHelper.KEY_T1_Ax);
                int idAy = cursor.getColumnIndex(DBHelper.KEY_T1_Ay);
                int idAz = cursor.getColumnIndex(DBHelper.KEY_T1_Az);
                int idWx = cursor.getColumnIndex(DBHelper.KEY_T1_Wx);
                int idWy = cursor.getColumnIndex(DBHelper.KEY_T1_Wy);
                int idWz = cursor.getColumnIndex(DBHelper.KEY_T1_Wz);
                int idOperation = cursor.getColumnIndex(DBHelper.KEY_T1_OPERATION);
                int idTime = cursor.getColumnIndex(DBHelper.KEY_T1_STEP_TIME);

                do {
                    double ax = cursor.getDouble(idAx);
                    double ay = cursor.getDouble(idAy);
                    double az = cursor.getDouble(idAz);
                    double wx = cursor.getDouble(idWx);
                    double wy = cursor.getDouble(idWy);
                    double wz = cursor.getDouble(idWz);
                    int operation = cursor.getInt(idOperation);
                    int stepTime= cursor.getInt(idTime);
                    onePosition = new MyOnePosition(ax, ay, az, wx, wy, wz, stepTime, operation);
                    positions.add(onePosition);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        return positions;
    }

    /**
     *
     * @param columnName - назва колонки з таблиці Т1
     * @param operation - наша дослід
     * @return ArrayList<Double> - масив значень по назві з бази даних Т1
     */
    public ArrayList<Double> getArrayForGraphics(String columnName, int operation){
        ArrayList<Double> result = new ArrayList<>();
        if(operation == -1) return result;
        Cursor cursor;
        String str_sql = "select c." + columnName + " " +
                "FROM collectionData as c "+
                "WHERE c.operation = " + operation;
        cursor = sqLiteDatabase.rawQuery(str_sql, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndex(columnName);
                do {
                    double res = cursor.getDouble(id);
                    result.add(res);
                } while (cursor.moveToNext());
            }
        }
        return result;
    }

    public ArrayList<Double> getArrayForGraphicsToAnd(String columnName, int operation, int startPosition){
        ArrayList<Double> result = new ArrayList<>();
        if(operation == -1) return result;
        Cursor cursor;
        String str_sql = "select c." + columnName + " " +
                "FROM collectionData as c "+
                "WHERE c.operation = " + operation + " AND c._id>" + startPosition;
        cursor = sqLiteDatabase.rawQuery(str_sql, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                int id = cursor.getColumnIndex(columnName);
                do {
                    double res = cursor.getDouble(id);
                    result.add(res);
                } while (cursor.moveToNext());
            }
        }
        return result;
    }

    public int getStepTime(int operation){
        Cursor cursor;
        String str_sql = "SELECT c.stepTime " +
                "FROM collectionData as c " +
                "WHERE c.operation = " + operation + " " +
                "ORDER BY c._id DESC " +
                "LIMIT 1";
        cursor = sqLiteDatabase.rawQuery(str_sql, null);
        if (cursor.getCount() != 0) {
            int stepTimeIndex = cursor.getColumnIndex(DBHelper.KEY_T1_STEP_TIME);
            if (cursor.moveToFirst()) {
                int stepTime = cursor.getInt(stepTimeIndex);
                cursor.close();
                return stepTime;
            }
        }
        cursor.close();
        return 0;
    }

}
