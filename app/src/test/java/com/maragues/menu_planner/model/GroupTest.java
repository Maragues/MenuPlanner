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
    Group group = GroupFactory.baseWithOwner().withNewStatus(UserFactory.base().withId(uid), Group.STATUS_ADMIN);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.STATUS_ADMIN, group.users().get(uid).status());
  }

  @Test
  public void withNewRole_addUser_user() {
    String uid = "uiddd";
    Group group = GroupFactory.baseWithOwner().withNewStatus(UserFactory.base().withId(uid), Group.STATUS_USER);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.STATUS_USER, group.users().get(uid).status());
  }

  @Test
  public void withNewRole_addOwner_owner() {
    String uid = "uiddd";
    Group group = GroupFactory.base().withNewStatus(UserFactory.base().withId(uid), Group.STATUS_OWNER);

    assertTrue(group.users().keySet().contains(uid));
    assertEquals(Group.STATUS_OWNER, group.users().get(uid).status());
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_addOwner_withAlreadyAnOwner() {
    Group group = GroupFactory.base().withNewStatus(UserFactory.base().withId("ownerId"), Group.STATUS_OWNER);

    group.withNewStatus(UserFactory.base().withId("perryId"), Group.STATUS_OWNER);
  }

  @Test
  public void withNewRole_addUser() {
    Group group = GroupFactory.base().withNewStatus(UserFactory.base().withId("ownerId"), Group.STATUS_OWNER);

    assertEquals(1, group.users().size());

    group.withNewStatus(UserFactory.base().withId("perryId"), Group.STATUS_USER);

    assertEquals(2, group.users().size());
  }

  @Test
  public void withNewRole_addAdmin() {
    Group group = GroupFactory.base().withNewStatus(UserFactory.base().withId("ownerId"), Group.STATUS_OWNER);

    assertEquals(1, group.users().size());

    group.withNewStatus(UserFactory.base().withId("perryId"), Group.STATUS_ADMIN);

    assertEquals(2, group.users().size());
  }

  /*
  UPDATE STATUS
   */

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromOwner_toAdmin() {
    String ownerId = "blabla id";

    GroupFactory.baseWithOwner(ownerId).withNewStatus(UserFactory.base(ownerId), Group.STATUS_ADMIN);
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromOwner_toUser() {
    String ownerId = "blabla id";

    GroupFactory.baseWithOwner(ownerId).withNewStatus(UserFactory.base(ownerId), Group.STATUS_USER);
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromOwner_toPending() {
    String ownerId = "blabla id";

    GroupFactory.baseWithOwner(ownerId).withNewStatus(UserFactory.base(ownerId), Group.STATUS_PENDING);
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromAdmin_toOwner() {
    User user = UserFactory.base("blabla id");

    GroupFactory.baseWithOwner()
            .withNewStatus(user, Group.STATUS_ADMIN)
            .withNewStatus(user, Group.STATUS_OWNER);
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromUser_toOwner() {
    User user = UserFactory.base("blabla id");

    GroupFactory.baseWithOwner()
            .withNewStatus(user, Group.STATUS_USER)
            .withNewStatus(user, Group.STATUS_OWNER);
  }

  @Test(expected = IllegalStateException.class)
  public void withNewRole_fromPending_toOwner() {
    User user = UserFactory.base("blabla id");

    GroupFactory.baseWithOwner()
            .withNewStatus(user, Group.STATUS_PENDING)
            .withNewStatus(user, Group.STATUS_OWNER);
  }

  @Test
  public void withNewRole_fromUser_toAdmin() {
    String id = "blabla id";
    User user = UserFactory.base(id);

    Group group = GroupFactory.baseWithOwner()
            .withNewStatus(user, Group.STATUS_USER)
            .withNewStatus(user, Group.STATUS_ADMIN);

    assertEquals(Group.STATUS_ADMIN, group.getUser(id).status());
  }

  @Test
  public void withNewRole_fromAdmin_toUser() {
    String id = "blabla id";
    User user = UserFactory.base(id);

    Group group = GroupFactory.baseWithOwner()
            .withNewStatus(user, Group.STATUS_ADMIN)
            .withNewStatus(user, Group.STATUS_USER);

    assertEquals(Group.STATUS_USER, group.getUser(id).status());
  }

  @Test
  public void withNewRole_fromPending_toUser() {
    String id = "blabla id";
    User user = UserFactory.base(id);

    Group group = GroupFactory.baseWithOwner()
            .withNewStatus(user, Group.STATUS_PENDING)
            .withNewStatus(user, Group.STATUS_USER);

    assertEquals(Group.STATUS_USER, group.getUser(id).status());
  }

  @Test
  public void withNewRole_fromPending_toAdmin() {
    String id = "blabla id";
    User user = UserFactory.base(id);

    Group group = GroupFactory.baseWithOwner()
            .withNewStatus(user, Group.STATUS_PENDING)
            .withNewStatus(user, Group.STATUS_ADMIN);

    assertEquals(Group.STATUS_ADMIN, group.getUser(id).status());
  }

  @Test(expected = IllegalStateException.class)
  public void withoutOwner() {
    User user = UserFactory.base().withId("Weird user");
    GroupFactory.base().withNewStatus(user, Group.STATUS_USER);
  }

  /*
  UTILITY METHODS
   */
  @Test
  public void testOwner() {
    User owner = UserFactory.base().withId("Weird owner");
    Group group = GroupFactory.base().withNewStatus(owner, Group.STATUS_OWNER);

    assertEquals(UserGroup.fromUser(owner, Group.STATUS_OWNER), group.owner());
  }

  @Test
  public void testGetOwnerById() {
    String ownerID = "owneer Id";
    User owner = UserFactory.base(ownerID);
    Group group = GroupFactory.baseWithOwner(owner);

    assertEquals(UserGroup.fromUser(owner, Group.STATUS_OWNER), group.getUser(ownerID));
  }

  @Test
  public void testGetUserById() {
    String userId = "user Id";
    User user = UserFactory.base(userId);
    Group group = GroupFactory.baseWithOwner().withNewStatus(user, Group.STATUS_USER);

    assertEquals(UserGroup.fromUser(user, Group.STATUS_USER), group.getUser(userId));
  }
}