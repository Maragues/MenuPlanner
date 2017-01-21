package com.maragues.menu_planner.model;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by miguelaragues on 21/1/17.
 */

@AutoValue
@FirebaseValue
public abstract class RecipeMeal {
  public abstract String recipeId();

  public abstract String name();

  public static RecipeMeal create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_RecipeMeal.FirebaseValue.class).toAutoValue();
  }

  public abstract RecipeMeal withName(String name);

  public abstract RecipeMeal withRecipeId(String recipeId);
}
