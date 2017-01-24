package com.maragues.menu_planner.model.preferences;

import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;

/**
 * Created by maragues on 12/05/16.
 */
public class SignInPreferences extends BasePreferences implements ISignInPreferences {

  @Override
  public void saveGroupId(String groupId) {
    getPrefsEditor().putString(PREFS_KEY_GROUPID, groupId).apply();
  }

  @Nullable
  public String getGroupId() {
    return getPrefs().getString(PREFS_KEY_GROUPID, null);
  }

  @Override
  public boolean hasGroupId() {
    return !App.appComponent.textUtils().isEmpty(getGroupId());
  }

  public static final String PREFS_KEY_GROUPID = "group_id";

}
