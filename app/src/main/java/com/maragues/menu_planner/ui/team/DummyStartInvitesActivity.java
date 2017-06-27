package com.maragues.menu_planner.ui.team;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.R;

/**
 * Created by maragues on 24/02/16.
 */
public final class DummyStartInvitesActivity extends Activity {
  private static final String TAG = DummyStartInvitesActivity.class.getSimpleName();

  private static final int REQUEST_INVITE = 7;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
            .setMessage(getString(R.string.invitation_title))
            .setDeepLink(Uri.parse(
                    TeamUtils.invitesDeeplinkUri(R.string.invites_deeplink,
                            App.appComponent.userProvider().getGroupId()
                    )
            ))
            .setCallToActionText(getString(R.string.invitation_ca))
            .build();

    try {
      startActivityForResult(intent, REQUEST_INVITE);
    } catch (ActivityNotFoundException e) {

    }

    finish();
  }
}
