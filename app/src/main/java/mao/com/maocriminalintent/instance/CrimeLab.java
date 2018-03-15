package mao.com.maocriminalintent.instance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mao.com.maocriminalintent.database.CrimeBaseHelper;
import mao.com.maocriminalintent.database.CrimeCursorWrapper;
import mao.com.maocriminalintent.database.CrimeDbSchema.CrimeTable;
import mao.com.maocriminalintent.model.Crime;

/**
 * Created by maoqi on 2018/2/25 0025.
 * 单例  将Crime 对象保存
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;


    //private Map<UUID,Crime> mCrimes;//使用LinkedHashMap优化 getCrime方法匹配
    //private List<Crime> mCrimes;


    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab getInstance(Context context){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mContext=context.getApplicationContext();
        mDatabase=new CrimeBaseHelper(mContext).getWritableDatabase();
        //mCrimes=new  LinkedHashMap<>();
        /*for (int i = 0; i < 100; i++) {   //先批量存入100个 毫无个性的Crime对象
            Crime crime = new Crime();
            crime.setmTitle("Crime #" + i);
            crime.setmSolved(i % 2 == 0); // Every other one
            crime.setPosition(i);
            if(i==2||i==4||i==6){
                crime.setmRequiresPolice(1);
            }else {
                crime.setmRequiresPolice(0);
            }
            mCrimes.put(crime.getmId(),crime);
            }*/
    }
    //数据库操作键值对象 （写入）
    public static ContentValues getContentValues(Crime crime){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID,crime.getmId().toString());
        contentValues.put(CrimeTable.Cols.TITLE,crime.getmTitle());
        contentValues.put(CrimeTable.Cols.DATE,crime.getmDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED,crime.ismSolved()?1:0);
        contentValues.put(CrimeTable.Cols.SUSPECT,crime.getmSuspect());
        return contentValues;
    }
    //获取数据库中的Crime 对象集合
    public List<Crime> getmCrimes() {
        //return new ArrayList<>();
        List<Crime>crimes=new ArrayList<>();
        CrimeCursorWrapper cursor=queryCrimes(null,null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){//有数据则直接取出
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
            //mDatabase.close();
        }
        return crimes;
    }

    /*public void setmCrimes(List<Crime> mCrimes) {
        this.mCrimes = mCrimes;
    }*/

    public Crime getCrime(UUID uuid){
       /* for (Crime crime:mCrimes) {
            if(crime.getmId().equals(uuid)){
                return crime;
            }
        }*/
        //return null;
       //return mCrimes.get(uuid);
        CrimeCursorWrapper cursor=queryCrimes(
                CrimeTable.Cols.UUID+" = ?",
                new String[]{uuid.toString()});
        try {
            if (cursor.getCount()==0) {return null;}
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    public void add (Crime crime){
        ContentValues contentValues = getContentValues(crime);
        //数据插入数据库
        mDatabase.insert(CrimeTable.NAME,null,contentValues);
        //mCrimes.put(crime.getmId(),crime);
    }

    //查询数据库

    /**
     *
     * @param whereClause 查询字段
     * @param whereArgs  查询字段字符串对象
     * @return
     */
    public CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME, null,//null 表示取全部字段的值
                whereClause, whereArgs,
                null, null, null
        );
            return new CrimeCursorWrapper(cursor);

    }

    //更新数据库
    public void updateCrime(Crime crime){
          String uuidStr=crime.getmId().toString();
          ContentValues contentValues=getContentValues(crime);
          mDatabase.update(CrimeTable.NAME,contentValues,
                  CrimeTable.Cols.UUID+" = ?",
                  new String[]{uuidStr});
    }

    public void deleteCrime(UUID uuid){
        /*for (Iterator<Map.Entry<UUID,Crime>> it = mCrimes.entrySet().iterator(); it.hasNext();){
            Map.Entry<UUID,Crime> item = it.next();
            if(item.getKey().equals(uuid)){
                it.remove();
            }
         }*/

        mDatabase.delete(CrimeTable.NAME,CrimeTable.Cols.UUID+" = ?",
                new String[]{uuid.toString()});
        }
}
