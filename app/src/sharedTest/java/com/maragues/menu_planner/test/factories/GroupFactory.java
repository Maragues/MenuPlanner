package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.Group;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class GroupFactory {
  public static final String DEFAULT_GROUP_ID = "-KbB2nkoCRBzgqJ40mCe";

  private GroupFactory(){}

  public static Group base(){
    return Group.empty()
            .withId(DEFAULT_GROUP_ID);
  }

  public static Group baseWithAdmin(){
    return base().withNewRole(UserFactory.base(), Group.ADMIN_ROLE);
  }
}
