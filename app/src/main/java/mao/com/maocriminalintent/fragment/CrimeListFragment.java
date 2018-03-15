package mao.com.maocriminalintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import mao.com.maocriminalintent.CrimePagerActivity;
import mao.com.maocriminalintent.R;
import mao.com.maocriminalintent.instance.CrimeLab;
import mao.com.maocriminalintent.model.Crime;
import mao.com.maocriminalintent.util.MyUtils;

/**
 * Created by maoqi on 2018/2/25 0025.
 *
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;


    private int currentPosition = -1;//选择操作Crime的位置
    private boolean mSubtitleVisible;//是否显示子标题

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";//屏幕旋转获取Title显示状态常量

    private RelativeLayout mEmptyLayout;//陋习为空的界面
    private Button mAddBtn;

    private CrimeLab crimeLab;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当CrimeListActivity接收到操作系统的onCreateOptionsMenu(...)方法回调请求时，
        // 我们 必须明确告诉FragmentManager：
        // 其管理的fragment应接收onCreateOptionsMenu(...)方法的 调用指令。要通知FragmentManager，
        Log.e("毛麒添","CrimeListFragment onCreate");
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView=view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyLayout=view.findViewById(R.id.rl_empty);
        mAddBtn=view.findViewById(R.id.add_button);
        if(savedInstanceState!=null){
            mSubtitleVisible=savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }
    //关联菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem item = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            item.setTitle(R.string.hide_subtitle);
        }else {
            item.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_crime:
                Crime crime=new Crime();
                CrimeLab.getInstance(getActivity()).add(crime);
                Intent intent=CrimePagerActivity.newIntent(getActivity(),crime.getmId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();//更新
                updateSubtitle();
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }
    //设置工具栏子标题 显示陋习个数
    public void updateSubtitle(){
        int size = crimeLab.getmCrimes().size();
        String string = getResources().getQuantityString(R.plurals.subtitle_plural, size,size);
        if (!mSubtitleVisible){
            string=null;//隐藏子标题
        }
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(string);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }



    public void updateUI() {
        crimeLab=CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getmCrimes();
        if(crimes.size()==0){
            mEmptyLayout.setVisibility(View.VISIBLE);
            mCrimeRecyclerView.setVisibility(View.GONE);
            mAddBtn.setOnClickListener(new View.OnClickListener() {//当数据库中没有数据的时候添加一条新数据
                @Override
                public void onClick(View view) {
                    Crime crime=new Crime();
                    CrimeLab.getInstance(getActivity()).add(crime);
                    Intent intent=CrimePagerActivity.newIntent(getActivity(),crime.getmId());
                    startActivity(intent);
                }
            });
        }else {
            mEmptyLayout.setVisibility(View.GONE);
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
        }
        if(mAdapter==null){//适配器没有则创建
            mAdapter=new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else {//否则根据更改的数据刷新 RecyclerView 刷新对应的位置
            mAdapter.setCrimes(crimes);
            if (currentPosition != -1) {
                mAdapter.notifyItemChanged(currentPosition);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
        updateSubtitle();
    }

    //旋转屏幕保存标题栏状态
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    //实现RecyclerView 的ViewHodler 和Adapter
    //普通holder
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        public CrimeHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime,parent,false));
            mTitleTextView=itemView.findViewById(R.id.crime_title);
            mDateTextView=itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime){
             mCrime=crime;
             mTitleTextView.setText(mCrime.getmTitle());
             mDateTextView.setText(MyUtils.getFormatDate(mCrime.getmDate()));
             mSolvedImageView.setVisibility(crime.ismSolved()?View.VISIBLE:View.GONE);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(getActivity(),mCrime.getmTitle() + " clicked!", Toast.LENGTH_SHORT).show();
            //Intent intent=new Intent(getActivity(), CrimeActivity.class);
            //Intent intent=CrimeActivity.newIntent(getActivity(),mCrime.getmId());
            Intent intent= CrimePagerActivity.newIntent(getActivity(),mCrime.getmId());
            Bundle args = new Bundle();
            args.putSerializable("crime_id",mCrime.getmId());
                        CrimeListFragment listFragment = new CrimeListFragment();
                       listFragment.setArguments(args);
            startActivity(intent);
            currentPosition=this.getAdapterPosition();
        }
    }
    //需要联系警方的holder
    private class CrimeHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        public CrimeHolder2(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime_2,parent,false));
            mTitleTextView=itemView.findViewById(R.id.crime_title_2);
            mDateTextView=itemView.findViewById(R.id.crime_date_2);
            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime){
            mCrime=crime;
            mTitleTextView.setText(mCrime.getmTitle());
            mDateTextView.setText(MyUtils.getFormatDate(mCrime.getmDate()));
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),mCrime.getmTitle() + " clicked2!", Toast.LENGTH_SHORT).show();
            /*Intent intent=CrimeActivity.newIntent(getActivity(),mCrime.getmId());
            startActivity(intent);*/
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
              mCrimes=crimes;
        }

        public void setCrimes(List<Crime> crimes){
            this.mCrimes=crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            if(viewType==0){
                return new CrimeHolder(layoutInflater,parent);
            }else {
                return new CrimeHolder2(layoutInflater,parent);
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if (holder instanceof CrimeHolder) {
                ((CrimeHolder)holder).bind(crime);
            } else if (holder instanceof CrimeHolder2) {
                ((CrimeHolder2)holder).bind(crime);
            }
        }


        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            if (crime.getmRequiresPolice()==0){
                return 0;
            }else {
                return 1;
            }
        }
    }
}
