package com.example.android.newsapp;

public class NewsFeed {

    private String mImageUrl;
    private String mHeadLine;
    private String mPublicationDate;
    private String mUrl;
    private String mAuthorName;

    public NewsFeed(String mImageUrl, String mHeadLine, String mPublicationDate, String mAuthorName, String mUrl) {
        this.mImageUrl = mImageUrl;
        this.mHeadLine = mHeadLine;
        this.mPublicationDate = mPublicationDate;
        this.mUrl = mUrl;
        this.mAuthorName = mAuthorName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getHeadLine() {
        return mHeadLine;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getUrl() {
        return mUrl;
    }
}
