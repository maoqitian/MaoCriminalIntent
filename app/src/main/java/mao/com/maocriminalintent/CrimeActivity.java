package mao.com.maocriminalintent;


import android.support.v4.app.Fragment;

import mao.com.maocriminalintent.fragment.CrimeFragment;

/**
 * Crime明细
 */
public class CrimeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
