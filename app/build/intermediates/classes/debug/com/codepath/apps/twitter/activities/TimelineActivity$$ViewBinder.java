// Generated code from Butter Knife. Do not modify!
package com.codepath.apps.twitter.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TimelineActivity$$ViewBinder<T extends com.codepath.apps.twitter.activities.TimelineActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427440, "field 'tabs'");
    target.tabs = finder.castView(view, 2131427440, "field 'tabs'");
    view = finder.findRequiredView(source, 2131427441, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131427441, "field 'viewPager'");
  }

  @Override public void unbind(T target) {
    target.tabs = null;
    target.viewPager = null;
  }
}
