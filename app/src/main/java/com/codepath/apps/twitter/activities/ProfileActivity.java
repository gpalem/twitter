package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.clients.TwitterApplication;
import com.codepath.apps.twitter.clients.TwitterClient;
import com.codepath.apps.twitter.fragments.UserTimelineFragment;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private TwitterClient client;
    private UserTimelineFragment fragmentUserTimeline;
    User user;

    @Bind(R.id.tvProfileName) TextView tvProfileName;
    @Bind(R.id.tvProfileCaption) TextView tvProfileCaption;
    @Bind(R.id.tvProfileFollowers) TextView tvProfileFollowers;
    @Bind(R.id.tvProfileFollowing) TextView tvProfileFollowing;
    @Bind(R.id.ivProfileImage) ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        //Get screen_name
        String screenName = getIntent().getStringExtra("screen_name");

        client = TwitterApplication.getRestClient();
        displayUserInfo(screenName);
        if (savedInstanceState == null) {
            //Create user timeline fragment
            fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
            //Display user fragment
            displayUserTimelineFragment();
        }
    }

    public void displayUserInfo(String screenName) {
        if (screenName == null) {
            client.getCurrentUserInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle(user.getScreenName());
                    populateUserHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    Log.e(TAG, response.toString());
                }
            });
        }
        else {
            client.getUserInfo(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle(user.getScreenName());
                    populateUserHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    Log.e(TAG, response.toString());
                }
            });
        }
    }

    public void displayUserTimelineFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, fragmentUserTimeline);
        ft.commit(); //change fragments
    }

    public void populateUserHeader(User user) {
        tvProfileName.setText(user.getName());
        tvProfileCaption.setText(user.getCaption());
        tvProfileFollowers.setText(user.getFollowers() + " Followers");
        tvProfileFollowing.setText(user.getFollowing() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

}
