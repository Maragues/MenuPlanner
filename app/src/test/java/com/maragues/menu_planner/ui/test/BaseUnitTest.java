package com.maragues.menu_planner.ui.test;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.test.dagger.DaggerUnitTestAppComponent;
import com.maragues.menu_planner.test.rules.ImmediateRxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Created by miguelaragues on 3/1/17.
 */

public abstract class BaseUnitTest {

  @Rule public MockitoRule rule = MockitoJUnit.rule();

  @Rule
  public final ImmediateRxSchedulersOverrideRule mOverrideSchedulersRule = new ImmediateRxSchedulersOverrideRule();

  public BaseUnitTest(){
    createAppComponent();
  }


  @Before
  public void setUp() {
  }



  @After
  public void tearDown() {

  }

  private void createAppComponent() {
    App.appComponent = DaggerUnitTestAppComponent.create();
  }
}
