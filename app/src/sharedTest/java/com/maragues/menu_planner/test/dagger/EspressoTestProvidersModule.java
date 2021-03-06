package com.maragues.menu_planner.test.dagger;

import com.maragues.menu_planner.model.providers.IGroupProvider;
import com.maragues.menu_planner.model.providers.IMealInstanceLabelProvider;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;
import com.maragues.menu_planner.model.providers.IMealProvider;
import com.maragues.menu_planner.model.providers.IRecipeProvider;
import com.maragues.menu_planner.model.providers.IUserProvider;
import com.maragues.menu_planner.test.mock.providers.MockGroupProvider;
import com.maragues.menu_planner.test.mock.providers.MockMealInstanceLabelProvider;
import com.maragues.menu_planner.test.mock.providers.MockMealInstanceProvider;
import com.maragues.menu_planner.test.mock.providers.MockMealProvider;
import com.maragues.menu_planner.test.mock.providers.MockRecipeProvider;
import com.maragues.menu_planner.test.mock.providers.MockUserProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by miguelaragues on 4/1/17.
 */
@Module
public class EspressoTestProvidersModule {

  @Provides
  @Singleton
  static IRecipeProvider providesRecipeProvider() {
    return (new MockRecipeProvider());
  }

  @Provides
  @Singleton
  static IUserProvider providesUserProvider() {
    return (new MockUserProvider());
  }

  @Provides
  @Singleton
  static IGroupProvider providesGroupProvider() {
    return (new MockGroupProvider());
  }

  @Provides
  @Singleton
  static IMealProvider providesMealProvider() {
    return (new MockMealProvider());
  }

  @Provides
  @Singleton
  static IMealInstanceProvider providesMealInstanceProvider() {
    return (new MockMealInstanceProvider());
  }
  @Provides
  @Singleton
  static IMealInstanceLabelProvider providesMealInstanceLabelProvider() {
    return (new MockMealInstanceLabelProvider());
  }
}
