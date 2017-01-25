package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.IRecipeProvider;
import com.maragues.menu_planner.test.factories.RecipeFactory;

import io.reactivex.Single;

/**
 * Created by miguelaragues on 4/1/17.
 */

public class MockRecipeProvider extends MockBaseListableProvider<Recipe>
        implements IRecipeProvider {


  @Override
  public Single<Recipe> create(@NonNull Recipe item) {
    return super.createInternal(item);
  }

  @Override
  public Single<String> getKey() {
    return Single.just(RecipeFactory.RECIPE_ID);
  }
}
