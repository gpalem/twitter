package com.codepath.apps.twitter.models;

import android.text.Html;
import android.text.Spanned;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gpalem on 2/19/16.
 */
public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String caption;
    private int followers;
    private int following;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
    }

    public Spanned getFormattedUser() {
        String formatted = "<font color=\"black\"><b>" + name + "</b></font>  ";
        return Html.fromHtml(formatted);
    }

    public Spanned getFormattedScreenName() {
        String formatted = "<font color=\"black\">@" + screenName + "</font>  ";
        return Html.fromHtml(formatted);
    }

    public Spanned getFormattedUserString() {
        String formatted = "<font color=\"black\">" + name + "</font>  ";
        formatted += "<font color=\"black\"><b>@" + screenName + "</b></font>";
        return Html.fromHtml(formatted);
    }

    public static User fromJSON(JSONObject jsonObject) {
        User u = new User();
        try {
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.caption = jsonObject.getString("description");
            u.followers = jsonObject.getInt("followers_count");
            u.following = jsonObject.getInt("friends_count");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    public static ArrayList<User> fromJSONArray(JSONArray jsonArray) {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject userJSON = jsonArray.getJSONObject(i);
                User user = User.fromJSON(userJSON);
                if (user != null) {
                    users.add(user);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return users;
    }
}
