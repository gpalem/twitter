package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gpalem on 2/28/16.
 */
public class FollowingFragment extends FollowFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    public static FollowingFragment newInstance(String screenName) {
        FollowingFragment followingsFragment = new FollowingFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        followingsFragment.setArguments(args);
        return followingsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void populateUsers() {
        String screenName = getArguments().getString("screen_name");
        client.getFollowing(screenName, nextCursor, handler);
    }
}
