package com.maragues.menu_planner.utils;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;

/**
 * Created by miguelaragues on 24/1/17.
 */

public abstract class DateUtils {
  private DateUtils() {
  }

  public static long weeksSinceEpoch(@NonNull LocalDateTime dateTime) {
    return ChronoUnit.WEEKS.between(LocalDate.ofEpochDay(0), dateTime);
  }
}
