package com.example.olya.mydoings.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLClientInfoException;

/**
 * Created by tyuly on 08.10.2016.
 */

public class BaseHelper  extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "doItDB.db";

    public BaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DataDase.DoingsTable.NAME +
                "(" +            " _id integer primary key autoincrement, " +
                DataDase.DoingsTable.Cols.UUID + ", " +
                DataDase.DoingsTable.Cols.TITLE + ", " +            DataDase.DoingsTable.Cols.DATE + ", " +
                DataDase.DoingsTable.Cols.DONE +            ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
