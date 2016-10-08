package com.example.olya.mydoings.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.olya.mydoings.Doings;

import java.util.Date;
import java.util.UUID;

/**
 * Created by tyuly on 08.10.2016.
 */

public class CursorWrapperDoings extends CursorWrapper {

    public CursorWrapperDoings(Cursor cursor) {
        super(cursor);
    }

    public Doings getDoings(){
        String uuidString = getString(getColumnIndex(DataDase.DoingsTable.Cols.UUID));
        String title = getString(getColumnIndex(DataDase.DoingsTable.Cols.TITLE));
        long date = getLong(getColumnIndex(DataDase.DoingsTable.Cols.DATE));
        int isDone = getInt(getColumnIndex(DataDase.DoingsTable.Cols.DONE));
        Doings doings=new Doings(UUID.fromString(uuidString));
        doings.setTitle(title);
        doings.setDate(new Date(date));
        doings.setDone(isDone != 0);


        return doings;
    }

}
