package com.example.android.newsapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.newsapp.NewsFeed;
import com.example.android.newsapp.NewsFeedAdapter;
import com.example.android.newsapp.NewsFeedLoader;
import com.example.android.newsapp.R;

import java.util.ArrayList;
import java.util.List;

public class SportFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsFeed>> {

    ListView listView;
    TextView mEmptyStateTextView;
    ProgressBar loadingIndicator;

    private NewsFeedAdapter mAdapter;
    private static int LOADER_ID = 0;
    public static final String NEWS_URL =
            "https://content.guardianapis.com/search?order-by=newest&use-date=published&show-fields=thumbnail&page-size=20&q=sport&api-key=3f439ff9-4754-4903-b0d7-ab8e582d2225";
    public static final String LOG_TAG = SportFragment.class.getName();

    public SportFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        listView = view.findViewById(R.id.listView);
        mEmptyStateTextView = view.findViewById(R.id.no_data);

        listView.setEmptyView(mEmptyStateTextView);
        mAdapter = new NewsFeedAdapter(getActivity(), new ArrayList<NewsFeed>());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeed currentNewsFeed = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsFeedUri = Uri.parse(currentNewsFeed.getUrl());
                if (newsFeedUri != null) {

                    // Create a new intent to view the NewsFeed URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsFeedUri);
                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
        return view;
    }

    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: SportFragment onCreateLoader() called");
        return new NewsFeedLoader(getActivity(), NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsFeed>> loader, List<NewsFeed> data) {
        Log.i(LOG_TAG, "TEST: SportFragment onLoaderFinished() called");
        // Clear the adapter of previous newsfeed data
        mAdapter.clear();
        View loadingIndicator = getActivity().findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);
        // If there is a valid list of {@link NewsFeed}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            // Set empty state text to display "No NewsFeed found."
            mEmptyStateTextView.setText(R.string.no_news_feed_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsFeed>> loader) {
        Log.i(LOG_TAG, "TEST: SportFragment onLoaderReset() called");
        mAdapter.clear();
    }
}
