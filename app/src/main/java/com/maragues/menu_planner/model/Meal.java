package com.maragues.menu_planner.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;

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
public abstract class Meal implements ISynchronizable {

  @Nullable
  public abstract List<String> recipes();

  public static Meal.Builder builder() {
    return new AutoValue_Meal.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Meal.Builder setId(String value);

    public abstract Meal.Builder setRecipes(List<String> value);

    public abstract Meal build();
  }

  public static Meal create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_Meal.FirebaseValue.class).toAutoValue();
  }
}
