package com.maragues.menu_planner.ui.login;

import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.ui.common.BasePresenter;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class LoginPresenter extends BasePresenter<ILogin> {
  private static final String TAG = LoginPresenter.class.getSimpleName();

  private Disposable disposable;

  public void onFirebaseUserArrived(@Nullable UserInfo firebaseUser) {
    if (firebaseUser != null) {
      App.appComponent.userProvider().exists(firebaseUser)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .flatMap(new Function<Boolean, SingleSource<User>>() {
                @Override
                public SingleSource<User> apply(Boolean userExists) throws Exception {
                  if (userExists) {
                    //supposedly we have assured that the user exists, thus toSingle is safe
                    return App.appComponent.userProvider().get(firebaseUser.getUid()).toSingle();
                  }

                  return App.appComponent.userProvider().create(firebaseUser);
                }
              })
              .map(user -> {
                App.appComponent.signInPreferences().saveGroupId(user.groupId());

                return user;
              })
              .doOnSuccess(this::onUserSignedIn)
              .subscribe();
    } else {
      // User is signed out
    }
  }

  void onUserSignedIn(User user) {
    hideProgressBar();

    navigateToHome();

    finish();
  }

  private void finish() {
    if (getView() != null) {
      getView().finish();
    } else {
      sendToView(ILogin::finish);
    }
  }

  private void navigateToHome() {
    if (getView() != null) {
      getView().navigateToHome();
    } else {
      sendToView(ILogin::navigateToHome);
    }
  }

  private void hideProgressBar() {
    if (getView() != null) {
      getView().hideProgressBar();
    } else {
      sendToView(ILogin::hideProgressBar);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (disposable != null && !disposable.isDisposed())
      disposable.dispose();
  }
}
