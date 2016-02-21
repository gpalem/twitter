package com.codepath.apps.simpletwitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gpalem on 2/19/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get Tweet
        //Find or inflate template
        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvUser = (TextView) convertView.findViewById(R.id.tvUser);
        TextView tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);
        TextView tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);

        tvUser.setText(tweet.getUser().getFormattedUserString());
        tvTweet.setText(tweet.getFormattedBody());
        tvTimestamp.setText(tweet.getAbbreviatedTimeSpan());
        ivPhoto.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivPhoto);

        return convertView;
    }
}
