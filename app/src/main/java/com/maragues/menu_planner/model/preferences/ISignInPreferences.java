package com.maragues.menu_planner.model.preferences;

import android.support.annotation.Nullable;

/**
 * Created by miguelaragues on 24/1/17.
 */

public interface ISignInPreferences extends IPreferencesProvider {
  void saveGroupId(String groupId);

  @Nullable
  String getGroupId();

  boolean hasGroupId();
}
