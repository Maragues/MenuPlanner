package com.maragues.menu_planner.ui.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.ui.common.BasePresenter;

import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class LoginPresenter extends BasePresenter<ILogin> {
  private static final String TAG = LoginPresenter.class.getSimpleName();
  private boolean userWantsToJoinTeam;

  String invitedToGroupId;

  @Override
  protected void onAttachView(@NonNull ILogin view) {
    super.onAttachView(view);

    /*
    * Checks if it's the first launch
    *
    * If it is, it checks if there's a pending invitation and stores the group id, which might be
    * an empty string
    *
    * In any case, it adds an auth listener and touches the first launch
    *
     */
    Single.just(App.appComponent.signInPreferences().isFirstLaunch())
            .subscribeOn(Schedulers.io())
            .doOnSuccess(ignore -> {
              App.appComponent.signInPreferences().touchFirstLaunch();

              addAuthListener();
            })
            .flatMap(firstLaunch -> {
              if (firstLaunch) return view.invitationGroupIdObservable();

              return Single.just("");
            })
            .subscribe(this::onInvitedToGroupLoaded, Throwable::printStackTrace);
  }

  void onInvitedToGroupLoaded(String groupId) {
    Timber.i("Loaded GroupId: " + groupId);

    invitedToGroupId = groupId;
  }

  private void addAuthListener() {
    if (getView() != null) {
      getView().addAuthListener();
    } else {
      sendToView(ILogin::addAuthListener);
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

  public void userClickedOnJoinTeam() {
    userWantsToJoinTeam = true;
  }

  public void userClickedOnStandardSignIn() {
    userWantsToJoinTeam = false;
  }
}
