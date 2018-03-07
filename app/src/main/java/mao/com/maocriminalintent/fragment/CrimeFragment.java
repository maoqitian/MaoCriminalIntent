package mao.com.maocriminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

import mao.com.maocriminalintent.R;
import mao.com.maocriminalintent.instance.CrimeLab;
import mao.com.maocriminalintent.model.Crime;
import mao.com.maocriminalintent.util.MyUtils;

/**
 * Created by maoqi on 2018/2/23 0023.
 * 模型及视图对象交互的控制器
 */

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";//对话框Fragment的 tag
    private static final String DIALOG_TIME = "DialogTime";//对话框Fragment的 tag
    private static final int REQUEST_DATE = 0;//DatePickerFragment 数据返回请求码
    private static final int REQUEST_TIME = 1;//TimePickerFragment 数据返回请求码
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime=new Crime();
        //Intent 的Extra 已经存储了对应的crimeId 根据这个UUID 来获取对应的 Crime的对象
        //UUID crimeId  = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        //改为从fragment的argument中获取UUID,这样CrimeFragment类变得通用，而不依靠特定的Activity
        UUID crimeId= (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime= CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    //附加argument给 fragment
    public static CrimeFragment newInstance(UUID crimed) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimed);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mDateButton = view.findViewById(R.id.crime_date);
        mTimeButton=view.findViewById(R.id.crime_time);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setmTitle(charSequence.toString());//根据输入的标题给model 设置数据
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fragmentManager,DIALOG_DATE);
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getFragmentManager();
                TimePickerFragment timePickerFragment=TimePickerFragment.newInstance(mCrime.getmDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                timePickerFragment.show(fragmentManager,DIALOG_TIME);
            }
        });
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        return view;
    }

    private void updateDate() { //更新日期
        mDateButton.setText(MyUtils.getFormatDate(mCrime.getmDate()));
    }

    //接收DatePickerFragment 同步设置的日期信息
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }
        if(requestCode==REQUEST_DATE){
           Date date= (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
           mCrime.setmDate(date);
           updateDate();
        }
        else if(requestCode==REQUEST_TIME){
            Date date= (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setmDate(date);
            updateDate();
        }
    }
}
