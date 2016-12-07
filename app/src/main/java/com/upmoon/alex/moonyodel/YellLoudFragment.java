package com.upmoon.alex.moonyodel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


//Sleep well and shout out heck

public class YellLoudFragment extends Fragment {

    private static final String API_KEY = "champlainrocks1878";

    private RecyclerView mMessageList;
    private HeckAdapter mHingAdapter;

    private SwipeRefreshLayout mListHoldyRefresher;

    private ArrayList<HeckingShout> mShouts;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_yell_loud, container, false);

        mMessageList = (RecyclerView) v.findViewById(R.id.gol_recycler_view);
        mMessageList.setLayoutManager(new LinearLayoutManager(getActivity(),
                                        LinearLayoutManager.VERTICAL,false));

        mHingAdapter = new HeckAdapter();
        mMessageList.setAdapter(mHingAdapter);

        mListHoldyRefresher = (SwipeRefreshLayout) v.findViewById(R.id.str);

        mListHoldyRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new DownloadMessages().execute();

            }
        });

        return v;
    }



    /*-------------------------------------------------------------------------
    *
    * Lovingly stolen from the big nerd ranch
    * because there are only so many ways to
    * skin a fish and I don't feel like
    * making up my own
    *
    * */
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    //------------------------------------------------------------------------

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

    private class DownloadMessages extends AsyncTask<Void, Void, String>{
        protected String doInBackground(Void... hidad) {

            String strinnn = "";

            try {


                String url = Uri.parse("https://www.stepoutnyc.com/chitchat")
                        .buildUpon()
                        .appendQueryParameter("key","champlainrocks1878")
                        .build()
                        .toString();
                strinnn = getUrlString(url);



            }catch(IOException io){

            }

            return strinnn;
        }

        protected void onPostExecute(String bigolobj){

            Log.d("meme",bigolobj);

            mListHoldyRefresher.setRefreshing(false);
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
