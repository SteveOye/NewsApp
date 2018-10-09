package com.example.android.newsapp.QueryUtils;

import android.net.Uri;
import android.util.Log;

import com.example.android.newsapp.NewsFeed;

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

public final class NewsUtils {
    private static final String TAG = NewsUtils.class.getSimpleName();
    private static final String LOG_TAG = NewsUtils.class.getSimpleName();
    private static final int MAX_CONNECTION_TIMEOUT= 15000;
    private static final int MAX_READ_TIME= 10000;

    public static String StringUrlBuilder(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .encodedAuthority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("order-by", "newest")
                .appendQueryParameter("page-size", "20")
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("show-fields", "thumbnail")
                .appendQueryParameter("q", "")
                .appendQueryParameter("api-key", "3f439ff9-4754-4903-b0d7-ab8e582d2225");
        String url = builder.build().toString();
        return url;
    }

    /*
    * Convert the string to URL
    * to establish the connection.
     */
    public static URL createUrl(){
        Log.i(LOG_TAG, "TEST:  createUrl() called");
        String stringUrl = StringUrlBuilder();
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "createUrl: Error creating URL", e);
            return null;
        }
    }

    /*
    * Establishing the connection with the URL provided
     */
    public static String makeHttpConnection(URL url) throws IOException {
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
            urlConnection.setConnectTimeout(MAX_CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(MAX_READ_TIME);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == urlConnection.HTTP_OK){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpConnection: problem making connection", e);
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
    public static List<NewsFeed> extractFeatureFromJson(String jsonResponse) {
        Log.i(LOG_TAG, "TEST:  extractFeatureFromJson() called");

        List<NewsFeed> newsFeeds = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            //Create the JSONObject with the key "response"
            JSONObject responseJSONObject = baseJsonResponse.getJSONObject("response");
            // Extract the JSONArray associated with the key called "results",
            // which represents a list of news stories.
            JSONArray newsFeedArray = responseJSONObject.getJSONArray("results");

            for (int i = 0; i< newsFeedArray.length(); i++){
                JSONObject currentNewsFeed = newsFeedArray.getJSONObject(i);
                JSONObject fields = currentNewsFeed.getJSONObject("fields");
                String image = fields.getString("thumbnail");
                String webTitle = currentNewsFeed.getString("webTitle");
                String date = currentNewsFeed.getString("webPublicationDate");
                String url = currentNewsFeed.getString("webUrl");

                //Extract the JSONArray with the key "tag"
                JSONArray tagsArray = currentNewsFeed.getJSONArray("tags");
                //Declare String variable to hold author name
                String author = " ";
                if (tagsArray.length() == 0) {
                    author = null;
                } else {
                    for (int j = 0; j<tagsArray.length();j++) {
                        JSONObject contributorTag = tagsArray.getJSONObject(j);
                        author = contributorTag.getString("webTitle");
                    }
                }
                NewsFeed newsFeed = new NewsFeed(image,webTitle,date,author,url);
                newsFeeds.add(newsFeed);
            }
        } catch (JSONException e) {
            Log.e("NewsUtils", "Problem parsing the NewsFeed JSON results", e);
        }
        return newsFeeds;
    }
}