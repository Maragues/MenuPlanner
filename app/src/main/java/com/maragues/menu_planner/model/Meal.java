package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

/**
 * Created by maragues on 08/12/2016.
 * <p>
 * - Recipes, 1 or more
 * - Tags (quick, diet, parties, etc.)
 * - Comments (N by any group user)
 * - ¿Score? (1 by any group user)
 * - Total time/Preparation time. You may profit from parallelizing recipes
 * - Occurrences: list/calendar of when this meal has been eaten
 */

@AutoValue
@FirebaseValue
public abstract class Meal implements ISynchronizable<Meal> {
  @Nullable //so that we can create before having a key. The server won't accept empty id
  public abstract String id();

  @Nullable //so that we can represent user-recipes
  public abstract String uid();

  @Nullable //so that we can represent user-recipes
  public abstract String groupId();

  @NonNull
  public abstract List<RecipeMeal> recipes();

  public static Meal.Builder builder() {
    return new AutoValue_Meal.Builder();
  }

  public static Meal emptyMeal() {
    return builder().setRecipes(new ArrayList<>()).build();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Meal.Builder setId(String value);

    public abstract Meal.Builder setUid(String value);

    public abstract Meal.Builder setGroupId(String value);

    public abstract Meal.Builder setRecipes(List<RecipeMeal> value);

    public abstract Meal build();
  }

  public static Meal create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_Meal.FirebaseValue.class).toAutoValue();
  }

  public abstract Meal withUid(String id);

  public abstract Meal withGroupId(String id);

  public abstract Meal withRecipes(List<RecipeMeal> recipes);

  public Meal withNewRecipe(@NonNull Recipe recipe) {
    List<RecipeMeal> recipes = recipes();

    recipes.add(RecipeMeal.fromRecipe(recipe));

    return withRecipes(recipes);
  }
}
