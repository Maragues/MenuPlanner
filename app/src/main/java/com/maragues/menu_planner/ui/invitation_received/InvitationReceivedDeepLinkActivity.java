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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG,"Creating deeplink activity navigating");

    getPresenter().decideNextScreen(this);
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
