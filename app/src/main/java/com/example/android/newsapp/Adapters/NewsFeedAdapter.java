package com.example.android.newsapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.NewsFeed;
import com.example.android.newsapp.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {

    private static final String TAG = NewsFeedAdapter.class.getSimpleName();

    public NewsFeedAdapter(Context context, ArrayList<NewsFeed> newsFeed) {
        super(context, 0, newsFeed);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.content_layout, parent, false);
        }
        NewsFeed currentNewsFeed = getItem(position);

        /*init the imageView
         * using picasso to download the image from the url
         * and set it to the view.
         */
        ImageView articleImage = listItemView.findViewById(R.id.article_image);
        Picasso.get()
                .load(currentNewsFeed.getImageUrl())
                .placeholder(R.drawable.image_placeholder)
                .into(articleImage);

        TextView articleTitle = listItemView.findViewById(R.id.article_title);
        articleTitle.setText(currentNewsFeed.getHeadLine());

        TextView authorName = listItemView.findViewById(R.id.author_name);
        authorName.setText(currentNewsFeed.getAuthorName());

        /*
         * init the textView
         * format the time gotten from the URI
         * and format it.
         */
        TextView articleDate = listItemView.findViewById(R.id.article_date);
        articleDate.setText(formatDate(currentNewsFeed.getPublicationDate()));

        //returns back the listView
        return listItemView;
    }

    /*
     * Return the formatted date string
     */
    private static String formatDate(String dataObject) {
        String jsonDatePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonFormatter = new SimpleDateFormat(jsonDatePattern, Locale.US);
        try {
            Date parsedJsonDate = jsonFormatter.parse(dataObject);
            String finalDatePattern = "EEE, MMM d, ''yyyy, h:mm a";
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.US);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing JSON date: ", e);
            return "Error!!!!";
        }
    }
}
