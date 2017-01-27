package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.UserGroup;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class UserGroupFactory {
  public static final String DEFAULT_ROLE = Group.ADMIN_ROLE;

  private UserGroupFactory() {
  }

  public static UserGroup base() {
    return UserGroup.empty(UserFactory.DEFAULT_NAME, DEFAULT_ROLE);
  }
}
