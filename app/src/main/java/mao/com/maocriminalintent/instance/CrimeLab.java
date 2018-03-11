package mao.com.maocriminalintent.instance;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mao.com.maocriminalintent.model.Crime;

/**
 * Created by maoqi on 2018/2/25 0025.
 * 单例  将Crime 对象保存
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;


    private Map<UUID,Crime> mCrimes;//使用LinkedHashMap优化 getCrime方法匹配
    //private List<Crime> mCrimes;

    public static CrimeLab getInstance(Context context){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes=new  LinkedHashMap<>();
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

    public List<Crime> getmCrimes() {
        return new ArrayList<>(mCrimes.values());
    }

    /*public void setmCrimes(List<Crime> mCrimes) {
        this.mCrimes = mCrimes;
    }*/

    public Crime getCrime(UUID uuid){
       /* for (Crime crime:mCrimes) {
            if(crime.getmId().equals(uuid)){
                return crime;
            }
        }
        return null;*/
       return mCrimes.get(uuid);
    }

    public void add (Crime crime){
        mCrimes.put(crime.getmId(),crime);
    }

    public void deleteCrime(UUID uuid){
        for (Iterator<Map.Entry<UUID,Crime>> it = mCrimes.entrySet().iterator(); it.hasNext();){
            Map.Entry<UUID,Crime> item = it.next();
            if(item.getKey().equals(uuid)){
                it.remove();
            }
         }
        }
}
