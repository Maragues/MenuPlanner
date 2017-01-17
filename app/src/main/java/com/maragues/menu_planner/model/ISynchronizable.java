package com.maragues.menu_planner.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by miguelaragues on 22/12/16.
 */

public interface ISynchronizable extends Parcelable {
  @Nullable
  String id();
}
