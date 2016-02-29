// Generated code from Butter Knife. Do not modify!
package com.codepath.apps.twitter.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ProfileActivity$$ViewBinder<T extends com.codepath.apps.twitter.activities.ProfileActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427473, "field 'tvProfileName'");
    target.tvProfileName = finder.castView(view, 2131427473, "field 'tvProfileName'");
    view = finder.findRequiredView(source, 2131427474, "field 'tvProfileCaption'");
    target.tvProfileCaption = finder.castView(view, 2131427474, "field 'tvProfileCaption'");
    view = finder.findRequiredView(source, 2131427437, "field 'tvProfileFollowers'");
    target.tvProfileFollowers = finder.castView(view, 2131427437, "field 'tvProfileFollowers'");
    view = finder.findRequiredView(source, 2131427438, "field 'tvProfileFollowing'");
    target.tvProfileFollowing = finder.castView(view, 2131427438, "field 'tvProfileFollowing'");
    view = finder.findRequiredView(source, 2131427472, "field 'ivProfileImage'");
    target.ivProfileImage = finder.castView(view, 2131427472, "field 'ivProfileImage'");
    view = finder.findRequiredView(source, 2131427439, "field 'profileTabs'");
    target.profileTabs = finder.castView(view, 2131427439, "field 'profileTabs'");
    view = finder.findRequiredView(source, 2131427440, "field 'profileViewPager'");
    target.profileViewPager = finder.castView(view, 2131427440, "field 'profileViewPager'");
  }

  @Override public void unbind(T target) {
    target.tvProfileName = null;
    target.tvProfileCaption = null;
    target.tvProfileFollowers = null;
    target.tvProfileFollowing = null;
    target.ivProfileImage = null;
    target.profileTabs = null;
    target.profileViewPager = null;
  }
}
