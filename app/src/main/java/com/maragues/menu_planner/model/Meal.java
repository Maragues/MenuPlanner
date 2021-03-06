package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
  public abstract String userId();

  @Nullable //so that we can represent user-recipes
  public abstract String groupId();

  @NonNull
  public abstract Map<String, RecipeMeal> recipes();

  public static Meal.Builder builder() {
    return new AutoValue_Meal.Builder();
  }

  public static Meal empty() {
    return builder().setRecipes(new HashMap<>()).build();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setId(String value);

    public abstract Builder setUserId(String value);

    public abstract Builder setGroupId(String value);

    public abstract Builder setRecipes(Map<String, RecipeMeal> value);

    public abstract Meal build();
  }

  public static Meal create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_Meal.FirebaseValue.class).toAutoValue();
  }

  public Object toFirebaseValue() {
    return new AutoValue_Meal.FirebaseValue(this);
  }

  public abstract Meal withUserId(String userId);

  public abstract Meal withGroupId(String groupId);

  public abstract Meal withRecipes(Map<String, RecipeMeal> recipes);

  public Meal withNewRecipe(@NonNull Recipe recipe) {
    Map<String, RecipeMeal> recipes = recipes();

    recipes.put(recipe.id(), RecipeMeal.fromRecipe(recipe));

    return withRecipes(recipes);
  }

  public Collection<RecipeMeal> recipeCollection(){
    return new ArrayList<>(recipes().values());
  }
}
