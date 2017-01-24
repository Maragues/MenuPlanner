package com.maragues.menu_planner.dagger;


import com.maragues.menu_planner.model.preferences.ISignInPreferences;
import com.maragues.menu_planner.model.preferences.SignInPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by maragues on 18/05/16.
 */
@Module
public class PreferencesModule {

  @Provides
  @Singleton
  ISignInPreferences provideSignInPreferences(){
    return new SignInPreferences();
  }

}
