package com.example.olya.mydoings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;

import com.example.olya.mydoings.database.BaseHelper;
import com.example.olya.mydoings.database.CursorWrapperDoings;
import com.example.olya.mydoings.database.DataDase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.R.attr.id;

/**
 * Created by tyuly on 03.10.2016.
 */

public class DoingsLab {
    private static DoingsLab sDoingsLab;
    //public static List<Doings> mDoings;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DoingsLab get(Context context) {
        if (sDoingsLab == null) {
            sDoingsLab = new DoingsLab(context);
        }
        return sDoingsLab;
    }

    private DoingsLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new BaseHelper(mContext).getWritableDatabase();
       // mDoings=new ArrayList<>();

    }

    public void addDoings(Doings d) {
        //mDoings.add(d);
        ContentValues contentValues=getContentValues(d);
        mDatabase.insert(DataDase.DoingsTable.NAME,null,contentValues);

    }

    public void deleteDoings(Doings d, UUID id) {
        //mDoings.remove(d);

        ContentValues contentValues=getContentValues(d);
        mDatabase.delete(DataDase.DoingsTable.NAME,DataDase.DoingsTable.Cols.UUID + " = ?", new String[] { id.toString() });

    }



    public  List<Doings> getDoings(){
        //return mDoings;
        List<Doings> doingsList=new ArrayList<>();
        CursorWrapperDoings cursorWrapperDoings=queryDoings(null,null);
        try {
            cursorWrapperDoings.moveToFirst();
            while (!cursorWrapperDoings.isAfterLast()) {
                doingsList.add(cursorWrapperDoings.getDoings());
                cursorWrapperDoings.moveToNext();
            }
        } finally {
            cursorWrapperDoings.close();
        }
        return doingsList;
    }

    public Doings getDoings(UUID id) { //получение по id
        /*for (Doings doing : mDoings) {
            if (doing.getId().equals(id)) {
                return doing;
            }
        }*/
        CursorWrapperDoings cursor = queryDoings(
                DataDase.DoingsTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getDoings();
        } finally {
            cursor.close();
        }
    }

    public void updateDoings(Doings doings) {
        String uuidString = doings.getId().toString();
        ContentValues values = getContentValues(doings);
        mDatabase.update(DataDase.DoingsTable.NAME, values,
                DataDase.DoingsTable.Cols.UUID + " = ?", //?  для того, чтобы  значение интерпретировалос как строковые данные, а не как код, на случай,
                // если сторка содержит sqlкод
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Doings doings) {
        ContentValues values = new ContentValues();
        values.put(DataDase.DoingsTable.Cols.UUID, doings.getId().toString());
        values.put(DataDase.DoingsTable.Cols.TITLE, doings.getTitle());
        values.put(DataDase.DoingsTable.Cols.DATE, doings.getDate().getTime());
        values.put(DataDase.DoingsTable.Cols.DONE, doings.getDone() ? 1 : 0);
        return values;    }

    private CursorWrapperDoings queryDoings(String whereClause, String[] whereArgs){
        Cursor cursor=mDatabase.query(DataDase.DoingsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new CursorWrapperDoings(cursor);
    }
}
