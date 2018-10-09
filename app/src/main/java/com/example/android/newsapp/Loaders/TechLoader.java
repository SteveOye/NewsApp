package com.example.android.newsapp.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.newsapp.NewsFeed;
import com.example.android.newsapp.QueryUtils.TechUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TechLoader extends AsyncTaskLoader<List<NewsFeed>> {

    public static final String LOG_TAG = TechLoader.class.getSimpleName();

    public TechLoader(Context context) {
        super(context);
        Log.i(LOG_TAG, "THIS: TechLoader constructor called");
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "THIS: TechLoader onStartLoading() called");
        forceLoad();
    }

    @Override
    public List<NewsFeed> loadInBackground() {
        Log.i(LOG_TAG, "THIS: TechLoader loadInBackground() called");
        Log.i(LOG_TAG, "TEST:  fetchNewsFeed called");
        URL url = TechUtils.createUrl();
        String jsonResponse = null;

        try {
            jsonResponse = TechUtils.makeHttpConnection(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "fetchNewsFeed: problem fetchNewsFeed", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link NewsFeed}
        List<NewsFeed> newsFeeds = TechUtils.extractFeatureFromJson(jsonResponse);
        return newsFeeds;
    }
}