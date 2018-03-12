package mao.com.maocriminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static mao.com.maocriminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by maoqi on 2018/3/12 0012.
 * 数据库帮助类
 */

public class CrimeBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION=1;
    private static final String DATABASE_NAME="crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //负责创建初始数据库
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         sqLiteDatabase.execSQL("create table "+ CrimeTable.NAME + "(" +
                 " _id integer primary key autoincrement, " +
                 CrimeTable.Cols.UUID + ", "+
                 CrimeTable.Cols.TITLE + ", " +
                 CrimeTable.Cols.DATE + ", " +
                 CrimeTable.Cols.SOLVED+")");
    }
    //法负责与升级相关的工作
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
