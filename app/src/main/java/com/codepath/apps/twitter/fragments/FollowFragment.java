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
import com.codepath.apps.twitter.adapters.UsersArrayAdapter;
import com.codepath.apps.twitter.clients.TwitterApplication;
import com.codepath.apps.twitter.clients.TwitterClient;
import com.codepath.apps.twitter.listeners.EndlessScrollListener;
import com.codepath.apps.twitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gpalem on 2/28/16.
 */
public abstract class FollowFragment extends Fragment {

    String TAG = this.getClass().getSimpleName();

    TwitterClient client;

    private ArrayList<User> users;
    private UsersArrayAdapter usersAdapter;
    protected ListView lvUsers;
    long nextCursor = -1;
    long prevCursor = -1;

    JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
        //Success
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i(TAG, response.toString());
            try {
                addAll(User.fromJSONArray(response.getJSONArray("users")));
                nextCursor = response.getLong("next_cursor");
                prevCursor = response.getLong("previous_cursor");
                Log.i(TAG, String.valueOf(prevCursor) + " " + String.valueOf(nextCursor));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
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
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        lvUsers = (ListView) view.findViewById(R.id.lvUsers);
        lvUsers.setAdapter(usersAdapter);
        lvUsers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateUsers();
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
        users = new ArrayList<>();
        usersAdapter = new UsersArrayAdapter(getActivity(), users);
        populateUsers();
    }

    public long getMaxUid() {
        return users.get(users.size()-1).getUid();
    }

    public void addAll(ArrayList<User> users) {
        this.users.addAll(users);
        usersAdapter.notifyDataSetChanged();
    }

    abstract void populateUsers();
}
