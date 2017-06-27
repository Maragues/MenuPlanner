package com.maragues.menu_planner.ui.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.ui.common.BasePresenter;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class LoginPresenter extends BasePresenter<ILogin> {
  private static final String TAG = LoginPresenter.class.getSimpleName();
  boolean isFirstPresenterLaunch = true, userWantsToJoinTeam;

  String invitedToGroupId;

  @Override
  protected void onAttachView(@NonNull ILogin view) {
    super.onAttachView(view);

    checkForInvitation();
  }

  /**
   * Reads the invitedToGroupId from the View, and if it exists it stores it.
   *
   * If it's not the first launch of the presenter or if the invitedToGroupId is empty, we ignore
   * the returned value.
   *
   * In any case, we add the auth listener
   */
  void checkForInvitation() {
    getView().invitationGroupIdObservable()
            .subscribeOn(Schedulers.io())
            .doOnSuccess(ignore -> addAuthListener())
            .doAfterSuccess(ignore -> isFirstPresenterLaunch = false)
            .filter(invitedToGroupId -> isFirstPresenterLaunch
                    && !App.appComponent.textUtils().isEmpty(invitedToGroupId))
            .subscribe(this::onInvitedToGroupLoaded, Throwable::printStackTrace);
  }

  @Override
  protected void onDetachView() {
    super.onDetachView();

    removeAuthListener();
  }

  void onInvitedToGroupLoaded(String groupId) {
    Timber.i("Loaded GroupId: " + groupId);

    invitedToGroupId = groupId;
  }

  void addAuthListener() {
    if (getView() != null) {
      getView().addAuthListener();
    } else {
      sendToView(ILogin::addAuthListener);
    }
  }

  void removeAuthListener() {
    if (getView() != null) {
      getView().removeAuthListener();
    } else {
      sendToView(ILogin::removeAuthListener);
    }
  }

  public void onFirebaseUserArrived(@Nullable UserInfo userInfo) {
    if (userInfo != null) {
      final User firebaseUser = User.fromUserInfo(userInfo);

      disposables.add(App.appComponent.userProvider().exists(firebaseUser)
              .subscribeOn(Schedulers.io())
              .flatMap(userExists -> {
                if (userExists) {
                  //supposedly we have assured that the user exists, thus toSingle is safe
                  return App.appComponent.userProvider().get(userInfo.getUid()).toSingle();
                }

                User userToCreate = firebaseUser;
                if (invitedToGroupId != null && userWantsToJoinTeam)
                  userToCreate = firebaseUser.withGroupId(invitedToGroupId);

                return App.appComponent.userProvider().create(userToCreate);
              })
              .doOnSuccess(user -> {
                App.appComponent.signInPreferences().saveGroupId(user.groupId());
              })
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::onUserSignedIn)
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

  void onUserClickedSignIn() {
    if (getView() != null) {
      if (App.appComponent.textUtils().isEmpty(invitedToGroupId)) {
        signIn();
      } else {
        getView().showJoinTeamDialog(invitedToGroupId);
      }
    }
  }

  private void signIn() {
    if (getView() != null) {
      getView().signIn();
    } else {
      sendToView(ILogin::signIn);
    }
  }

  public void onUserAcceptedInvitation(boolean accepted) {
    userWantsToJoinTeam = accepted;

    signIn();
  }
}
