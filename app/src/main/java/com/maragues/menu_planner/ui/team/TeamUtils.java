package com.maragues.menu_planner.ui.team;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by miguelaragues on 30/1/17.
 */

public abstract class TeamUtils {
  public static final String PARAM_GROUP_ID = "nuid";

  private TeamUtils() {
  }

  public static Uri standardInviteUri(@NonNull String groupId) throws UnsupportedEncodingException {
    String innerLink = invitesDeeplinkUri(R.string.invites_deeplink_inner, groupId);
    String dynamicLink = App.appComponent.context().getString(R.string.invites_deeplink,
            URLEncoder.encode(innerLink, "utf-8"));
    return Uri.parse(dynamicLink);
  }

  public static String invitesDeeplinkUri(@StringRes int msgRes, @NonNull String groupId) {
    return App.appComponent.context().getString(msgRes, PARAM_GROUP_ID + "=" + groupId);
  }

  @Nullable
  public static String extractGroupId(@Nullable Uri uri) {
    if (uri == null)
      return null;

    return uri.getQueryParameter(PARAM_GROUP_ID);
  }
}
