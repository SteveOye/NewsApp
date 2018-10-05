package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static final String LOG_TAG = Utils.class.getSimpleName();

    public static List<NewsFeed> fetchNewsFeed(String requestUrl){
        Log.i(LOG_TAG, "TEST:  fetchNewsFeed called");
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpConnection(url);
        } catch (IOException e) {
            Log.e(TAG, "fetchNewsFeed: problem fetchNewsFeed", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link NewsFeed}
        List<NewsFeed> newsFeeds = extractFeatureFromJson(jsonResponse);
        return newsFeeds;
    }

    /*
    * Convert the string to URL
    * to establish the connection.
     */
    private static URL createUrl(String stringUrl){
        Log.i(LOG_TAG, "TEST:  createUrl() called");
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createUrl: Error creating URL", e);
        }
        return url;
    }

    /*
    * Establishing the connection with the URL provided
     */
    private static String makeHttpConnection(URL url) throws IOException {
        Log.i(LOG_TAG, "TEST:  makeHttpConnection() called");
        String jsonResponse = " ";

        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpConnection: problem making connection", e);;
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // reading the data from the url
    private static String readFromStream(InputStream inputStream) throws IOException {
        Log.i(LOG_TAG, "TEST:  readFromStream() called");
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    //extract the relevant feed needed in the listView
    private static List<NewsFeed> extractFeatureFromJson(String jsonResponse) {
        Log.i(LOG_TAG, "TEST:  extractFeatureFromJson() called");

        List<NewsFeed> newsFeeds = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            //Create the JSONObject with the key "response"
            JSONObject responseJSONObject = baseJsonResponse.getJSONObject("response");
            //JSONObject responseJSONObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of news stories.
            JSONArray newsFeedArray = responseJSONObject.getJSONArray("results");


            for (int i = 0; i<= newsFeedArray.length(); i++){
                JSONObject currentNewsFeed = newsFeedArray.getJSONObject(i);
                JSONObject fields = currentNewsFeed.getJSONObject("fields");
                String image = fields.getString("thumbnail");
                String webTitle = currentNewsFeed.getString("webTitle");
                String date = currentNewsFeed.getString("webPublicationDate");
                String url = currentNewsFeed.getString("webUrl");
                NewsFeed newsFeed = new NewsFeed(image,webTitle,date,url);
                newsFeeds.add(newsFeed);
            }
        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the NewsFeed JSON results", e);
        }
        return newsFeeds;
    }
}