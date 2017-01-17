package com.maragues.menu_planner.model.adapters;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import me.mattlogan.auto.value.firebase.adapter.TypeAdapter;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class LocalDateTimeAdapter implements TypeAdapter<LocalDateTime, Long> {
  @Override
  public LocalDateTime fromFirebaseValue(Long value) {
    return LocalDateTime.from(Instant.ofEpochMilli(value));
  }

  @Override
  public Long toFirebaseValue(LocalDateTime value) {
    return value.toEpochSecond(ZoneOffset.UTC) * 1000;
  }
}
