package mao.com.maocriminalintent.instance;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mao.com.maocriminalintent.model.Crime;

/**
 * Created by maoqi on 2018/2/25 0025.
 * 单例  将Crime 对象保存
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;



    private List<Crime> mCrimes;

    public static CrimeLab getInstance(Context context){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){
        mCrimes=new ArrayList<>();
        for (int i = 0; i < 100; i++) {   //先批量存入100个 毫无个性的Crime对象
            Crime crime = new Crime();
            crime.setmTitle("Crime #" + i);
            crime.setmSolved(i % 2 == 0); // Every other one
            mCrimes.add(crime);
            }
    }

    public List<Crime> getmCrimes() {
        return mCrimes;
    }

    public void setmCrimes(List<Crime> mCrimes) {
        this.mCrimes = mCrimes;
    }

    public Crime getCrime(UUID uuid){
        for (Crime crime:mCrimes) {
            if(crime.getmId().equals(uuid)){
                return crime;
            }
        }
        return null;
    }
}
