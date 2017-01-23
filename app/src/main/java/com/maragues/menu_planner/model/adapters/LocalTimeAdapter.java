package com.maragues.menu_planner.model.adapters;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import me.mattlogan.auto.value.firebase.adapter.TypeAdapter;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class LocalTimeAdapter implements TypeAdapter<LocalTime, String> {
  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  @Override
  public LocalTime fromFirebaseValue(String value) {
    return LocalTime.parse(value, FORMATTER);
  }

  @Override
  public String toFirebaseValue(LocalTime value) {
    return value.format(FORMATTER);
  }
}
