package com.maragues.menu_planner.model;

import android.support.annotation.StringRes;

import com.google.auto.value.AutoValue;
import com.google.firebase.database.DataSnapshot;
import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.adapters.LocalTimeAdapter;
import com.maragues.menu_planner.model.providers.IMealInstanceLabelProvider;

import org.threeten.bp.LocalTime;

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
  public static final MealInstanceLabel DINNER = builder()
          .setId(IMealInstanceLabelProvider.DINNER_ID)
          .setTime(LocalTime.of(20, 0))
          .build();

  public static final MealInstanceLabel LUNCH = builder()
          .setId(IMealInstanceLabelProvider.LUNCH_ID)
          .setTime(LocalTime.of(12, 0))
          .build();


  @FirebaseAdapter(LocalTimeAdapter.class)
  public abstract LocalTime time();

  public static MealInstanceLabel.Builder builder() {
    return new AutoValue_MealInstanceLabel.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract MealInstanceLabel.Builder setId(String value);

    public abstract MealInstanceLabel.Builder setTime(LocalTime value);

    public abstract MealInstanceLabel build();
  }

  public abstract MealInstanceLabel withId(String id);

  public abstract MealInstanceLabel withTime(LocalTime time);

  public static MealInstanceLabel create(DataSnapshot dataSnapshot) {
    return dataSnapshot
            .getValue(AutoValue_MealInstanceLabel.FirebaseValue.class)
            .toAutoValue()
            .withId(dataSnapshot.getKey());
  }

  @StringRes
  public int getLocalizedLabelResId() {
    if (id() != null) {
      switch (id()) {
        case IMealInstanceLabelProvider.DINNER_ID:
          return R.string.label_dinner;
        case IMealInstanceLabelProvider.LUNCH_ID:
          return R.string.label_lunch;
      }
    }

    return 0;
  }
}
