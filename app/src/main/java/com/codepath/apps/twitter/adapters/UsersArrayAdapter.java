package com.codepath.apps.twitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitter.R;
import com.codepath.apps.twitter.activities.ProfileActivity;
import com.codepath.apps.twitter.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gpalem on 2/28/16.
 */
public class UsersArrayAdapter extends ArrayAdapter<User> {

    public UsersArrayAdapter(Context context, List<User> users) {
        super(context, android.R.layout.simple_list_item_1, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get Tweet
        //Find or inflate template
        final User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_follow, parent, false);
        }

        ImageView ivFollowPhoto = (ImageView) convertView.findViewById(R.id.ivFollowPhoto);
        ivFollowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent displayUserProfile = new Intent(getContext(), ProfileActivity.class);
                displayUserProfile.putExtra("screen_name", user.getScreenName());
                getContext().startActivity(displayUserProfile);
            }
        });
        TextView tvFollowUser = (TextView) convertView.findViewById(R.id.tvFollowUser);
        TextView tvFollowScreenName = (TextView) convertView.findViewById(R.id.tvFollowScreenName);

        tvFollowUser.setText(user.getFormattedUser());
        tvFollowScreenName.setText("@"+user.getScreenName());
        ivFollowPhoto.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivFollowPhoto);

        return convertView;
    }
}
