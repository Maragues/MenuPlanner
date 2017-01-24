package com.maragues.menu_planner.test.mock.preferences;

import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.preferences.ISignInPreferences;

/**
 * Created by miguelaragues on 24/1/17.
 */

public class MockSignInPreferences implements ISignInPreferences {
  private String groupId;

  @Override
  public void saveGroupId(String groupId) {
    this.groupId = groupId;
  }

  @Nullable
  @Override
  public String getGroupId() {
    return groupId;
  }

  @Override
  public boolean hasGroupId() {
    return !App.appComponent.textUtils().isEmpty(groupId);
  }

  @Override
  public void clear() {
    groupId = null;
  }
}
