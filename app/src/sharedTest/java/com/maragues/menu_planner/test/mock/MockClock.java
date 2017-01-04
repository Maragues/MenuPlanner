package com.maragues.menu_planner.test.mock;

import android.support.annotation.NonNull;

import org.threeten.bp.Clock;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

/**
 * Created by maragues on 03/05/16.
 */
public class MockClock {
  private static Clock fixedClock;

  public static Clock clock() {
    if (fixedClock != null)
      return fixedClock;

    return Clock.systemUTC();
  }

  public static void setClock(@NonNull Clock clock) {
    fixedClock = clock;
  }

  public static void reset(){
    fixedClock = null;
  }

  public static void setFixedEpochInstant(long instantInMillis) {
    fixedClock = Clock.fixed(Instant.ofEpochMilli(instantInMillis), ZoneId.of("Z"));
  }

  public static void setFixedDate(@NonNull ZonedDateTime dateTime) {
    fixedClock = Clock.fixed(dateTime.toInstant(), dateTime.getZone());
  }
}
