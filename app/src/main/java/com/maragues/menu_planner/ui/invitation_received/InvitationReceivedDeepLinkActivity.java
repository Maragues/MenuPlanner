package com.maragues.menu_planner.ui.invitation_received;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.maragues.menu_planner.R;

public class InvitationReceivedDeepLinkActivity extends AppCompatActivity {
  private static final String TAG = InvitationReceivedDeepLinkActivity.class.getSimpleName();

  private GoogleApiClient googleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invitation_received_deep_link);

    // Create an auto-managed GoogleApiClient with access to App Invites.
    googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(AppInvite.API)
            .enableAutoManage(this, this)
            .build();

    // Check for App Invite invitations and launch deep-link activity if possible.
    // Requires that an Activity is registered in AndroidManifest.xml to handle
    // deep-link URLs.
    boolean autoLaunchDeepLink = true;
    AppInvite.AppInviteApi.getInvitation(googleApiClient, this, autoLaunchDeepLink)
            .setResultCallback(
                    result -> {
                      Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                      if (result.getStatus().isSuccess()) {
                        // Extract information from the intent
                        Intent intent = result.getInvitationIntent();
                        String deepLink = AppInviteReferral.getDeepLink(intent);
                        String invitationId = AppInviteReferral.getInvitationId(intent);

                        // Because autoLaunchDeepLink = true we don't have to do anything
                        // here, but we could set that to false and manually choose
                        // an Activity to launch to handle the deep link here.
                        // ...
                      }
                    });
  }
}
