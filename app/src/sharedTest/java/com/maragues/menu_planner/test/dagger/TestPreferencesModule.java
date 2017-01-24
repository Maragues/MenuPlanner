package com.maragues.menu_planner.test.dagger;

import com.maragues.menu_planner.model.preferences.ISignInPreferences;
import com.maragues.menu_planner.test.mock.preferences.MockSignInPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.spy;

/**
 * Created by miguelaragues on 24/1/17.
 */
@Module
public class TestPreferencesModule {

  @Provides
  @Singleton
  ISignInPreferences provideSignInPreferences() {
    return spy(new MockSignInPreferences());
  }
}
