package com.elekso.potfix.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elekso.potfix.model.PotfixModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mandar on 08-Oct-15.
 */
public class DBDataSource {

    // Database fields
    private static SQLiteDatabase database;
    private static DBPotholeHelper dbHelper;
    private String[] allColumns = { DBPotholeHelper.COLUMN_ID, DBPotholeHelper.COLUMN_LA , DBPotholeHelper.COLUMN_LO};
    public static Context context;
    private static DBDataSource dbInstance = new DBDataSource();

    private DBDataSource() {
    }

    public static DBDataSource getInstance(Context c) {
        DBDataSource.context = c;
        return dbInstance;
    }
    public static DBDataSource getInstance() {
        return dbInstance;
    }

    public static void init()
    {
        dbInstance = new DBDataSource(context);
        try {
            dbInstance.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBDataSource(Context context) {
        dbHelper = new DBPotholeHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //
    //  Pothole
    //    Count
    //    List
    //    Update
    //
    public void savePotholeDB(double la, double lo) {
        ContentValues values = new ContentValues();
        values.put(DBPotholeHelper.COLUMN_LA, la);
        values.put(DBPotholeHelper.COLUMN_LO, lo);
        long insertId = database.insert(DBPotholeHelper.TABLE_RECENT_POTHOLE, null, values);
    }

    public int countPotholeDB()
    {
        String countQuery = "SELECT  * FROM " + DBPotholeHelper.TABLE_RECENT_POTHOLE;
    //  SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int ret=0;
        if (cursor.moveToFirst()) {
            ret= cursor.getCount();
        }
        cursor.close();

        // return count
        return ret;
    }

    public List<PotfixModel>  updatePothole()
    {
        List<PotfixModel> datalist= new ArrayList<>();
        String countQuery = "SELECT  * FROM " + DBPotholeHelper.TABLE_RECENT_POTHOLE;
        //  SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    PotfixModel mod =new PotfixModel();
                    mod.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DBPotholeHelper.COLUMN_LA)));
                    mod.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DBPotholeHelper.COLUMN_LO)));
                    mod.setIsBump(true);
                    datalist.add(mod);
                } while (cursor.moveToNext());
            }
        }
        return  datalist;
    }


}
