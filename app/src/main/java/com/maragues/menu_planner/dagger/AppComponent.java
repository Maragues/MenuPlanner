package com.maragues.menu_planner.dagger;

import android.content.Context;

import com.maragues.menu_planner.model.providers.IRecipeProvider;
import com.maragues.menu_planner.model.providers.IUserProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by miguelaragues on 22/12/16.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(
        modules = {
                AppModule.class,
                ProvidersModule.class
        })
public interface AppComponent {
  Context context();

  IRecipeProvider recipeProvider();
  IUserProvider userProvider();
}
