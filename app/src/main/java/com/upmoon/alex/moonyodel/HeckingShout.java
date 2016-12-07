package com.upmoon.alex.moonyodel;

/**
 * Created by Alex on 12/6/2016.
 */

public class HeckingShout {
    private String mMessageID;
    private String mTimestamp;
    private String mMessageContent;
    private int mLikes;
    private int mDislikes;
    private int mLat;
    private int mLon;

    public HeckingShout(){}

    public HeckingShout(String id, String time, String content, int likes, int dislikes)
    {
        mMessageID = id;
        mTimestamp = time;
        mMessageContent = content;
        mLikes = likes;
        mDislikes = dislikes;
    }

    public String getMessageID()
    {
        return mMessageID;
    }

    public void setMessageID(String id) {
        mMessageID = id;
    }

    public String getMessageTimestamp()
    {
        return mTimestamp;
    }

    public void setMessageTimestamp(String time)
    {
        mTimestamp = time;
    }

    public String getMessageContent()
    {
        return mMessageContent;
    }

    public void setMessageContent(String content)
    {
        mMessageContent = content;
    }

    public int getLikes()
    {
        return mLikes;
    }

    public void setLikes(int likes)
    {
        mLikes = likes;
    }

    public int getDislikes()
    {
        return mDislikes;
    }

    public void setDislikes(int dislikes)
    {
        mDislikes = dislikes;
    }

    public int getLat() { return mLat; }

    public void setLat(int lat) { mLat = lat; }

    public int getLon() { return mLon; }

    public void setLon(int lon) { mLon = lon; }

    @Override
    public String toString()
    {
        return mMessageContent;
    }
}
