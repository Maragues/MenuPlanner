package com.maragues.menu_planner.ui.login;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.test.factories.UserFactory;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 24/1/17.
 */
public class LoginPresenterTest extends BasePresenterTest<ILogin, LoginPresenter> {
  public LoginPresenterTest() {
    super(ILogin.class);
  }

  @NonNull
  @Override
  protected LoginPresenter createPresenter() {
    return new LoginPresenter();
  }

  @Test
  public void firebaseUserArrives_exists_invokesOnUserSignedIn() {
    initPresenter();

    UserInfo userInfo = mock(FirebaseUser.class);
    doReturn(UserFactory.DEFAULT_UID).when(userInfo).getUid();

    doReturn(Single.just(true)).when(App.appComponent.userProvider()).exists(eq(userInfo));
    doReturn(Maybe.just(UserFactory.base())).when(App.appComponent.userProvider()).get(eq(UserFactory.DEFAULT_UID));

    presenter.onFirebaseUserArrived(userInfo);

    verify(presenter).onUserSignedIn(any(User.class));
  }

  @Test
  public void firebaseUserArrives_notExists_createsNewUser() {
    initPresenter();

    UserInfo userInfo = mock(FirebaseUser.class);
    doReturn(Single.just(false)).when(App.appComponent.userProvider()).exists(eq(userInfo));

    presenter.onFirebaseUserArrived(userInfo);

    verify(App.appComponent.userProvider()).create(eq(userInfo));
  }

  @Test
  public void firebaseUserArrives_null_doesNotNavigateToHome() {
    initPresenter();

    presenter.onFirebaseUserArrived(null);

    verify(view, never()).navigateToHome();
  }

  @Test
  public void firebaseUserArrives_null_doesNotFinish() {
    initPresenter();

    presenter.onFirebaseUserArrived(null);

    verify(view, never()).finish();
  }

  /*
  ON USER SIGNED IN
   */
  @Test
  public void onUserSignedIn_invokesNavigateToHome() {
    initPresenter();

    presenter.onUserSignedIn(mock(User.class));

    verify(view).navigateToHome();
  }

  @Test
  public void onUserSignedIn_invokesHideProgressBar() {
    initPresenter();

    presenter.onUserSignedIn(mock(User.class));

    verify(view).hideProgressBar();
  }

  @Test
  public void onUserSignedIn_invokesFinish() {
    initPresenter();

    presenter.onUserSignedIn(mock(User.class));

    verify(view).finish();
  }
}