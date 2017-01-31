package com.maragues.menu_planner.test.dagger;

import android.app.Application;
import android.content.Context;

import com.maragues.menu_planner.test.mock.MockClock;
import com.maragues.menu_planner.test.mock.MockConnectivity;
import com.maragues.menu_planner.utils.Connectivity;
import com.maragues.menu_planner.utils.LocalTextUtils;

import org.threeten.bp.Clock;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by miguelaragues on 28/1/17.
 */

@Module
public class EspressoAppModule {

  Context mContext;

  public EspressoAppModule(Application application) {
    mContext = application;
  }

  @Provides
  @Singleton
  Context providesContext() {
    return mContext;
  }

  @Provides
  @Singleton
  LocalTextUtils providesLocalTextUtils() {
    return new LocalTextUtils();
  }

  @Provides
  @Singleton
  public Connectivity provideConnectivity() {
    return new MockConnectivity();
  }

  @Provides
  public Clock provideClock() {
    return MockClock.clock();
  }
}
