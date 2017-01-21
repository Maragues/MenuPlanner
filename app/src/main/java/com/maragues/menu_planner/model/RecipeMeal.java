package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.maragues.menu_planner.App;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 21/1/17.
 */

@AutoValue
@FirebaseValue
public abstract class RecipeMeal implements ISynchronizable<RecipeMeal> {
  public abstract String recipeId();

  public abstract String name();


  public static RecipeMeal.Builder builder() {
    return new AutoValue_RecipeMeal.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract RecipeMeal.Builder setRecipeId(String value);

    public abstract RecipeMeal.Builder setName(String value);

    public abstract RecipeMeal build();
  }

  public static RecipeMeal create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_RecipeMeal.FirebaseValue.class).toAutoValue();
  }

  public abstract RecipeMeal withName(String name);

  public abstract RecipeMeal withRecipeId(String recipeId);

  static RecipeMeal fromRecipe(@NonNull Recipe recipe) {
    if (App.appComponent.textUtils().isEmpty(recipe.id()))
      throw new IllegalArgumentException("Recipe must have an id");

    return builder()
            .setName(recipe.name())
            .setRecipeId(recipe.id())
            .build();
  }
}
