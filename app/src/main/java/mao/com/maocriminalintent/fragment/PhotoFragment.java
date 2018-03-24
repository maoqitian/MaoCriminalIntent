package mao.com.maocriminalintent.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.Serializable;

import mao.com.maocriminalintent.R;
import mao.com.maocriminalintent.util.MyUtils;

/**
 * Created by maoqi on 2018/3/24 0024.
 * 显示方法版的照片
 */

public class PhotoFragment extends android.support.v4.app.DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
        ImageView photoView=view.findViewById(R.id.iv_dialog_photo);
        //获取图片路径
        String path =(String) getArguments().getSerializable("path");
        final AlertDialog dialog=new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("陋习照片放大")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
        photoView.setImageBitmap(MyUtils.getScaledBitmap(path,getActivity()));
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static PhotoFragment newInstance(String path) {

        Bundle args = new Bundle();
        args.putSerializable("path",path);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
