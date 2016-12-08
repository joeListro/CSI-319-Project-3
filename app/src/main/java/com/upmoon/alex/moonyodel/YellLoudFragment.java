package com.upmoon.alex.moonyodel;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Button;
import android.widget.EditText;
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


/*
* I swear on me mum this is all my work sans cited material
*
* Hi Daivd, this might not be the most pretty object oriented
* project we've made but I think this kind of compactness for
* a project like this is super pretty and cool so please don't
* grade it too harshly
*
* - Alex
*
* */


/**
 * YellLoudFragment
 *      Fragment that handle's the UI lifecycle.
 */
public class YellLoudFragment extends Fragment {

	/** Declare static final variables for the server connection */
    private static final String API_KEY = "champlainrocks1878";
    private static final String CLIENT_YOU_BETTER_WANT_IT = "Boingo's big client bango";

    //Array indicies for the gps coords in message json
    private static final int LAT = 0, LON = 1;

    private RecyclerView mMessageList;
    private HeckAdapter mHingAdapter;

	private EditText mMessageBox;

	/** Refresh the layout on user down-swipe */
    private SwipeRefreshLayout mListHoldyRefresher;

	/** List Array to hold the HeckingShout ChitChat-message container objects */
    private ArrayList<HeckingShout> mShouts;

    private LocationManager mLM;

    private double mLongitude, mLatitude;

    /**************************************************************************
     * Reqired onCreate Override
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

	/**************************************************************************
	 * Create the Main Fragment View
	 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** Inflate the layout for this fragment */
        View v = inflater.inflate(R.layout.fragment_yell_loud, container, false);

        mMessageList = (RecyclerView) v.findViewById(R.id.gol_recycler_view);
        mMessageList.setLayoutManager(new LinearLayoutManager(getActivity(),
                                        LinearLayoutManager.VERTICAL,false));

        mHingAdapter = new HeckAdapter();
        mMessageList.setAdapter(mHingAdapter);

        mListHoldyRefresher = (SwipeRefreshLayout) v.findViewById(R.id.str);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        updateLocation();

        //Log.d("LOCATION", Double.toString(mLongitude) + "," + Double.toString(mLatitude));

