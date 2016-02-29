package com.codepath.apps.twitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.clients.TwitterApplication;
import com.codepath.apps.twitter.clients.TwitterClient;
import com.codepath.apps.twitter.fragments.HomeTimelineFragment;
import com.codepath.apps.twitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = TimelineActivity.class.getSimpleName();
    TwitterClient client;

    private final int TWEET_REQUEST_CODE = 1;
    private final int TWEET_RESPONSE_CODE = 1;

    @Bind(R.id.tabs) PagerSlidingTabStrip tabs;
    @Bind(R.id.viewpager) ViewPager viewPager;
    private TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient();

        //Get viewpager, set adapter to viewpager, find sliding tabstrip and attach tabstrip
        ButterKnife.bind(this);
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tweetsPagerAdapter);
        tabs.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline, menu);

        MenuItem tweetItem = (MenuItem) menu.findItem(R.id.mnuTweet);
        tweetItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(i, TWEET_REQUEST_CODE);
                return true;
            }
        });

        MenuItem profileItem = (MenuItem) menu.findItem(R.id.mnuProfile);
        profileItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                client.getCurrentUserInfo(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        User user = User.fromJSON(response);
                        Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
                        i.putExtra("screen_name", user.getScreenName());
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                        client.networkFailHandler(throwable);
                    }
                });
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TWEET_RESPONSE_CODE && requestCode == TWEET_REQUEST_CODE) {
            //Post and Get Updated Timeline
            String tweet = data.getExtras().getString("tweet");
            HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment) tweetsPagerAdapter.getItem(0);
            homeTimelineFragment.updateTweet(tweet);
        }
    }

    //Return order of fragments in view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "@Mentions"};
        private Fragment fragmentList[] = {null, null};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (fragmentList[0] == null)
                    fragmentList[0] = new HomeTimelineFragment();
                return fragmentList[0];
            }
            else {
                if (fragmentList[1] == null)
                    fragmentList[1] = new MentionsTimelineFragment();
                return fragmentList[1];
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
