package com.example.android.newsapp.Loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.newsapp.QueryUtils.LifeStyleUtils;
import com.example.android.newsapp.NewsFeed;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class LifeStyleLoader extends AsyncTaskLoader<List<NewsFeed>> {

    public static final String LOG_TAG = LifeStyleLoader.class.getSimpleName();

    public LifeStyleLoader(Context context) {
        super(context);
        Log.i(LOG_TAG, "THIS: LifeStyleLoader constructor called");
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "THIS: LifeStyleLoader onStartLoading() called");
        forceLoad();
    }

    @Override
    public List<NewsFeed> loadInBackground() {
        Log.i(LOG_TAG, "THIS: LifeStyleLoader loadInBackground() called");
        Log.i(LOG_TAG, "TEST:  fetchNewsFeed called");
        URL url = LifeStyleUtils.createUrl();
        String jsonResponse = null;

        try {
            jsonResponse = LifeStyleUtils.makeHttpConnection(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "fetchNewsFeed: problem fetchNewsFeed", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link NewsFeed}
        List<NewsFeed> newsFeeds = LifeStyleUtils.extractFeatureFromJson(jsonResponse);
        return newsFeeds;
    }
}
