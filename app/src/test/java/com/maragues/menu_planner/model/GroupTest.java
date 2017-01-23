package com.maragues.menu_planner.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class GroupTest {
  @Test
  public void testEmpty() {
    assertNotNull(Group.empty());
  }

  @Test
  public void empty_hasZeroUsers() {
    assertTrue(Group.empty().users().isEmpty());
  }

  @Test
  public void addUser_admin() {
    String uid = "uiddd";
    Group group = Group.empty().withNewRole(User.empty().withId(uid), Group.ADMIN_ROLE);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.ADMIN_ROLE, group.users().get(uid));
  }

  @Test
  public void addUser_user() {
    String uid = "uiddd";
    Group group = Group.empty().withNewRole(User.empty().withId(uid), Group.USER_ROLE);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.USER_ROLE, group.users().get(uid));
  }
}