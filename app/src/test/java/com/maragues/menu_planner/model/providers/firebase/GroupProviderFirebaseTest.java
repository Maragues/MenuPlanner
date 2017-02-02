package com.maragues.menu_planner.model.providers.firebase;

import com.google.firebase.database.DatabaseReference;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.test.factories.UserFactory;

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
import static org.mockito.Mockito.doAnswer;

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
  public void create_assingedKeyEqualsUserId() {
    Group group = GroupFactory.base();
    String expectedId = "myId";
    User user = UserFactory.base().withId(expectedId);

    mockSingleResponse();

    TestObserver<Group> observer = new TestObserver<>();

    provider.create(group, user).subscribe(observer);

    observer.assertComplete();
    assertEquals(expectedId, observer.values().get(0).id());
  }

  @Test
  public void create_assingsOwnerRole() {
    Group group = GroupFactory.base();
    String userId = "myId";
    User user = UserFactory.base().withId(userId);

    mockSingleResponse();

    TestObserver<Group> observer = new TestObserver<>();

    provider.create(group, user).subscribe(observer);

    Group createdGroup = observer.values().get(0);
    assertTrue(createdGroup.users().containsKey(userId));
    assertEquals(Group.STATUS_OWNER, createdGroup.users().get(userId).status());
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_userWithNullIdThrowsException() {
    Group group = GroupFactory.base();
    User userWithoutId = UserFactory.base().withId(null);

    mockSingleResponse();

    TestObserver<Group> observer = new TestObserver<>();

    provider.create(group, userWithoutId).subscribe(observer);
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
  ADD USER TO GROUP
   */
  @Test
  public void update_WithNewUser() {
    Group group = GroupFactory.baseWithOwner();

    User user = UserFactory.base();

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ((DatabaseReference.CompletionListener) invocation.getArgument(1)).onComplete(null, databaseReference);

        return null;
      }
    }).when(databaseReference).updateChildren(anyMap(), any(DatabaseReference.CompletionListener.class));

    TestObserver<Group> observer = new TestObserver<>();

    provider.update(group.withNewStatus(user, Group.STATUS_PENDING)).subscribe(observer);

    observer.assertComplete();

    observer.assertValueCount(1);

    Group receivedGroup = observer.values().get(0);

    assertEquals(2, receivedGroup.users().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_withNoId() {
    provider.update(GroupFactory.base().withId("")).subscribe(new TestObserver<>());
  }
}