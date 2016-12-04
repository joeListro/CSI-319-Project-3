package com.example.josephlistro.csi_319_project_3;

/**
 * Created by joseph.listro on 12/1/2016.
 */

public class ChitChatMessage {
    // Container for messages
    private String mMessage;
    private String mClient;
    private String mUrl;

    public String getMessage() {
        return mMessage;
    }

    public String getClient() {
        return mClient;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setClient(String client) {
        mClient = client;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
