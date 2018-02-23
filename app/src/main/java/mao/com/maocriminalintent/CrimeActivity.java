package mao.com.maocriminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mao.com.maocriminalintent.fragment.CrimeFragment;

public class CrimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm=getSupportFragmentManager();

        Fragment fragment=fm.findFragmentById(R.id.fragment_container);
        if(fragment==null){
           fragment=new CrimeFragment();
           fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
