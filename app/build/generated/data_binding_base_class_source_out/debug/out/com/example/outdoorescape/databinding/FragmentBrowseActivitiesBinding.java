// Generated by view binder compiler. Do not edit!
package com.example.outdoorescape.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.outdoorescape.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentBrowseActivitiesBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView rvBrowseActivities;

  private FragmentBrowseActivitiesBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView rvBrowseActivities) {
    this.rootView = rootView;
    this.rvBrowseActivities = rvBrowseActivities;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentBrowseActivitiesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentBrowseActivitiesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_browse_activities, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentBrowseActivitiesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.rvBrowseActivities;
      RecyclerView rvBrowseActivities = ViewBindings.findChildViewById(rootView, id);
      if (rvBrowseActivities == null) {
        break missingId;
      }

      return new FragmentBrowseActivitiesBinding((ConstraintLayout) rootView, rvBrowseActivities);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}