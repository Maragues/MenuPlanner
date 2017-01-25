package com.maragues.menu_planner.test;

import com.maragues.menu_planner.model.ISynchronizable;

/**
 * Created by miguelaragues on 25/1/17.
 */

public abstract class TestUtils {
  private TestUtils() {
  }

  public static Class<?> getFirebaseValueClass(Class<? extends ISynchronizable> clazz) throws ClassNotFoundException {
    String className = clazz.getSimpleName();

    try {
      return Class.forName("com.maragues.menu_planner.model.$$AutoValue_" + className + "$FirebaseValue");
    } catch (ClassNotFoundException e) {
      //ignore the first time
    }

    return Class.forName("com.maragues.menu_planner.model.$AutoValue_" + className + "$FirebaseValue");
  }
}
