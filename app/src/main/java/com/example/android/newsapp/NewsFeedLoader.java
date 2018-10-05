package com.example.android.newsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class NewsFeedLoader extends AsyncTaskLoader<List<NewsFeed>> {

    String mUrl;
    public static final String LOG_TAG = NewsFeedLoader.class.getSimpleName();

    public NewsFeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.i(LOG_TAG, "THIS:NEwsFeedLoader constructor called");
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "THIS: NewsFeedLoader onStartLoading() called");
        forceLoad();
    }

    @Override
    public List<NewsFeed> loadInBackground() {
        Log.i(LOG_TAG, "THIS: NewsFeedLoader loadInBackground() called");
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl== null) {
            return null;
        }

        return Utils.fetchNewsFeed(mUrl);
    }

}
