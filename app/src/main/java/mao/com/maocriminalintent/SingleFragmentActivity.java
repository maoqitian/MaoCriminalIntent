package mao.com.maocriminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by maoqi on 2018/2/25 0025.
 * 抽象基类  对应子类加载对应的Fragment
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    //子类返回对应的Fragment对象
    protected abstract Fragment createFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm=getSupportFragmentManager();

        Fragment fragment=fm.findFragmentById(R.id.fragment_container);
        if(fragment==null){
            fragment=createFragment();
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
