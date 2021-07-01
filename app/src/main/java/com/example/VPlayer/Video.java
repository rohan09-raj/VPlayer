package com.example.VPlayer;

import android.net.Uri;

public class Video {

    String mVideoTitle;
    String mVideoDuration;
    Uri mVideoUri;

    public String getVideoTitle() {
        return mVideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.mVideoTitle = videoTitle;
    }

    public String getVideoDuration() {
        return mVideoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.mVideoDuration = videoDuration;
    }

    public Uri getVideoUri() {
        return mVideoUri;
    }

    public void setVideoUri(Uri videoUri) {
        this.mVideoUri = videoUri;
    }

}