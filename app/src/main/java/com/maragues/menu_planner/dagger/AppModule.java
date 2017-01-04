package com.maragues.menu_planner.dagger;

import android.app.Application;
import android.content.Context;

import com.maragues.menu_planner.utils.Connectivity;
import com.maragues.menu_planner.utils.LocalTextUtils;

import org.threeten.bp.Clock;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by miguelaragues on 22/12/16.
 */
@Module
public class AppModule {

  Context context;

  public AppModule(Application application) {
    context = application;
  }

  @Provides
  @Singleton
  public Context providesContext() {
    return context;
  }

  @Provides
  @Singleton
  LocalTextUtils providesLocalTextUtils() {
    return new LocalTextUtils();
  }

  @Provides
  @Singleton
  public Connectivity provideConnectivity() {
    return new Connectivity();
  }

  @Provides
  public Clock provideClock() {
    return Clock.systemUTC();
  }
}
