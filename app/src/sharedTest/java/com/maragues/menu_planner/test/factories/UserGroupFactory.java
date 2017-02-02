package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.UserGroup;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class UserGroupFactory {
  public static final String DEFAULT_ROLE = Group.STATUS_ADMIN;

  private UserGroupFactory() {
  }

  public static UserGroup base() {
    return UserGroup.fromUser(UserFactory.base(), DEFAULT_ROLE);
  }
}
