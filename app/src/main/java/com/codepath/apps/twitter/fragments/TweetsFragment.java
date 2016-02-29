package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.adapters.TweetsArrayAdapter;
import com.codepath.apps.twitter.clients.TwitterApplication;
import com.codepath.apps.twitter.clients.TwitterClient;
import com.codepath.apps.twitter.listeners.EndlessScrollListener;
import com.codepath.apps.twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gpalem on 2/25/16.
 */
public abstract class TweetsFragment extends Fragment {

    String TAG = this.getClass().getSimpleName();

    TwitterClient client;

    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter tweetsAdapter;
    protected ListView lvTweets;

    JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
                //Success
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i(TAG, response.toString());
                    addAll(Tweet.fromJSONArray(response));
                }

                //Failure
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    client.networkFailHandler(throwable);
                }
            };

    //inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                getNextTweets();
                return true;
            }
        });
        return view;
    }

    //creation logic
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(); //singleton client
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsArrayAdapter(getActivity(), tweets);
        populateTimeline();
    }

    public long getMaxUid() {
        return tweets.get(tweets.size()-1).getuid();
    }

    public void addAll(ArrayList<Tweet> tweets) {
        this.tweets.addAll(tweets);
        tweetsAdapter.notifyDataSetChanged();
    }

    public void insert(int index, Tweet tweet) {
        tweets.add(index, tweet);
        tweetsAdapter.notifyDataSetChanged();
    }

    abstract void populateTimeline();
    abstract void getNextTweets();

}
