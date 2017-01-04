package com.maragues.menu_planner.ui.test;

import com.maragues.menu_planner.App;

import org.junit.After;
import org.junit.Before;
import com.maragues.menu_planner.test.dagger.DaggerUnitTestAppComponent;

/**
 * Created by miguelaragues on 3/1/17.
 */

public abstract class BaseUnitTest {


  @Before
  public void setUp() {
    createAppComponent();
  }



  @After
  public void tearDown() {

  }

  private void createAppComponent() {
    App.appComponent = DaggerUnitTestAppComponent.create();
  }
}
