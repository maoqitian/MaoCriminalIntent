package mao.com.maocriminalintent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import mao.com.maocriminalintent.R;

/**
 * Created by maoqi on 2018/3/6 0006.
 * 显示对话框 放在DialogFragment 中创建并显示
 */

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE ="mao.com.maocriminalintent.model.date";

    private DatePicker mDatePicker;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date= (Date) getArguments().getSerializable(ARG_DATE);//从CrimeFragment 获取的Crime对应的date
        //通过Calendar 获取年月日
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        View dialogDateView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker=dialogDateView.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year,month,day,null);
        return new AlertDialog.Builder(getActivity())
                .setView(dialogDateView)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date1=new GregorianCalendar(year, month, day).getTime();
                        setResult(date1, Activity.RESULT_OK);
                    }
                })
                .create();
    }

    //调用该方法将Crime对象的date 通过Arguments传递到DatePickerFragment
    public static DatePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setResult(Date date,int resultCode){
         if(getTargetFragment()==null){
             return;
         }
         Intent intent=new Intent();
         intent.putExtra(EXTRA_DATE,date);
         getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
