package com.maragues.menu_planner.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.maragues.menu_planner.MealModel;
import com.squareup.sqldelight.ColumnAdapter;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

/**
 * Created by maragues on 08/12/2016.
 */

@AutoValue
public abstract class Meal implements MealModel {
  private static final ColumnAdapter<ZonedDateTime, Long> ZONED_DATE_TIME_ADAPTER = new ColumnAdapter<ZonedDateTime, Long>() {
    @NonNull
    @Override
    public ZonedDateTime decode(Long databaseValue) {
      return ZonedDateTime.ofInstant(Instant.ofEpochMilli(databaseValue), ZoneOffset.UTC);
    }

    @Override
    public Long encode(@NonNull ZonedDateTime value) {
      return value.toInstant().toEpochMilli();
    }
  };

  public static final Factory<Meal> FACTORY = new Factory<>(AutoValue_Meal::new, ZONED_DATE_TIME_ADAPTER);

}
