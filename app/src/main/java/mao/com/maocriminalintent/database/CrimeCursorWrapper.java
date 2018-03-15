package mao.com.maocriminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import mao.com.maocriminalintent.database.CrimeDbSchema.CrimeTable;
import mao.com.maocriminalintent.model.Crime;

/**
 * Created by maoqi on 2018/3/13 0013.
 * Cursor 的封装对象，扩展对Cursor操作
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    //读取信息
    public Crime getCrime(){
        String uuidStr=getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime=new Crime(UUID.fromString(uuidStr));
        crime.setmTitle(title);
        crime.setmDate(new Date(date));
        crime.setmSolved(isSolved!=0);
        crime.setmSuspect(suspect);
        return crime;
    }
}
