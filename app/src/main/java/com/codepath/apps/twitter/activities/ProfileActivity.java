package com.codepath.apps.twitter.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.clients.TwitterApplication;
import com.codepath.apps.twitter.clients.TwitterClient;
import com.codepath.apps.twitter.fragments.FollowersFragment;
import com.codepath.apps.twitter.fragments.FollowingFragment;
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

    @Bind(R.id.profile_tabs) PagerSlidingTabStrip profileTabs;
    @Bind(R.id.profile_viewpager) ViewPager profileViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        //Get screen_name
        String screenName = getIntent().getStringExtra("screen_name");
        Log.i(TAG, screenName);

        client = TwitterApplication.getRestClient();
        displayUserInfo(screenName);

        profileViewPager.setAdapter(new UserPagerAdapter(getSupportFragmentManager(), screenName));
        profileTabs.setViewPager(profileViewPager);
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
                    client.networkFailHandler(throwable);
                }
            });
        }
        else {
            client.getUserInfo(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    getSupportActionBar().setTitle("@"+user.getScreenName());
                    populateUserHeader(user);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    client.networkFailHandler(throwable);
                }
            });
        }
    }

    public void populateUserHeader(User user) {
        tvProfileName.setText(user.getName());
        tvProfileCaption.setText(user.getCaption());
        tvProfileFollowers.setText(user.getFollowers() + " Followers");
        tvProfileFollowing.setText(user.getFollowing() + " Following");
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    //Return order of fragments in view pager
    public class UserPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = {"Tweets", "Followers", "Following"};

        private String screenName;

        public UserPagerAdapter(FragmentManager fm, String screenName) {
            super(fm);
            this.screenName = screenName;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return UserTimelineFragment.newInstance(screenName);
            }
            if (position == 1) {
                return FollowersFragment.newInstance(screenName);
            }
            else {
                return FollowingFragment.newInstance(screenName);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

}
