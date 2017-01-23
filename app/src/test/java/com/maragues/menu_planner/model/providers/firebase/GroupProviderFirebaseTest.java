package com.maragues.menu_planner.model.providers.firebase;

import com.google.firebase.database.DatabaseReference;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Map;

import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

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
    Group group = Group.empty();
    String id = "myId";
    User user = User.empty().withId(id);

    String expectedKey = "my key";
    doReturn(group.withId(expectedKey)).when(provider).assignKey(eq(group));

    mockSingleResponse();

    TestObserver<Group> observer = new TestObserver<>();

    provider.create(group, user).subscribe(observer);

    observer.assertComplete();
    assertEquals(expectedKey, observer.values().get(0).id());
  }

  @Test
  public void create_assingsRole() {
    Group group = Group.empty();
    String userId = "myId";
    User user = User.empty().withId(userId);

    String expectedKey = "my key";
    doReturn(group.withId(expectedKey)).when(provider).assignKey(eq(group));

    mockSingleResponse();

    TestObserver<Group> observer = new TestObserver<>();

    provider.create(group, user).subscribe(observer);

    Group createdGroup = observer.values().get(0);
    assertTrue(createdGroup.users().containsKey(userId));
    assertEquals(Group.ADMIN_ROLE, createdGroup.users().get(userId));
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

  private void mockSingleResponse() {
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        DatabaseReference.CompletionListener listener = (DatabaseReference.CompletionListener) invocation.getArguments()[1];

        listener.onComplete(null, mock(DatabaseReference.class));

        return null;
      }
    }).when(databaseReference).updateChildren(anyMap(), any(DatabaseReference.CompletionListener.class));
  }
}