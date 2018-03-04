package mao.com.maocriminalintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import mao.com.maocriminalintent.CrimeActivity;
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

    private static final int REQUEST_CRIME = 1;//CrimeFragment 处理结果返回状态码,根据状态码判断对应的Crime对象进行高效刷新RecyclerView

    private int position;//更改对应Item 的位置
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView=view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CRIME){
           position= (int) data.getSerializableExtra("mPosition");
        }
    }

    private void updateUI() {
        CrimeLab crimeLab=CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getmCrimes();
        if(mAdapter==null){//适配器没有则创建
            mAdapter=new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else {//否则根据更改的数据刷新 RecyclerView 刷新对应的位置
            //mAdapter.notifyDataSetChanged();
            mAdapter.notifyItemChanged(position);
        }

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
            startActivityForResult(intent,REQUEST_CRIME);
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
