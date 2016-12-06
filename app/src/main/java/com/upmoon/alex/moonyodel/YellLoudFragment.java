package com.upmoon.alex.moonyodel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;

import java.net.URL;


//Sleep well and shout out heck

public class YellLoudFragment extends Fragment {

    private RecyclerView mMessageList;

    private SwipeRefreshLayout mListHoldyRefresher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_yell_loud, container, false);

        return v;
    }




    private class HeckHolder extends RecyclerView.ViewHolder
    {
        public HeckHolder(View itemView){
            super(itemView);

        }

        public void bindHeck(){

        }
    }

    private class HeckAdapter extends RecyclerView.Adapter<HeckHolder>{

        public HeckAdapter(){

        }

        @Override
        public HeckHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater LI = LayoutInflater.from(getActivity());
            View view = LI.inflate(R.layout.yodel_message,parent,false);
            return new HeckHolder(view);
        }

        @Override
        public void onBindViewHolder(HeckHolder holder, int position){


        }

        @Override
        public int getItemCount(){
            return 0;
        }
    }

    private class DownloadMessages extends AsyncTask<URL, Integer, Long>{
        protected Long doInBackground(URL... urls) {
            return new Long(0);
        }
    }

    private class SendMessage extends AsyncTask<URL, Integer, Long>{
        protected Long doInBackground(URL... urls) {
            return new Long(0);
        }
    }

    private class SendLike extends AsyncTask<URL, Integer, Long>{
        protected Long doInBackground(URL... urls) {
            return new Long(0);
        }
    }

    private class SendDislike extends AsyncTask<URL, Integer, Long>{
        protected Long doInBackground(URL... urls) {
            return new Long(0);
        }
    }
}
