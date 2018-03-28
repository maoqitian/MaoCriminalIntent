package mao.com.maocriminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import mao.com.maocriminalintent.fragment.CrimeFragment;
import mao.com.maocriminalintent.fragment.CrimeListFragment;
import mao.com.maocriminalintent.model.Crime;

/**
 * Created by maoqi on 2018/2/25 0025.
 * Crime 列表
 */

public class CrimeListActivity extends SingleFragmentActivity implements
        CrimeListFragment.Callbacks,CrimeFragment.CrimeCallbacks {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {//使用别名加载不同资源布局
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        //如果布局不包含可以放入CrimeFragment得detial_fragment_container，则启动新的CrimePagerActivity
        if(findViewById(R.id.detial_fragment_container) == null){
            Intent intent=CrimePagerActivity.newIntent(getApplicationContext(),crime.getmId());
            startActivity(intent);
        }else {
            //否则启动事物将CrimeFragment放入布局中
            CrimeFragment crimeFragment=CrimeFragment.newInstance(crime.getmId());
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.detial_fragment_container,crimeFragment)
                    .commit();
        }
    }
    //更新
    @Override
    public void onUpdateCrime(Crime crime) {
         CrimeListFragment crimeListFragment=
                 (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
         crimeListFragment.updateUI();
    }
}
