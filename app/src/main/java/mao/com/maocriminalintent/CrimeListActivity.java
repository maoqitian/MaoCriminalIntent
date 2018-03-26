package mao.com.maocriminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import mao.com.maocriminalintent.fragment.CrimeListFragment;

/**
 * Created by maoqi on 2018/2/25 0025.
 * Crime 列表
 */

public class CrimeListActivity extends SingleFragmentActivity {


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
}
