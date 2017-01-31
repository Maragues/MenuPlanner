package com.maragues.menu_planner.ui.team;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by miguelaragues on 30/1/17.
 */

public abstract class TeamUtils {
  public static final String PARAM_USER_ID = "uid";

  private TeamUtils() {
  }

  public static Uri inviteUri() throws UnsupportedEncodingException {
    String innerLink = App.appComponent.context().getString(R.string.invites_deeplink_inner,
            PARAM_USER_ID + "=" + App.appComponent.userProvider().getUid());
    String dynamicLink = App.appComponent.context().getString(R.string.invites_deeplink,
            URLEncoder.encode(innerLink, "utf-8"));
    return Uri.parse(dynamicLink);
  }

  @Nullable
  public static String extractUserId(@Nullable Uri uri) {
    if (uri == null)
      return null;

    return uri.getQueryParameter(PARAM_USER_ID);
  }
}
