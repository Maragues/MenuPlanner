package com.maragues.menu_planner.dagger;

import android.content.Context;

import com.maragues.menu_planner.model.preferences.ISignInPreferences;
import com.maragues.menu_planner.model.providers.IGroupProvider;
import com.maragues.menu_planner.model.providers.IMealProvider;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;
import com.maragues.menu_planner.model.providers.IRecipeProvider;
import com.maragues.menu_planner.model.providers.IUserProvider;
import com.maragues.menu_planner.utils.LocalTextUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by miguelaragues on 22/12/16.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(
        modules = {
                AppModule.class,
                PreferencesModule.class,
                ProvidersModule.class
        })
public interface AppComponent {
  Context context();

  IRecipeProvider recipeProvider();
  IUserProvider userProvider();
  IGroupProvider groupProvider();
  IMealProvider mealProvider();
  IMealInstanceProvider mealInstanceProvider();

  ISignInPreferences signInPreferences();

  LocalTextUtils textUtils();
}
