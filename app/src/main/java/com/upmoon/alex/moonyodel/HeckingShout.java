package com.upmoon.alex.moonyodel;

/**
 * Created by Alex on 12/6/2016.
 */

/**
 *  HeckingShout Value Object that will store the Chit Chat Messages
 */
public class HeckingShout {
    private String mMessageID;
    private String mTimestamp;
    private String mMessageContent;
    private int mLikes;
    private int mDislikes;
    private int mLat;
    private int mLon;

	/**************************************************************************
     * Default Constructor
     */
    public HeckingShout(){}

	/**************************************************************************
     * Constructor for HeckingShout Object
     *
     * @param id        - The unique ID of the ChitChat message
     * @param time      - Timestamp of when the message was created
     * @param content   - The message body
     * @param likes     - Number of likes the message has received
     * @param dislikes  - Number of likes the message has received
     */
    public HeckingShout(String id, String time, String content, int likes, int dislikes)
    {
        mMessageID = id;
        mTimestamp = time;
        mMessageContent = content;
        mLikes = likes;
        mDislikes = dislikes;
    }

	/**************************************************************************
     * getMessageID
     *      - Get HeckingObject's ChitChat message's unique ID.
     *
     * @return  - The message's unique ID
     */
    public String getMessageID()
    {
        return mMessageID;
    }

	/**************************************************************************
     * setMessageID
     *      - Set HeckingObject's ChitChat message's unique ID.
     *
     * @param id        - A unique ChitChat message ID
     */
    public void setMessageID(String id) {
        mMessageID = id;
    }

	/**************************************************************************
     * getMessageTimestamp
     *      - Get HeckingObject's ChitChat message's timestamp.
     *
     * @return          - The message's timestamp.
     */
    public String getMessageTimestamp()
    {
        return mTimestamp;
    }

	/**************************************************************************
     * setMessageTimestamp
     *      - Set HeckingObject's ChitChat message's timestamp.
     *
     * @param time      - The timestamp when the message was created.
     */
    public void setMessageTimestamp(String time)
    {
        mTimestamp = time;
    }

	/**************************************************************************
     * getMessageContent
     *      - Get HeckingObject's ChitChat message's body.
     *
     * @return          - The message's content / body
     */
    public String getMessageContent()
    {
        return mMessageContent;
    }

	/**********************************************************************
     * setMessageContent
     *      - Set HeckingObject's ChitChat message's body.
     *
     * @param content   - The message body's text content.
     */
    public void setMessageContent(String content)
    {
        mMessageContent = content;
    }

	/**************************************************************************
     * getLikes
     *      - Get HeckingObject's ChitChat message's number of likes.
     *
     * @return          - The message's number of likes.
     */
    public int getLikes()
    {
        return mLikes;
    }

	/**************************************************************************
     * setLikes
     *      - Set HeckingObject's ChitChat message's number of likes.
     *
     * @param likes     - The message's number of likes.
     */
    public void setLikes(int likes)
    {
        mLikes = likes;
    }

    /**************************************************************************
     * getDislikes
     *      - Get HeckingObject's ChitChat message's number of dislikes.
     *
     * @return          - The message's number of dislikes.
     */
    public int getDislikes()
    {
        return mDislikes;
    }

	/**************************************************************************
     * setDislikes
     *      - Set HeckingObject's ChitChat message's number of dislikes.
     *
     * @param dislikes  -  The message's number of dislikes.
     */
    public void setDislikes(int dislikes)
    {
        mDislikes = dislikes;
    }

    /**************************************************************************
     * getLat
     *      - Get HeckingObject's ChitChat message's latitude.
     *
     * @return          - The message's latitude.
     */
    public int getLat() { return mLat; }

	/**************************************************************************
     * setLat
     *      - Set HeckingObject's ChitChat message's latitude.
     *
     * @param lat       - The message's latitude.
     */
    public void setLat(int lat) { mLat = lat; }

    /**************************************************************************
     * getLon
     *      - Get HeckingObject's ChitChat message's longitude.
     *
     * @return          - The message's longitude.
     */
    public int getLon() { return mLon; }

	/**************************************************************************
     * setLon
     *      - Set HeckingObject's ChitChat message's longitude.
     *
     * @param lon       - The message's longitude.
     */
    public void setLon(int lon) { mLon = lon; }

	/**************************************************************************
     * toString
     *      - Return a String containing the message content of
     *          this HeckingObject.
     *
     * @return          - Message's content.
     */
    @Override
    public String toString()
    {
        return mMessageContent;
    }
}
