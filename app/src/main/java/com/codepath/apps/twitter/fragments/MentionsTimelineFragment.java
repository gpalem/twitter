package com.codepath.apps.twitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gpalem on 2/26/16.
 */
public class MentionsTimelineFragment extends TweetsFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void getNextTweets() {
        long max_id = getMaxUid();
        client.getNextMentions(max_id-1, handler);
    }

    void populateTimeline() {
        client.getMentionsTimeline(handler);
    }
}
