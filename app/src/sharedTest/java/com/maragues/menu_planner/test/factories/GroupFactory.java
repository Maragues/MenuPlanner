package com.maragues.menu_planner.test.factories;

import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class GroupFactory {
  public static final String DEFAULT_GROUP_ID = "-KbB2nkoCRBzgqJ40mCe";
  public static final String DEFAULT_OWNER_ID = "IOwnThisWorld";

  private GroupFactory() {
  }

  public static Group base() {
    return Group.empty()
            .withId(DEFAULT_GROUP_ID);
  }

  public static Group baseWithOwner() {
    return baseWithOwner(DEFAULT_OWNER_ID);
  }

  public static Group baseWithOwner(String ownerId) {
    return baseWithOwner(UserFactory.base().withId(ownerId));
  }

  public static Group baseWithOwner(User owner) {
    return base().addWithRole(owner, Group.ROLE_OWNER);
  }
}
