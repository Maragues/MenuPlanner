package com.maragues.menu_planner.ui.test;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AppCompatActivity;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.test.dagger.DaggerEspressoAppComponent;
import com.maragues.menu_planner.test.mock.MockConnectivity;
import com.maragues.menu_planner.test.dagger.EspressoAppModule;

/**
 * Created by maragues on 25/07/16.
 */
public class MyActivityTestRule<T extends AppCompatActivity> extends ActivityTestRule<T> {

  private boolean connectivity = true;

  public MyActivityTestRule(Class<T> activityClass) {
    super(activityClass);
  }

  public MyActivityTestRule(Class<T> activityClass, boolean launchActivity) {
    super(activityClass, false, launchActivity);
  }

  @Override
  protected void beforeActivityLaunched() {
    super.beforeActivityLaunched();

    createAppComponent();

    MockConnectivity.setConnectivity(connectivity);
  }

  private void createAppComponent() {
    App.appComponent = DaggerEspressoAppComponent.builder()
            .espressoAppModule(new EspressoAppModule((Application) InstrumentationRegistry.getTargetContext().getApplicationContext()))
            .build();
  }

  public void setConnectivity(boolean connectivity){
    this.connectivity = connectivity;
  }
}
