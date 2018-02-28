package mao.com.maocriminalintent.fragment;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView=view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab=CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getmCrimes();
        mAdapter=new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
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
            mSolvedImageView = (ImageView) itemView.findViewById(R.id.crime_solved);
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
            Toast.makeText(getActivity(),mCrime.getmTitle() + " clicked!", Toast.LENGTH_SHORT).show();
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
