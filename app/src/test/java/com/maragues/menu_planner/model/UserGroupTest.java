package com.maragues.menu_planner.model;

import com.maragues.menu_planner.test.factories.UserFactory;
import com.maragues.menu_planner.test.factories.UserGroupFactory;
import com.maragues.menu_planner.ui.test.BaseUnitTest;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by miguelaragues on 27/1/17.
 */
public class UserGroupTest extends BaseUnitTest {
  @Test
  public void testEmpty() {
    assertNotNull(UserGroup.empty(UserFactory.DEFAULT_NAME, Group.ADMIN_ROLE));
  }

  @Test
  public void owner_isOwner() {
    assertTrue(UserGroupFactory.base().withRole(Group.OWNER_ROLE).isOwner());
  }

  @Test
  public void ownser_isNotAdmin() {
    assertFalse(UserGroupFactory.base().withRole(Group.OWNER_ROLE).isAdmin());
  }

  @Test
  public void ownser_isNotNotUser() {
    assertFalse(UserGroupFactory.base().withRole(Group.OWNER_ROLE).isUser());
  }

  @Test
  public void admin_isAdmin() {
    assertTrue(UserGroupFactory.base().withRole(Group.ADMIN_ROLE).isAdmin());
  }

  @Test
  public void admin_isNotUser() {
    assertFalse(UserGroupFactory.base().withRole(Group.ADMIN_ROLE).isUser());
  }

  @Test
  public void admin_isNotOwner() {
    assertFalse(UserGroupFactory.base().withRole(Group.ADMIN_ROLE).isOwner());
  }

  @Test
  public void user_isUser() {
    assertTrue(UserGroupFactory.base().withRole(Group.USER_ROLE).isUser());
  }

  @Test
  public void user_isNotAdmin() {
    assertFalse(UserGroupFactory.base().withRole(Group.USER_ROLE).isAdmin());
  }

  @Test
  public void user_isNotOwner() {
    assertFalse(UserGroupFactory.base().withRole(Group.USER_ROLE).isOwner());
  }
}