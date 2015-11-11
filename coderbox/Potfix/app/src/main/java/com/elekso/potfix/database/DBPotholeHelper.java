package com.elekso.potfix.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by mandar on 08-Oct-15.
 */
public class DBPotholeHelper extends SQLiteOpenHelper
{
    //
    // Database
    //
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DBPothole.db";

    //
    // Table
    //
    public static final String TABLE_RECENT_POTHOLE = "tbl_RECENT_POTHOLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LA = "col_la";
    public static final String COLUMN_LO = "col_lo";

    public static final String TABLE_PROFIE = "tbl_PROFILE";
    public static final String PRO_COLUMN_ID = "id";
    public static final String PRO_COLUMN_NAME = "name";
    public static final String PRO_COLUMN_EMAIL = "email";

    //
    // Database creation sql statement
    //
    private static final String CREATE_TABLE_RECENT_POTHOLE = "create table "
            + TABLE_RECENT_POTHOLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_LA
            + " DOUBLE not null," + COLUMN_LO
            +" DOUBLE not null)";

    private static final String CREATE_TABLE_PROFILE = "create table "
            + TABLE_PROFIE + "(" + PRO_COLUMN_ID
            + " integer, " + PRO_COLUMN_EMAIL
            + " text," + PRO_COLUMN_NAME
            +" text)";

    //
    //  Methods
    //
    public DBPotholeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECENT_POTHOLE);
        db.execSQL(CREATE_TABLE_PROFILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_POTHOLE);
        onCreate(db);
    }
}
