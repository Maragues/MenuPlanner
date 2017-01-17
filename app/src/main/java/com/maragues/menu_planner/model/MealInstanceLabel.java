package com.maragues.menu_planner.model;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.maragues.menu_planner.model.adapters.LocalDateTimeAdapter;

import org.threeten.bp.LocalDateTime;

import me.mattlogan.auto.value.firebase.adapter.FirebaseAdapter;
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
public abstract class MealInstanceLabel implements ISynchronizable {

  @Nullable
  @FirebaseAdapter(LocalDateTimeAdapter.class)
  public abstract LocalDateTime time();

  public static MealInstanceLabel.Builder builder() {
    return new AutoValue_MealInstanceLabel.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract MealInstanceLabel.Builder setId(String value);

    public abstract MealInstanceLabel.Builder setTime(LocalDateTime value);

    public abstract MealInstanceLabel build();
  }

  public static MealInstanceLabel create(DataSnapshot dataSnapshot) {
    return dataSnapshot.getValue(AutoValue_MealInstanceLabel.FirebaseValue.class).toAutoValue();
  }

}
