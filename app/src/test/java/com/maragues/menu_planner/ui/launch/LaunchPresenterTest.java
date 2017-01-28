package com.maragues.menu_planner.ui.launch;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.test.mock.providers.MockUserProvider;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 13/1/17.
 */
public class LaunchPresenterTest extends BasePresenterTest<ILaunch, LaunchPresenter> {

  public LaunchPresenterTest() {
    super(ILaunch.class);
  }

  @Override
  protected void onPresenterCreated() {
    super.onPresenterCreated();

    doNothing().when(presenter).attachView(any(ILaunch.class));
  }

  @NonNull
  @Override
  protected LaunchPresenter createPresenter() {
    return new LaunchPresenter();
  }

  @Test
  public void noUser_navigatesToLogin() {
    initPresenter();

    ((MockUserProvider) App.appComponent.userProvider()).setUid(null);

    presenter.decideNextScreen(view);

    verify(view).navigateToLogin();
    verify(view, never()).navigateToHome();
  }

  @Test
  public void noUser_navigatesToHome() {
    initPresenter();

    presenter.decideNextScreen(view);

    verify(view, never()).navigateToLogin();
    verify(view).navigateToHome();
  }

  /*
  INVITES
   */
  @Test
  public void firstLaunch_subscribesToInvitationObservable(){
    App.appComponent.signInPreferences().clear();

    initPresenter();

    verify(view).invitationObservable();
  }

  @Test
  public void firstLaunch_checksForInvites(){
    App.appComponent.signInPreferences().clear();

    initPresenter();

    verify(view).invitationExists();
  }

  @Test
  public void firstLaunch_invitesTrue_doesNotNavigateAnywhere(){
  //in this case, Android takes care and launches our DeepLinkActivity
  }

  @Test
  public void firstLaunch_invitesFalse_navigatesToLogin(){
    //in this case, Android takes care and launches our DeepLinkActivity
  }

  @Test
  public void secondLaunch_doesNotCheckForInvites(){

  }

  @Test
  public void secondLaunch_doesNotSubscribeForInvites(){

    verify(view, never()).invitationObservable();
  }
}