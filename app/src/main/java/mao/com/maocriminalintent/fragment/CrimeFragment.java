package mao.com.maocriminalintent.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import mao.com.maocriminalintent.CrimeListActivity;
import mao.com.maocriminalintent.R;
import mao.com.maocriminalintent.instance.CrimeLab;
import mao.com.maocriminalintent.model.Crime;
import mao.com.maocriminalintent.util.MyUtils;

/**
 * Created by maoqi on 2018/2/23 0023.
 * 模型及视图对象交互的控制器
 */

public class CrimeFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";//对话框Fragment的 tag
    private static final String DIALOG_TIME = "DialogTime";//对话框Fragment的 tag
    private static final String DIALOG_PHOTO = "DialogPhoto";//对话框Fragment的 tag
    private static final int REQUEST_DATE = 0;//DatePickerFragment 数据返回请求码
    private static final int REQUEST_TIME = 1;//TimePickerFragment 数据返回请求码
    private static final int REQUEST_CONTACT = 2;//联系人返回码

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int REQUEST_PHOTO = 3;//打开相机


    private Crime mCrime;

    private EditText mTitleField;
    private Button mDateButton;//日期
    private Button mTimeButton;//时间
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;
    private CheckBox mSolvedCheckBox;

    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private UUID crimeId;

    private File mPhotoFile;//获取照片文件

    private PackageManager packageManager;

    private Intent cameraIntent=null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
        }
        setHasOptionsMenu(true);
        //mCrime=new Crime();
        //Intent 的Extra 已经存储了对应的crimeId 根据这个UUID 来获取对应的 Crime的对象
        //UUID crimeId  = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        //改为从fragment的argument中获取UUID,这样CrimeFragment类变得通用，而不依靠特定的Activity
        crimeId= (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime= CrimeLab.getInstance(getActivity()).getCrime(crimeId);
        mPhotoFile=CrimeLab.getInstance(getActivity()).getPhotoFile(mCrime);
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
        mReportButton=view.findViewById(R.id.report_crime);
        mSuspectButton=view.findViewById(R.id.choose_crime_suspect);
        mCallSuspectButton=view.findViewById(R.id.call_crime);
        mPhotoButton = view.findViewById(R.id.crime_camera);
        mPhotoView = view.findViewById(R.id.crime_photo);
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
        mDateButton.setOnClickListener(this);
        mTimeButton.setOnClickListener(this);
        mReportButton.setOnClickListener(this);
        mSuspectButton.setOnClickListener(this);
        mCallSuspectButton.setOnClickListener(this);
        mPhotoView.setOnClickListener(this);
        mPhotoButton.setOnClickListener(this);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        if(mCrime.getmSuspect()!=null){
           mSuspectButton.setText(mCrime.getmSuspect());
        }

        //如果找不到联系人应用，点击选择联系人按钮应用就会崩溃，使用PackageManager来检查是否有对应的Activity
        packageManager=getActivity().getPackageManager();
        if(packageManager.resolveActivity(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
                PackageManager.MATCH_DEFAULT_ONLY)==null){
             mSuspectButton.setEnabled(false);
        }
        if(mCrime.getmSuspectContact()==null){
            mCallSuspectButton.setEnabled(false);
        }else {
            mCallSuspectButton.setEnabled(true);
            mCallSuspectButton.setText(mCrime.getmSuspectContact());
        }

        //启动相机Intent
        cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //检查是否存在相机应用
        boolean canTakePhoto=mPhotoFile!=null && cameraIntent.resolveActivity(packageManager)!=null;
        mPhotoButton.setEnabled(canTakePhoto);
        //updatePhotoView();
        ViewTreeObserver viewTreeObserver = mPhotoView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updatePhotoView(mPhotoView.getWidth(), mPhotoView.getHeight());
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_item,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_crime:
                UUID id = (UUID)getArguments().getSerializable("crime_id");
                if(id!=null)
                CrimeLab.getInstance(getActivity()).deleteCrime(id);
                Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void updateDate() { //更新日期
        mDateButton.setText(MyUtils.getFormatDate(mCrime.getmDate()));
    }

    //操作完之后更新数据库
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
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
        }else if(requestCode==REQUEST_CONTACT&&data!=null){//处理联系人数据,并将选择的联系人作为嫌疑人
            Uri uri = data.getData();
            String[] queryFiles = {ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID};//查询名字 id
            Cursor cursor = getActivity().getContentResolver().query(uri, queryFiles, null, null, null);
            String suspectContactId=null;//嫌疑人id 根据这个id 再去查电话表得到嫌疑人电话
            try {
                if(cursor.getCount()==0){
                    return;
                }
                cursor.moveToFirst();
                String suspectName = cursor.getString(0);
                mCrime.setmSuspect(suspectName);
                mSuspectButton.setText(suspectName);

                suspectContactId=cursor.getString(1);
            }finally {
                cursor.close();
            }
            Cursor queryContact=null;

            queryContact = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = ?", new String[]{suspectContactId}, null);

            try{
                if(queryContact.getCount()==0){
                    return;
                }
                queryContact.moveToFirst();
                int index = queryContact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phoneNumber = queryContact.getString(index);
                mCrime.setmSuspectContact(phoneNumber);
                mCallSuspectButton.setEnabled(true);
                mCallSuspectButton.setText(phoneNumber);

            }finally {
                queryContact.close();
            }
        }else if(requestCode==REQUEST_PHOTO){//拍照返回
            /*Uri uri = FileProvider.getUriForFile(getActivity(),
                    "mao.com.maocriminalintent.fileprovider", mPhotoFile);
            //关闭文件访问
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);*/
            updatePhotoView(mPhotoView.getWidth(),mPhotoView.getHeight());
        }
    }
    //消息回复
    private String getCrimeReport(){
        String solvedString =null;
        if(mCrime.ismSolved()){
           solvedString=getString(R.string.crime_report_solved);
        }else {
            solvedString=getString(R.string.crime_report_unsolved);
        }
        String dateFromat = "EEE,MMM dd";
        String  dateStr = DateFormat.format(dateFromat, mCrime.getmDate()).toString();
        String suspect=mCrime.getmSuspect();
        if(suspect==null){
            suspect=getString(R.string.crime_report_no_suspect);
        }else {
            suspect=getString(R.string.crime_report_suspect);
        }
        String report = getString(R.string.crime_report,mCrime.getmTitle(), dateStr, solvedString, suspect);
        return report;
    }

    //设置相机拍摄的图片
    private void updatePhotoView(int width, int height){
        if(mPhotoFile==null || !mPhotoFile.exists()){
            mPhotoView.setImageBitmap(null);
        }else {
            Bitmap bitmap = MyUtils.getScaledBitmap(mPhotoFile.getPath(),width,height);
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    //权限检查 读取联系人
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            /*if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager=null;
        Intent intent=null;
        switch (view.getId()){
            case R.id.crime_date://日期设置
                fragmentManager=getFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(fragmentManager,DIALOG_DATE);
                break;
            case R.id.crime_time://时间设置
                fragmentManager=getFragmentManager();
                TimePickerFragment timePickerFragment=TimePickerFragment.newInstance(mCrime.getmDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                timePickerFragment.show(fragmentManager,DIALOG_TIME);
                break;
            case R.id.report_crime://发送报告
                /*intent=new Intent(Intent.ACTION_VIEW);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);*/
                //使用IntentBuilder来创建发消息的Intent
                ShareCompat.IntentBuilder intentBuilder= ShareCompat.IntentBuilder.from(getActivity());
                intentBuilder.setType("text/plain");
                intentBuilder.setSubject(getString(R.string.send_report));
                intentBuilder.setText(getCrimeReport());
                intentBuilder.setChooserTitle(R.string.send_report);
                intentBuilder.startChooser();
                break;
            case R.id.choose_crime_suspect://选择嫌疑人
                intent=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,REQUEST_CONTACT);
                break;
            case R.id.call_crime:
                //拨打嫌疑人电话
                intent=new Intent(Intent.ACTION_DIAL);//开启打电话应用并填好号码，待用户点击确认拨打
                intent.setData(Uri.parse("tel:"+mCrime.getmSuspectContact()));
                startActivity(intent);
                break;
            case R.id.crime_camera://拍照
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "mao.com.maocriminalintent.fileprovider", mPhotoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);//指定存储路径
                List<ResolveInfo> cameraActivities=getActivity().getPackageManager().queryIntentActivities(cameraIntent,PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity:cameraActivities) {
                    //写入权限
                   getActivity().grantUriPermission(activity.activityInfo.packageName,uri,
                           Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(cameraIntent,REQUEST_PHOTO);
                break;
            case R.id.crime_photo:
                if(mPhotoFile==null || !mPhotoFile.exists()){
                    mPhotoView.setImageBitmap(null);
                }else {
                //点击拍摄的照片，显示放大版的照片
                fragmentManager=getFragmentManager();
                PhotoFragment photoFragment=PhotoFragment.newInstance(mPhotoFile.getPath());
                photoFragment.setTargetFragment(CrimeFragment.this,REQUEST_PHOTO);
                photoFragment.show(fragmentManager,DIALOG_PHOTO);
                }
                break;
        }
    }
}
