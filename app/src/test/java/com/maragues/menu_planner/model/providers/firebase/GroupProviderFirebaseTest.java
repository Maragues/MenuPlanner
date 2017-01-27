package com.maragues.menu_planner.model.providers.firebase;

import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.UserFactory;

import org.junit.Test;

import java.util.Map;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

/**
 * Created by miguelaragues on 23/1/17.
 */
public class GroupProviderFirebaseTest extends BaseProviderFirebaseTest<GroupProviderFirebase> {

  @Override
  protected GroupProviderFirebase createProvider() {
    return new GroupProviderFirebase();
  }

  /*
  CREATE SUBSCRIPTION
   */
  @Test
  public void create_assingsKey() {
    Group group = GroupFactory.base();
    String id = "myId";
    User user = UserFactory.base().withId(id);

    String expectedKey = "my key";
    doReturn(group.withId(expectedKey)).when(provider).assignKey(eq(group));

    mockSingleResponse();

    TestObserver<Group> observer = new TestObserver<>();

    provider.create(group, user).subscribe(observer);

    observer.assertComplete();
    assertEquals(expectedKey, observer.values().get(0).id());
  }

  @Test
  public void create_assingsOwnerRole() {
    Group group = GroupFactory.base();
    String userId = "myId";
    User user = UserFactory.base().withId(userId);

    String expectedKey = "my key";
    doReturn(group.withId(expectedKey)).when(provider).assignKey(eq(group));

    mockSingleResponse();

    TestObserver<Group> observer = new TestObserver<>();

    provider.create(group, user).subscribe(observer);

    Group createdGroup = observer.values().get(0);
    assertTrue(createdGroup.users().containsKey(userId));
    assertEquals(Group.OWNER_ROLE, createdGroup.users().get(userId).role());
  }

  /*
  SYNCHRONIZABLE TO MAP
   */
  @Test(expected = IllegalArgumentException.class)
  public void synchronizableToMap_noIdThrowsIllegalArgumentException() {
    provider.synchronizableToMap(Group.empty());
  }

  @Test
  public void synchronizableToMap_returnsMap() {
    Group group = Group.empty().withId("bla");
    Map<String, Object> map = provider.synchronizableToMap(group);

    assertNotNull(map);
  }

  @Test
  public void synchronizableToMap_usesCorrectPath() {
    String key = "blabla";
    Group group = Group.empty().withId(key);
    Map<String, Object> map = provider.synchronizableToMap(group);

    String expectedPath = "/" + GroupProviderFirebase.GROUPS_KEY + "/" + key;
    map.containsKey(expectedPath);
  }

  @Test
  public void synchronizableToMap_createsObjectForPath() {
    String expectedId = "my id";
    Group group = Group.empty().withId(expectedId);

    Map<String, Object> map = provider.synchronizableToMap(group);
    String expectedPath = "/" + GroupProviderFirebase.GROUPS_KEY + "/" + expectedId;

    assertNotNull(map.get(expectedPath));
  }

/*
  @Test
  public void synchronizableToMap_groupWithAdmin() {
    String userId = "userIddd";
    Group group = Group.empty().withId("bla").withNewRole(User.empty().withId(userId), Group.ADMIN_ROLE);

    Map<String, Object> map = provider.synchronizableToMap(group);

    assertTrue(map.keySet().contains(userId));

    assertEquals(Group.ADMIN_ROLE, map.get(userId));
  }
*/
}