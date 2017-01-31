package com.maragues.menu_planner.ui.team;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;

/**
 * Created by miguelaragues on 30/1/17.
 */

public abstract class TeamUtils {
  public static final String PARAM_USER_ID = "uid";

  private TeamUtils() {
  }

  public static Uri inviteUri() {
    return Uri.withAppendedPath(
            Uri.parse(App.appComponent.context().getString(R.string.invites_deeplink)),
            "?" + PARAM_USER_ID + "=" + App.appComponent.userProvider().getUid());
  }

  @Nullable
  public static String extractUserId(@Nullable Uri uri) {
    if (uri == null)
      return null;

    return uri.getQueryParameter(PARAM_USER_ID);
  }
}
