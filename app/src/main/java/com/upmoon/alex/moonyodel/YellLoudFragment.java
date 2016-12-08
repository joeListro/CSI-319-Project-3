package com.upmoon.alex.moonyodel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        mListHoldyRefresher.setRefreshing(true);
        new DownloadMessages().execute();

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
        private TextView mIdView, mTimeView, mContentView;

        String mID;

        private Button mLike, mDislike;



        public HeckHolder(View itemView){
            super(itemView);



            //mIdView = (TextView) itemView.findViewById(R.id.text_id);
            mTimeView = (TextView) itemView.findViewById(R.id.text_time);
            mContentView = (TextView) itemView.findViewById(R.id.text_content);

            mLike = (Button) itemView.findViewById(R.id.like);
            mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new SendLike().execute(mID);
                }
            });
            mDislike = (Button) itemView.findViewById(R.id.dislike);
            mDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new SendDislike().execute(mID);
                }
            });

        }

        public void bindHeck(int pos){

            HeckingShout hs = mShouts.get(pos);

            mID = hs.getMessageID();

            //mIdView.setText(hs.getMessageID());
            mTimeView.setText(hs.getMessageTimestamp());
            mContentView.setText(hs.getMessageContent());
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

            holder.bindHeck(position);
        }

        @Override
        public int getItemCount(){
            if(mShouts != null){
                return mShouts.size();
            }
            else{
                return 0;
            }
        }
    }

    private class DownloadMessages extends AsyncTask<Void, Void, String>{
        protected String doInBackground(Void... hidad) {

            try {
                String url = Uri.parse("https://www.stepoutnyc.com/chitchat")
                        .buildUpon()
                        .appendQueryParameter("key","champlainrocks1878")
                        .build()
                        .toString();
                String strinnn = getUrlString(url);
                Log.d("JSON MESSAGES BODY",strinnn);
                JSONObject json = new JSONObject(strinnn);

                JSONArray shouts = json.getJSONArray("messages");

                mShouts = new ArrayList<HeckingShout>();

                for(int i = 0; i < shouts.length(); i++){
                    JSONObject shout = shouts.getJSONObject(i);

                    HeckingShout iwannashoutoutheck = new HeckingShout();

                    iwannashoutoutheck.setMessageID       (shout.getString("_id"));
                    iwannashoutoutheck.setMessageTimestamp(shout.getString("date"));
                    iwannashoutoutheck.setMessageContent  (shout.getString("message"));
                    iwannashoutoutheck.setLikes           (shout.getInt("likes"));
                    iwannashoutoutheck.setDislikes        (shout.getInt("dislikes"));

                    mShouts.add(iwannashoutoutheck);
                }

            }catch(JSONException je){

            }catch(IOException io){

            }
            return new String();
        }

        protected void onPostExecute(String strin){
            mHingAdapter.notifyDataSetChanged();

            mListHoldyRefresher.setRefreshing(false);
        }
    }

    /* Yell a HeckingShout to the server */
    private class SendMessage extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... id) {
            try {
                /* Build new JSONObject from the HeckingShout */
                JSONObject newShout = new JSONObject();
                HeckingShout shout = mShouts.get(Integer.getInteger(id[0]));
                newShout.put("_id", shout.getMessageID());
                newShout.put("date", shout.getMessageTimestamp());
                newShout.put("message", shout.getMessageContent());
                newShout.put("likes", 0);
                newShout.put("dislikes", 0);

                /* Build URL string, strUrl */
                String url = Uri.parse("https://www.stepoutnyc.com/chitchat")
                        .buildUpon()
                        .appendQueryParameter("key","champlainrocks1878")
                        .build()
                        .toString();
                String strUrl = getUrlString(url);
                Log.d("JSON MESSAGES BODY", strUrl);

                /* Load the JSONArray of existing HeckingShouts from the URL string, strUrl */
                JSONObject json = new JSONObject(strUrl);
                JSONArray shouts = json.getJSONArray("messages");

                /* Yell the new HeckingShout into the existing JSONArray  on the server */
                shouts.put(newShout);

            } catch(JSONException je){
                // JSONException thrown
                Log.d("YellLoudFragment:", "JSONException thrown.");
            } catch(IOException io) {
                // IOException thrown
                Log.d("YellLoudFragment:","IOException thrown.");
            }

            return new String();
        }

        protected void onPostExecute(String str){
            mHingAdapter.notifyDataSetChanged(); // Does this refresh the local feed?
            mListHoldyRefresher.setRefreshing(false);
        }
    }

    private class SendLike extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... id) {

            String status = "";
            try {
                String url = Uri.parse("https://www.stepoutnyc.com/chitchat/like/" + id[0])
                        .buildUpon()
                        .appendQueryParameter("key","champlainrocks1878")
                        .build()
                        .toString();
                String strinnn = getUrlString(url);
                Log.d("JSON MESSAGES BODY",strinnn);
                JSONObject json = new JSONObject(strinnn);

                status = json.getString("message");

            }catch(JSONException je){

                status = "JSONException";

            }catch(IOException io){

                status = "IOException";
            }

            return status;
        }

        protected void onPostExecute(String result){
            Toast.makeText(getActivity(), "Like: " + result, Toast.LENGTH_SHORT).show();
        }
    }

    private class SendDislike extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... id) {

            String status = "";
            try {
                String url = Uri.parse("https://www.stepoutnyc.com/chitchat/dislike/" + id[0])
                        .buildUpon()
                        .appendQueryParameter("key","champlainrocks1878")
                        .build()
                        .toString();
                String strinnn = getUrlString(url);
                Log.d("JSON MESSAGES BODY",strinnn);
                JSONObject json = new JSONObject(strinnn);

                status = json.getString("message");

            }catch(JSONException je){

                status = "JSONException";

            }catch(IOException io){

                status = "IOException";
            }

            return status;
        }

        protected void onPostExecute(String result){
            Toast.makeText(getActivity(), "Dislike: " + result, Toast.LENGTH_SHORT).show();
        }
    }
}
