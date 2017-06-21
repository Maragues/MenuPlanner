package com.maragues.menu_planner.ui.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.ui.common.BasePresenter;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class LoginPresenter extends BasePresenter<ILogin> {
  private static final String TAG = LoginPresenter.class.getSimpleName();
  private boolean userWantsToJoinTeam;

  @Override
  protected void onAttachView(@NonNull ILogin view) {
    super.onAttachView(view);

    if (!isHandlingInvitesScenario())
      getView().addAuthListener();

    App.appComponent.signInPreferences().touchFirstLaunch();
  }

  boolean isHandlingInvitesScenario() {
    if (getView().getInvitedByUserId() != null) {
      disposables.add(App.appComponent.userProvider().get(getView().getInvitedByUserId())
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .doOnSuccess(this::onInvitedByUserLoaded)
              .doOnComplete(this::checkInvites)
              .subscribe());

      return true;
    } else if (App.appComponent.signInPreferences().isFirstLaunch()) {
      checkInvites();

      return true;
    }

    return false;
  }

  void checkInvites() {
    disposables.add(getView().invitationObservable()
            .subscribe(this::onInvitationChecked));

    getView().checkInvitations();
  }

  private User invitedByUser;

  void onInvitedByUserLoaded(User user) {
    invitedByUser = user;

    if (getView() != null) {
      getView().showInvitationLayout(user);
    } else {
      sendToView(v -> v.showInvitationLayout(user));
    }
  }

  void onInvitationChecked(Boolean hasInvitation) {
    if (!hasInvitation) {
      if (getView() != null) {
        getView().addAuthListener();
      } else {
        sendToView(ILogin::addAuthListener);
      }
    }

    //else no need for, App Invites takes care of launching the activity
  }

  public void onFirebaseUserArrived(@Nullable UserInfo userInfo) {
    if (userInfo != null) {
      final User firebaseUser = User.fromUserInfo(userInfo);

      disposables.add(App.appComponent.userProvider().exists(firebaseUser)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .flatMap(new Function<Boolean, SingleSource<User>>() {
                @Override
                public SingleSource<User> apply(Boolean userExists) throws Exception {
                  if (userExists) {
                    //supposedly we have assured that the user exists, thus toSingle is safe
                    return App.appComponent.userProvider().get(userInfo.getUid()).toSingle();
                  }

                  User userToCreate = firebaseUser;
                  if (invitedByUser != null && userWantsToJoinTeam)
                    userToCreate = firebaseUser.withGroupId(invitedByUser.groupId());

                  return App.appComponent.userProvider().create(userToCreate);
                }
              })
              .map(user -> {
                App.appComponent.signInPreferences().saveGroupId(user.groupId());

                return user;
              })
              .doOnSuccess(this::onUserSignedIn)
              .subscribe()
      );
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

  public void userClickedOnJoinTeam() {
    userWantsToJoinTeam = true;
  }

  public void userClickedOnStandardSignIn() {
    userWantsToJoinTeam = false;
  }
}
