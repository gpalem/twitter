// Generated code from Butter Knife. Do not modify!
package com.codepath.apps.twitter.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ComposeActivity$$ViewBinder<T extends com.codepath.apps.twitter.activities.ComposeActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427434, "field 'btnCompose'");
    target.btnCompose = finder.castView(view, 2131427434, "field 'btnCompose'");
    view = finder.findRequiredView(source, 2131427433, "field 'etCompose'");
    target.etCompose = finder.castView(view, 2131427433, "field 'etCompose'");
  }

  @Override public void unbind(T target) {
    target.btnCompose = null;
    target.etCompose = null;
  }
}
