package com.maragues.menu_planner.utils;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;

/**
 * Created by miguelaragues on 24/1/17.
 */

public abstract class DateUtils {
  private DateUtils() {
  }

  public static LocalDateTime startOfWeek() {
    return startOfWeek(LocalDate.now());
  }

  public static LocalDateTime startOfWeek(@NonNull LocalDate week) {
    TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();

    return week.atStartOfDay().with(fieldISO, 1);
  }

  public static LocalDateTime endOfWeek(@NonNull LocalDate week) {
    TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();

    return endOfDay(week.atStartOfDay()).with(fieldISO, 7);
  }

  public static LocalDateTime endOfDay(@NonNull LocalDateTime dateTime) {
    return dateTime.withHour(23).withMinute(59).withSecond(59);
  }

  public static int weekOfYear(LocalDate date) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return date.get(weekFields.weekOfWeekBasedYear());
  }
}
