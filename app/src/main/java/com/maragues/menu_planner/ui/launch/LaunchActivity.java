package com.maragues.menu_planner.ui.launch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.maragues.menu_planner.ui.common.BaseActivity;
import com.maragues.menu_planner.ui.home.HomeActivity;
import com.maragues.menu_planner.ui.login.LoginActivity;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class LaunchActivity extends BaseActivity<LaunchPresenter, ILaunch> implements ILaunch {

  public static Intent createIntent(@NonNull Context context) {
    return new Intent(context, LaunchActivity.class);
  }

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
}
