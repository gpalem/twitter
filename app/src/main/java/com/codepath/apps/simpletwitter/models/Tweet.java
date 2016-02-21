package com.codepath.apps.simpletwitter.models;

import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by gpalem on 2/19/16.
 */
//Parse JSONArray and store data
public class Tweet {
    private String body;
    private long uid; //unique id of tweet
    private User user;
    private String createdAt;
    private long timestamp;

    public String getBody() {
        return body;
    }

    public long getuid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public Spanned getFormattedBody() {
        String formatted = "<font color=\"black\">" + body + "</font>";
        return Html.fromHtml(formatted);
    }

    public String getAbbreviatedTimeSpan() {
        long elapsed = Math.max(System.currentTimeMillis() - timestamp, 0);
        String timeSpan = "";
        if (elapsed >= DateUtils.YEAR_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.YEAR_IN_MILLIS) + "y";
        }
        else if (elapsed >= DateUtils.WEEK_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.WEEK_IN_MILLIS) + "w";
        }
        else if (elapsed >= DateUtils.DAY_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.DAY_IN_MILLIS) + "d";
        }
        else if (elapsed >= DateUtils.HOUR_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.HOUR_IN_MILLIS) + "h";
        }
        else if (elapsed >= DateUtils.MINUTE_IN_MILLIS) {
            timeSpan = String.valueOf(elapsed / DateUtils.MINUTE_IN_MILLIS) + "m";
        }
        else {
            timeSpan = String.valueOf(elapsed / DateUtils.SECOND_IN_MILLIS) + "s";
        }
        return timeSpan;
    }

    //Deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy").parse(tweet.createdAt).getTime();
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject tweetJSON = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJSON);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