        //http://stackoverflow.com/questions/12704009/setting-the-orientation-for-only-1-fragment-in-my-activity-while-the-rest-is-in
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/**********************************************************************
		 * Create a listener to refresh the view layout on user down-swipe.
		 */
        mListHoldyRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new DownloadMessages().execute();

            }
        });

		/**********************************************************************
		 *	Create a listener to submit the text in the EditText box as a new
		 *		ChitChat message.
		 */
        mMessageBox = (EditText) v.findViewById(R.id.editText);
        mMessageBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //Log.d("keycode",Integer.toString(i));
                if(i == 5){
                    mListHoldyRefresher.setRefreshing(true);
                    new SendMessage().execute(textView.getText().toString());
                    textView.setText("Message");
                }

                return false;
            }
        });

		/** Refresh the view after the addition of a new message */
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


    //http://stackoverflow.com/questions/33865445/gps-location-provider-requires-access-fine-location-permission-for-android-6-0
    private void updateLocation(){
        Log.d("hi","hi");
        if(getActivity().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            Log.d("hi","hi");
            mLM = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            //gets last known location, low energy use, low effort
            Location location = mLM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //handles mildy rare case where device has no known last location
            if(!(location == null)) {
                mLongitude = location.getLongitude();
                mLatitude = location.getLatitude();
            } else {
                mLongitude = 0;
                mLatitude = 0;
            }
        }
    }

	/**************************************************************************
	 * HeckHolder
	 * 		- RecyclerView Holder for the array of HeckingShout objects (messages)
	 */
    private class HeckHolder extends RecyclerView.ViewHolder
    {
        private int mHeck;

        private TextView mIdView, mTimeView, mContentView, mGPSView, mLikesView;

        private String mID;

        private Button mLike, mDislike;


		/**********************************************************************
		 * HeckHolder Constructor
		 * 		- Initialize HeckHolder
		 *
		 * @param itemView
		 */
        public HeckHolder(View itemView){
            super(itemView);

			mTimeView = (TextView) itemView.findViewById(R.id.text_time);
            mContentView = (TextView) itemView.findViewById(R.id.text_content);

            mLikesView = (TextView) itemView.findViewById(R.id.ldamount);
            mGPSView = (TextView) itemView.findViewById(R.id.gpsloc);

            mLike = (Button) itemView.findViewById(R.id.like);

            mLike.setBackgroundResource(android.R.drawable.btn_default);
			/** Listen for like button presses and SendLike for each press */
            mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!(mShouts.get(mHeck).isHasFelt())) {
                        new SendLike().execute(mID);
                        mShouts.get(mHeck).setmLDlazyInt(1);
                        mLike.setBackgroundColor(Color.parseColor("Yellow"));
                        mShouts.get(mHeck).setLikes(mShouts.get(mHeck).getLikes() + 1);
                        mLikesView.setText("L: " + Integer.toString(mShouts.get(mHeck).getLikes()) + " D: " + Integer.toString(mShouts.get(mHeck).getDislikes()));
                        mShouts.get(mHeck).hasFelt();
                    } else {
                        Toast.makeText(getActivity(), "You already chose dummy!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mDislike = (Button) itemView.findViewById(R.id.dislike);
			/** Listen for dislike button presses and SendDislike for each press */
            mDislike.setBackgroundResource(android.R.drawable.btn_default);
            mDislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!(mShouts.get(mHeck).isHasFelt())) {
                        new SendDislike().execute(mID);
                        mShouts.get(mHeck).setmLDlazyInt(2);
                        mDislike.setBackgroundColor(Color.parseColor("Yellow"));
                        mShouts.get(mHeck).setDislikes(mShouts.get(mHeck).getDislikes() + 1);
                        mLikesView.setText("L: " + Integer.toString(mShouts.get(mHeck).getLikes()) + " D: " + Integer.toString(mShouts.get(mHeck).getDislikes()));
                        mShouts.get(mHeck).hasFelt();
                    } else {
                        Toast.makeText(getActivity(), "You already chose dummy!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

		/**********************************************************************
		 * bindHeck
		 * 		- Create a new message in the mShouts array from a HeckingShout object.
		 *
		 * @param pos	- The index after the last populated index in mShouts.
		 */
        public void bindHeck(int pos){

            mHeck = pos;

            HeckingShout hs = mShouts.get(pos);

            mID = hs.getMessageID();

            mTimeView.setText(hs.getMessageTimestamp());
            mContentView.setText(hs.getMessageContent());

            // Absolutely poor show solution to a problem I didn't know could happen
            if(!(mShouts.get(mHeck).isHasFelt())) {
                mLike.setBackgroundResource(android.R.drawable.btn_default);
                mDislike.setBackgroundResource(android.R.drawable.btn_default);
            } else if(mShouts.get(mHeck).getmLDlazyInt() == 1){
                mLike.setBackgroundColor(Color.parseColor("Yellow"));
            } else if(mShouts.get(mHeck).getmLDlazyInt() == 2){
                mDislike.setBackgroundColor(Color.parseColor("Yellow"));
            }


            mLikesView.setText("L: " + Integer.toString(mShouts.get(mHeck).getLikes()) + " D: " + Integer.toString(mShouts.get(mHeck).getDislikes()));

            if(hs.isHasLoc()){
                mGPSView.setText("Lat: " + Double.toString(hs.getLat()) + " Lon: " + Double.toString(hs.getLon()));
            }
        }
    }

	/**************************************************************************
	 * HeckAdapter
	 * 		- Populate the fragment view with the messages in HeckingShouts from HeckHolder
	 */
    private class HeckAdapter extends RecyclerView.Adapter<HeckHolder>{

		/** Empty Default Constructor */
        public HeckAdapter(){

        }

		/** Upon creation, inflate the view */
        @Override
        public HeckHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater LI = LayoutInflater.from(getActivity());
            View view = LI.inflate(R.layout.yodel_message,parent,false);
            return new HeckHolder(view);
        }

		/** Bing position index in mShouts to HeckHolder holder */
        @Override
        public void onBindViewHolder(HeckHolder holder, int position){
            holder.bindHeck(position);
        }

		/** Return the number of messages (HeckingShouts) in mShouts array */
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

	/**************************************************************************
	 * Download Messages from the server in the background asynchronously
	 */
    private class DownloadMessages extends AsyncTask<Void, Void, String>{
        protected String doInBackground(Void... hidad) {
            try {
                String url = Uri.parse("https://www.stepoutnyc.com/chitchat")
                        .buildUpon()
                        .appendQueryParameter("key",API_KEY)
                        .appendQueryParameter("limit","60")
                        .build()
                        .toString();
                String strinnn = getUrlString(url);
                //Log.d("JSON MESSAGES BODY",strinnn);
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

                    JSONArray gpscoords = shout.getJSONArray("loc");

                    if(!(gpscoords.isNull(LAT) || gpscoords.isNull(LON))){
                        iwannashoutoutheck.setLat(gpscoords.getDouble(LAT));
                        iwannashoutoutheck.setLon(gpscoords.getDouble(LON));
                        iwannashoutoutheck.hasLoc();
                    }

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

	/**************************************************************************
	 * Yell a HeckingShout to the server in the background asynchronously
	 */
    private class SendMessage extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... message) {
            updateLocation();
            try {
                /* Build URL string, strUrl */
                String urlSpec = Uri.parse("https://www.stepoutnyc.com/chitchat")
                        .buildUpon()
                        .appendQueryParameter("key",API_KEY)
                        .appendQueryParameter("message",message[0])
                        .appendQueryParameter("client",CLIENT_YOU_BETTER_WANT_IT)
                        .appendQueryParameter("lat",Double.toString(mLatitude))
                        .appendQueryParameter("lon",Double.toString(mLongitude))
                        .build()
                        .toString();

                URL url = new URL(urlSpec);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
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
                    return new String(out.toByteArray());
                } finally {
                    connection.disconnect();
                }
            } catch(IOException io) {
                // IOException thrown
                //Log.d("YellLoudFragment:","IOException thrown.");
            }

            return new String();
        }

        protected void onPostExecute(String str){

            new DownloadMessages().execute();

            mListHoldyRefresher.setRefreshing(true);
        }
    }

	/**************************************************************************
	 * SendLike
	 * 		- Notify the server about a message like.
	 */
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
                //Log.d("JSON MESSAGES BODY",strinnn);
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
            //Toast.makeText(getActivity(), "Like: " + result, Toast.LENGTH_SHORT).show();
        }
    }

	/**************************************************************************
	 * SendDislike
	 * 		- Notify the server about a message dislike.
	 */
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
                //Log.d("JSON MESSAGES BODY",strinnn);

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
            //Toast.makeText(getActivity(), "Dislike: " + result, Toast.LENGTH_SHORT).show();
        }
    }
}
