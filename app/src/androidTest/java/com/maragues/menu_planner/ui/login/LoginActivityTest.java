package com.maragues.menu_planner.ui.login;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.test.rules.ImmediateRxSchedulersOverrideRule;
import com.maragues.menu_planner.ui.test.MyActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Created by miguelaragues on 30/1/17.
 */
public class LoginActivityTest {

  @Rule
  public MyActivityTestRule<LoginActivity> activityRule = new MyActivityTestRule<>(LoginActivity.class, false);

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @Rule
  public final ImmediateRxSchedulersOverrideRule mOverrideSchedulersRule = new ImmediateRxSchedulersOverrideRule();

  @Test
  public void invitesLink_opensDeepLinkActivity() {
    App.appComponent.signInPreferences().clear();


//    activityRule.launchActivity();
  }
}