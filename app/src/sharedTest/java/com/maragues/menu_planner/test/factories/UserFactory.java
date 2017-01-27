package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.User;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class UserFactory {
  public static final String DEFAULT_UID = "E7uvIt9HU3dJk5ozpFfc8u2DUk72";
  public static final String DEFAULT_NAME = "Kokito kolega";

  private UserFactory(){}

  public static User base(){
    return User.empty().withId(DEFAULT_UID).withName(DEFAULT_NAME);
  }
}
