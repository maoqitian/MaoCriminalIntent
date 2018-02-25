package mao.com.maocriminalintent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mao.com.maocriminalintent.R;
import mao.com.maocriminalintent.instance.CrimeLab;
import mao.com.maocriminalintent.model.Crime;

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

    private class CrimeHolder extends RecyclerView.ViewHolder{

        public CrimeHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime,parent));
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
              mCrimes=crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
