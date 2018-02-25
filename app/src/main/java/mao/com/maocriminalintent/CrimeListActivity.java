package mao.com.maocriminalintent;

import android.support.v4.app.Fragment;

import mao.com.maocriminalintent.fragment.CrimeListFragment;

/**
 * Created by maoqi on 2018/2/25 0025.
 * Crime 列表
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
