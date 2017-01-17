package com.maragues.menu_planner.dagger;

import com.maragues.menu_planner.model.providers.IMealProvider;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;
import com.maragues.menu_planner.model.providers.IRecipeProvider;
import com.maragues.menu_planner.model.providers.IUserProvider;
import com.maragues.menu_planner.model.providers.firebase.MealProviderFirebase;
import com.maragues.menu_planner.model.providers.firebase.MealInstanceProviderFirebase;
import com.maragues.menu_planner.model.providers.firebase.RecipeProviderFirebase;
import com.maragues.menu_planner.model.providers.firebase.UserProviderFirebase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by miguelaragues on 30/12/16.
 */
@Module
public class ProvidersModule {

  @Provides
  @Singleton
  static IRecipeProvider providesRecipeProvider() {
    return new RecipeProviderFirebase();
  }

  @Provides
  @Singleton
  static IUserProvider providesUserProvider() {
    return new UserProviderFirebase();
  }

  @Provides
  @Singleton
  static IMealProvider providesMealProvider() {
    return new MealProviderFirebase();
  }

  @Provides
  @Singleton
  static IMealInstanceProvider providesMealSlotProvider() {
    return new MealInstanceProviderFirebase();
  }
}
