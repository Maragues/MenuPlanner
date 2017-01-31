package com.maragues.menu_planner.ui.invitation_received;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.maragues.menu_planner.ui.common.BaseActivity;
import com.maragues.menu_planner.ui.launch.LaunchActivity;
import com.maragues.menu_planner.ui.login.LoginActivity;
import com.maragues.menu_planner.ui.team.TeamUtils;

public class InvitationReceivedDeepLinkActivity extends BaseActivity<InvitationReceivedPresenter, IInvitationReceived>
        implements IInvitationReceived, GoogleApiClient.OnConnectionFailedListener {
  private static final String TAG = InvitationReceivedDeepLinkActivity.class.getSimpleName();

  private GoogleApiClient googleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getPresenter().decideNextScreen(this);
  }

  private void checkInvites() {
    googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(AppInvite.API)
            .build();

    boolean autoLaunchDeepLink = false;
    AppInvite.AppInviteApi.getInvitation(googleApiClient, this, autoLaunchDeepLink)
            .setResultCallback(
                    result -> {
                      if (result.getStatus().isSuccess()) {
                        // Extract information from the intent
                        Intent intent = result.getInvitationIntent();
                        String deepLink = AppInviteReferral.getDeepLink(intent);
                        String invitationId = AppInviteReferral.getInvitationId(intent);

                        Log.d(TAG, "Deep link. " + deepLink + ", InvitationId " + invitationId);

                        // Because autoLaunchDeepLink = true we don't have to do anything
                        // here, but we could set that to false and manually choose
                        // an Activity to launch to handle the deep link here.
                        // ...
                      }
                    });

    googleApiClient.disconnect();
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @NonNull
  @Override
  public InvitationReceivedPresenter providePresenter() {
    return new InvitationReceivedPresenter();
  }

  @Nullable
  @Override
  public String getInvitedByUserId() {
    if (getIntent().getData() == null)
      return null;

    return TeamUtils.extractUserId(getIntent().getData());
  }

  @Override
  public void navigateToLogin(String invitedByUserId) {
    startActivity(LoginActivity.createIntent(this, invitedByUserId));
  }

  @Override
  public void navigateToAcceptInvitation(String invitedByUserId) {

  }

  @Override
  public void navigateToLauncher() {
    startActivity(LaunchActivity.createIntent(this));
  }
}
