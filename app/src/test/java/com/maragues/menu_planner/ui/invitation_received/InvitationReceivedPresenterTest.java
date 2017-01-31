package com.maragues.menu_planner.ui.invitation_received;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 30/1/17.
 */
public class InvitationReceivedPresenterTest extends BasePresenterTest<IInvitationReceived, InvitationReceivedPresenter> {

  public InvitationReceivedPresenterTest() {
    super(IInvitationReceived.class);
  }

  @NonNull
  @Override
  protected InvitationReceivedPresenter createPresenter() {
    return new InvitationReceivedPresenter();
  }

  @Override
  protected void onPresenterCreated() {
    super.onPresenterCreated();

    doNothing().when(presenter).attachView(any(IInvitationReceived.class));
  }

  @Test
  public void nullUri_navigatesToLauncher(){
    App.appComponent.signInPreferences().clear();

    doReturn(null).when(view).getInvitedByUserId();

    initPresenter();

    presenter.decideNextScreen(view);

    verify(view).navigateToLauncher();
  }

  @Test
  public void nullUri_invokesFinish(){
    App.appComponent.signInPreferences().clear();

    doReturn(null).when(view).getInvitedByUserId();

    initPresenter();

    presenter.decideNextScreen(view);

    verify(view).finish();
  }

  @Test
  public void noUser_navigatesToLoginWithUserId() {
    App.appComponent.signInPreferences().clear();

    String expectedUid = "expec uid";
    doReturn(expectedUid)
            .when(view)
            .getInvitedByUserId();

    initPresenter();

    presenter.decideNextScreen(view);

    verify(view).navigateToLogin(eq(expectedUid));
  }

  @Test
  public void noUser_invokesFinish() {
    App.appComponent.signInPreferences().clear();

    String expectedUid = "expec uid";
    doReturn(expectedUid)
            .when(view)
            .getInvitedByUserId();

    initPresenter();

    presenter.decideNextScreen(view);

    verify(view).finish();
  }

  @Test
  public void existingUser_navigatesToAcceptInvitation() {
    doReturn(true).when(App.appComponent.signInPreferences()).hasGroupId();

    String expectedUid = "expec uid";
    doReturn(expectedUid)
            .when(view)
            .getInvitedByUserId();

    initPresenter();

    presenter.decideNextScreen(view);

    verify(view).navigateToAcceptInvitation(eq(expectedUid));
  }

  @Test
  public void existingUser_invokesFinish() {
    doReturn(true).when(App.appComponent.signInPreferences()).hasGroupId();

    String expectedUid = "expec uid";
    doReturn(expectedUid)
            .when(view)
            .getInvitedByUserId();

    initPresenter();

    presenter.decideNextScreen(view);

    verify(view).finish();
  }
}