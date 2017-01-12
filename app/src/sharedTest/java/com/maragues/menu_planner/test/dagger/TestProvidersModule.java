package com.maragues.menu_planner.test.dagger;

import com.maragues.menu_planner.model.providers.IRecipeProvider;
import com.maragues.menu_planner.model.providers.IUserProvider;
import com.maragues.menu_planner.test.mock.providers.MockRecipeProvider;
import com.maragues.menu_planner.test.mock.providers.MockUserProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.spy;

/**
 * Created by miguelaragues on 4/1/17.
 */
@Module
public class TestProvidersModule {

  @Provides
  @Singleton
  static IRecipeProvider providesRecipeProvider() {
    return spy(new MockRecipeProvider());
  }

  @Provides
  @Singleton
  static IUserProvider providesUserProvider() {
    return spy(new MockUserProvider());
  }
}