package mao.com.maocriminalintent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import mao.com.maocriminalintent.R;

/**
 * Created by maoqi on 2018/3/7 0007.
 * 时间选择
 */

public class TimePickerFragment extends DialogFragment {
    TimePicker mTimePicker;
    private static final String ARG_TIME = "TIME";
    public static final String EXTRA_TIME ="mao.com.maocriminalintent.model.time";

    Calendar mCalendar;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        mTimePicker=view.findViewById(R.id.dialog_time_picker);
        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour, minute;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = mTimePicker.getHour();
                            minute = mTimePicker.getMinute();
                        } else {
                            hour = mTimePicker.getCurrentHour();
                            minute = mTimePicker.getCurrentMinute();
                        }
                        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        mCalendar.set(Calendar.MINUTE, minute);

                        setResult(mCalendar.getTime(), Activity.RESULT_OK);
                    }
                }).create();
    }

    public static TimePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME,date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private void setResult(Date date,int resultCode){
        if(getTargetFragment()==null){
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(EXTRA_TIME,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
