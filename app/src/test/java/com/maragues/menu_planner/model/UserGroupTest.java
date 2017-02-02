package com.maragues.menu_planner.model;

import com.maragues.menu_planner.test.factories.UserFactory;
import com.maragues.menu_planner.test.factories.UserGroupFactory;
import com.maragues.menu_planner.ui.test.BaseUnitTest;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by miguelaragues on 27/1/17.
 */
public class UserGroupTest extends BaseUnitTest {

  @Test
  public void owner_isOwner() {
    assertTrue(UserGroupFactory.base().withStatus(Group.STATUS_OWNER).isOwner());
  }

  @Test
  public void ownser_isNotAdmin() {
    assertFalse(UserGroupFactory.base().withStatus(Group.STATUS_OWNER).isAdmin());
  }

  @Test
  public void ownser_isNotNotUser() {
    assertFalse(UserGroupFactory.base().withStatus(Group.STATUS_OWNER).isUser());
  }

  @Test
  public void admin_isAdmin() {
    assertTrue(UserGroupFactory.base().withStatus(Group.STATUS_ADMIN).isAdmin());
  }

  @Test
  public void admin_isNotUser() {
    assertFalse(UserGroupFactory.base().withStatus(Group.STATUS_ADMIN).isUser());
  }

  @Test
  public void admin_isNotOwner() {
    assertFalse(UserGroupFactory.base().withStatus(Group.STATUS_ADMIN).isOwner());
  }

  @Test
  public void user_isUser() {
    assertTrue(UserGroupFactory.base().withStatus(Group.STATUS_USER).isUser());
  }

  @Test
  public void user_isNotAdmin() {
    assertFalse(UserGroupFactory.base().withStatus(Group.STATUS_USER).isAdmin());
  }

  @Test
  public void user_isNotOwner() {
    assertFalse(UserGroupFactory.base().withStatus(Group.STATUS_USER).isOwner());
  }

  /*
  FROM USER
   */
  @Test
  public void fromUser_copiesFields() {
    UserGroup userGroup = UserGroup.fromUser(UserFactory.base(), Group.STATUS_USER);

    assertEquals(UserFactory.DEFAULT_NAME, userGroup.name());
    assertEquals(UserFactory.DEFAULT_UID, userGroup.id());
  }

  @Test
  public void fromUser_usesStatus() {
    UserGroup userGroup = UserGroup.fromUser(UserFactory.base(), Group.STATUS_USER);

    assertEquals(Group.STATUS_USER, userGroup.status());
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromUser_withEmptyName() {
    UserGroup.fromUser(UserFactory.base().withName(null), Group.STATUS_USER);
  }

  @Test(expected = IllegalArgumentException.class)
  public void fromUser_withEmptyId() {
    UserGroup.fromUser(UserFactory.base().withId(null), Group.STATUS_USER);
  }
}