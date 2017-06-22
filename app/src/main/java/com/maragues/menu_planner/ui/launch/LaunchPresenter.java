package com.maragues.menu_planner.ui.launch;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.ui.common.BasePresenter;

import java.lang.ref.WeakReference;

import timber.log.Timber;

/**
 * Created by miguelaragues on 13/1/17.
 * <p>
 * This presenter is a special case where we don't want to follow ThirtyInch's lifecycle,
 * since onAttachView doesn't seem to be invoked before onResume, maybe due to the NoDisplay theme,
 * I don't know
 */

public class LaunchPresenter extends BasePresenter<ILaunch> {
  private static final String TAG = LaunchPresenter.class.getSimpleName();

  @Override
  protected void onCreate() {
    super.onCreate();
  }

  @Override
  protected void onAttachView(@NonNull ILaunch view) {
    //do nothing, special case
  }

  WeakReference<ILaunch> weakView;

  void decideNextScreen(@NonNull ILaunch view) {
    String uid = App.appComponent.userProvider().getUid();
    String gid = App.appComponent.userProvider().getGroupId();
    if (App.appComponent.textUtils().isEmpty(App.appComponent.userProvider().getUid())
            || App.appComponent.textUtils().isEmpty(App.appComponent.userProvider().getGroupId())) {
      Timber.d("Launch navigating to login");
      if (view != null) {
        view.navigateToLogin();
      } else if (weakView.get() != null) {
        weakView.get().navigateToLogin();
      }
    } else {
      Timber.d("Launch navigating to home");
      if (view != null) {
        view.navigateToHome();
      } else if (weakView.get() != null) {
        weakView.get().navigateToHome();
      }
    }
  }
}
