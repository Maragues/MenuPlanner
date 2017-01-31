package com.maragues.menu_planner.ui.launch;

import com.maragues.menu_planner.test.rules.ImmediateRxSchedulersOverrideRule;
import com.maragues.menu_planner.ui.test.MyActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static junit.framework.Assert.assertTrue;

/**
 * Created by miguelaragues on 28/1/17.
 */
@RunWith(android.support.test.runner.AndroidJUnit4.class)
public class LaunchActivityTest {
  @Rule
  public MyActivityTestRule<LaunchActivity> activityRule = new MyActivityTestRule<>(LaunchActivity.class);

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();

  @Rule
  public final ImmediateRxSchedulersOverrideRule mOverrideSchedulersRule = new ImmediateRxSchedulersOverrideRule();

  @Test
  public void invitesLink() {
    assertTrue(false);
  }
}