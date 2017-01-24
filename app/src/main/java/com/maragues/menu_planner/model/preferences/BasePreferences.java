package com.maragues.menu_planner.model.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;


/**
 * Created by maragues on 29/04/16.
 */
public abstract class BasePreferences implements IPreferencesProvider {

  public void clear() {
    getPrefsEditor().clear().apply();
  }

  public SharedPreferences.Editor getPrefsEditor() {
    return getPrefs().edit();
  }

  public SharedPreferences getPrefs() {
    return getPrefs(getPreferencesName());
  }

  public static SharedPreferences getPrefs(String name) {
    return App.appComponent.context()
            .getSharedPreferences(name, Context.MODE_PRIVATE);
  }

  @NonNull
  String getPreferencesName() {
    return PREFS_FILENAME;
  }

  public static final String PREFS_FILENAME = "my_preferences";
}
