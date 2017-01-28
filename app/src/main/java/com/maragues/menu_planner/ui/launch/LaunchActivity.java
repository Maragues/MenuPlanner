package com.maragues.menu_planner.ui.launch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.maragues.menu_planner.ui.common.BaseActivity;
import com.maragues.menu_planner.ui.home.HomeActivity;
import com.maragues.menu_planner.ui.login.LoginActivity;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class LaunchActivity extends BaseActivity<LaunchPresenter, ILaunch> implements ILaunch {

  private GoogleApiClient googleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getPresenter().decideNextScreen(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @NonNull
  @Override
  public LaunchPresenter providePresenter() {
    return new LaunchPresenter();
  }

  @Override
  public void navigateToLogin() {
    startActivity(LoginActivity.createIntent(this));

    finish();
  }

  @Override
  public void navigateToHome() {
    startActivity(HomeActivity.createIntent(this));

    finish();
  }

  private BehaviorSubject<Boolean> hasInvitationSubject = BehaviorSubject.create();

  @Override
  public Observable<Boolean> invitationObservable(){
    return hasInvitationSubject;
  }

  public void checkInvitations(){
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
                    new ResultCallback<AppInviteInvitationResult>() {
                      @Override
                      public void onResult(AppInviteInvitationResult result) {
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
                      }
                    });
  }
}
