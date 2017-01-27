package com.maragues.menu_planner.model;

import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.UserFactory;

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
    assertNotNull(GroupFactory.base());
  }

  @Test
  public void empty_hasZeroUsers() {
    assertTrue(GroupFactory.base().users().isEmpty());
  }

  @Test
  public void addUser_admin() {
    String uid = "uiddd";
    Group group = GroupFactory.base().withNewRole(UserFactory.base().withId(uid), Group.ADMIN_ROLE);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.ADMIN_ROLE, group.users().get(uid).role());
  }

  @Test
  public void addUser_user() {
    String uid = "uiddd";
    Group group = GroupFactory.base().withNewRole(UserFactory.base().withId(uid), Group.USER_ROLE);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.USER_ROLE, group.users().get(uid).role());
  }

  @Test
  public void addOwner_owner() {
    String uid = "uiddd";
    Group group = GroupFactory.base().withNewRole(UserFactory.base().withId(uid), Group.OWNER_ROLE);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.OWNER_ROLE, group.users().get(uid).role());
  }
}