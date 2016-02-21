package com.codepath.apps.simpletwitter.models;

import android.text.Html;
import android.text.Spanned;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gpalem on 2/19/16.
 */
public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

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
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
