package mao.com.maocriminalintent;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import mao.com.maocriminalintent.fragment.CrimeFragment;

/**
 * Crime明细
 */
public class CrimeActivity extends SingleFragmentActivity {


    private static final String EXTRA_CRIME_ID="mao.com.maocriminalintent.model.crime_id";

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);//将UUID 给到Fragment 的argument
    }

    public static Intent newIntent(Context packageContext, UUID crimeId){
          Intent intent=new Intent(packageContext,CrimeActivity.class);
          intent.putExtra(EXTRA_CRIME_ID,crimeId);
          return intent;
    }

}
