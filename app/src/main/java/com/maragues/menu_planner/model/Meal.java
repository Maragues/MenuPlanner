package com.maragues.menu_planner.model;

import com.google.auto.value.AutoValue;

import org.threeten.bp.ZonedDateTime;

/**
 * Created by maragues on 08/12/2016.
 */

@AutoValue
public abstract class Meal {
  String name;
  ZonedDateTime createdAt, updatedAt;
  /*private static final ColumnAdapter<ZonedDateTime, Long> ZONED_DATE_TIME_ADAPTER = new ColumnAdapter<ZonedDateTime, Long>() {
    @NonNull
    @Override
    public ZonedDateTime decode(Long databaseValue) {
      return ZonedDateTime.ofInstant(Instant.ofEpochMilli(databaseValue), ZoneOffset.UTC);
    }

    @Override
    public Long encode(@NonNull ZonedDateTime value) {
      return value.toInstant().toEpochMilli();
    }
  };*/

}
