package com.maragues.menu_planner.model;

import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.UserFactory;
import com.maragues.menu_planner.ui.test.BaseUnitTest;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class GroupTest extends BaseUnitTest {
  @Test
  public void testEmpty() {
    assertNotNull(GroupFactory.base());
  }

  @Test
  public void empty_hasZeroUsers() {
    assertTrue(GroupFactory.base().users().isEmpty());
  }

  @Test
  public void withNewRole_addUser_admin() {
    String uid = "uiddd";
    Group group = GroupFactory.baseWithOwner().addWithRole(UserFactory.base().withId(uid), Group.ROLE_ADMIN);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.ROLE_ADMIN, group.users().get(uid).role());
  }

  @Test
  public void withNewRole_addUser_user() {
    String uid = "uiddd";
    Group group = GroupFactory.baseWithOwner().addWithRole(UserFactory.base().withId(uid), Group.ROLE_USER);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.ROLE_USER, group.users().get(uid).role());
  }

  @Test
  public void withNewRole_addOwner_owner() {
    String uid = "uiddd";
    Group group = GroupFactory.base().addWithRole(UserFactory.base().withId(uid), Group.ROLE_OWNER);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.ROLE_OWNER, group.users().get(uid).role());
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_addOwner_withAlreadyAnOwner() {
    Group group = GroupFactory.base().addWithRole(UserFactory.base().withId("ownerId"), Group.ROLE_OWNER);

    group.addWithRole(UserFactory.base().withId("perryId"), Group.ROLE_OWNER);
  }

  @Test
  public void withNewRole_addUser() {
    Group group = GroupFactory.base().addWithRole(UserFactory.base().withId("ownerId"), Group.ROLE_OWNER);

    assertEquals(1, group.users().size());

    group.addWithRole(UserFactory.base().withId("perryId"), Group.ROLE_USER);

    assertEquals(2, group.users().size());
  }

  @Test
  public void withNewRole_addAdmin() {
    Group group = GroupFactory.base().addWithRole(UserFactory.base().withId("ownerId"), Group.ROLE_OWNER);

    assertEquals(1, group.users().size());

    group.addWithRole(UserFactory.base().withId("perryId"), Group.ROLE_ADMIN);

    assertEquals(2, group.users().size());
  }

  /*
  UPDATE STATUS
   */

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromOwner_toAdmin() {
    String ownerId = "blabla id";

    GroupFactory.baseWithOwner(ownerId).addWithRole(UserFactory.base(ownerId), Group.ROLE_ADMIN);
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromOwner_toUser() {
    String ownerId = "blabla id";

    GroupFactory.baseWithOwner(ownerId).addWithRole(UserFactory.base(ownerId), Group.ROLE_USER);
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromAdmin_toOwner() {
    User user = UserFactory.base("blabla id");

    GroupFactory.baseWithOwner()
            .addWithRole(user, Group.ROLE_ADMIN)
            .addWithRole(user, Group.ROLE_OWNER);
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromUser_toOwner() {
    User user = UserFactory.base("blabla id");

    GroupFactory.baseWithOwner()
            .addWithRole(user, Group.ROLE_USER)
            .addWithRole(user, Group.ROLE_OWNER);
  }

  @Test
  public void withNewRole_fromUser_toAdmin() {
    String id = "blabla id";
    User user = UserFactory.base(id);

    Group group = GroupFactory.baseWithOwner()
            .addWithRole(user, Group.ROLE_USER)
            .addWithRole(user, Group.ROLE_ADMIN);

    assertEquals(Group.ROLE_ADMIN, group.getUser(id).role());
  }

  @Test
  public void withNewRole_fromAdmin_toUser() {
    String id = "blabla id";
    User user = UserFactory.base(id);

    Group group = GroupFactory.baseWithOwner()
            .addWithRole(user, Group.ROLE_ADMIN)
            .addWithRole(user, Group.ROLE_USER);

    assertEquals(Group.ROLE_USER, group.getUser(id).role());
  }

  @Test(expected = IllegalStateException.class)
  public void withoutOwner() {
    User user = UserFactory.base().withId("Weird user");
    GroupFactory.base().addWithRole(user, Group.ROLE_USER);
  }

  /*
  UTILITY METHODS
   */
  @Test
  public void testOwner() {
    User owner = UserFactory.base().withId("Weird owner");
    Group group = GroupFactory.base().addWithRole(owner, Group.ROLE_OWNER);

    assertEquals(UserGroup.fromUser(owner, Group.ROLE_OWNER), group.owner());
  }

  @Test
  public void testGetOwnerById() {
    String ownerID = "owneer Id";
    User owner = UserFactory.base(ownerID);
    Group group = GroupFactory.baseWithOwner(owner);

    assertEquals(UserGroup.fromUser(owner, Group.ROLE_OWNER), group.getUser(ownerID));
  }

  @Test
  public void testGetUserById() {
    String userId = "user Id";
    User user = UserFactory.base(userId);
    Group group = GroupFactory.baseWithOwner().addWithRole(user, Group.ROLE_USER);

    assertEquals(UserGroup.fromUser(user, Group.ROLE_USER), group.getUser(userId));
  }
}