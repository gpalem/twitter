package com.codepath.apps.simpletwitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = TimelineActivity.class.getSimpleName();

    private final int TWEET_REQUEST_CODE = 1;
    private final int TWEET_RESPONSE_CODE = 1;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsAdapter;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsArrayAdapter(this, tweets);
        lvTweets.setAdapter(tweetsAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                getNextTweets();
                return true;
            }
        });

        client = TwitterApplication.getRestClient(); //singleton client
        populateTimeline();
    }

    private void getNextTweets() {
        long max_id = tweets.get(tweets.size()-1).getuid();
        client.getNextTweets(max_id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
                tweets.addAll(Tweet.fromJSONArray(response));
                tweetsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, responseString.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(TAG, errorResponse.toString());
            }
        });
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            //Success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(TAG, response.toString());
                tweets.addAll(Tweet.fromJSONArray(response));
                tweetsAdapter.notifyDataSetChanged();
            }

            //Failure

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(TAG, errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.i(TAG, errorResponse.toString());
            }
        });
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

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TWEET_RESPONSE_CODE && requestCode == TWEET_REQUEST_CODE) {
            //Post and Get Updated Timeline
            String tweet = data.getExtras().getString("tweet");
            client.postUpdate(tweet, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("TWEET", response.toString());
                    tweets.add(0, Tweet.fromJSON(response));
                    tweetsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i(TAG, responseString);
                }
            });
        }
    }
}
