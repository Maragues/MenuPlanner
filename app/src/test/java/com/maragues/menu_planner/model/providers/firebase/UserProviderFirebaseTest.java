package com.maragues.menu_planner.model.providers.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.BaseFirebaseKeys;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.test.TestUtils;
import com.maragues.menu_planner.test.factories.UserFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 23/1/17.
 */

public class UserProviderFirebaseTest extends BaseProviderFirebaseTest<UserProviderFirebase> {
  @Mock
  UserInfo userInfo;

  @Before
  public void setup() {
    super.setUp();

    doReturn(UserFactory.DEFAULT_UID).when(userInfo).getUid();
  }

  @Override
  protected UserProviderFirebase createProvider() {
    return new UserProviderFirebase();
  }

  @Test
  public void create_usesCorrectPath() {
    doNothing().when(databaseReference).runTransaction(any(Transaction.Handler.class));

    TestObserver<User> observer = new TestObserver<>();

    provider.create(userInfo).subscribe(observer);

    verify(databaseReference).child(UserProviderFirebase.USERS_KEY);
    verify(databaseReference).child(UserFactory.DEFAULT_UID);
  }

  @Test
  public void create_createsGroup() {
    TestObserver<User> observer = new TestObserver<>();

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ((Transaction.Handler) invocation.getArgument(0)).onComplete(null, true, mockUserDataSnapshot());

        return null;
      }
    }).when(databaseReference).runTransaction(any(Transaction.Handler.class));

    provider.create(userInfo).subscribe(observer);

    verify(App.appComponent.groupProvider()).create(any(Group.class), any(User.class));
  }

  @Test
  public void create_assignsGroupIdToUser() {
    TestObserver<User> observer = new TestObserver<>();

    DataSnapshot snapshot = mockUserDataSnapshot();

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ((Transaction.Handler) invocation.getArgument(0)).onComplete(null, true, snapshot);

        return null;
      }
    }).when(databaseReference).runTransaction(any(Transaction.Handler.class));

    String expectedGroupId = "group id exp";

    mockFullTransaction(snapshot, expectedGroupId);

    provider.create(userInfo).subscribe(observer);

    User user = observer.values().get(0);
    assertNotNull(user);

    assertEquals(expectedGroupId, user.groupId());


  }

  @Captor
  private ArgumentCaptor<Map<String, Object>> captor;

  @Test
  public void create_updatesGroupId() {
    TestObserver<User> observer = new TestObserver<>();

    DataSnapshot snapshot = mockUserDataSnapshot();
    String expectedGroupId = "group id exp";

    mockFullTransaction(snapshot, expectedGroupId);

    provider.create(userInfo).subscribe(observer);

    verify(databaseReference).updateChildren(captor.capture());

    assertTrue(captor.getValue().keySet().contains(BaseFirebaseKeys.GROUP_ID_KEY));
    assertEquals(expectedGroupId, captor.getValue().get(BaseFirebaseKeys.GROUP_ID_KEY));
  }

  /*
  GET
   */
  @Test
  public void get_checksCorrectPath(){
    provider.get(UserFactory.DEFAULT_UID).subscribe();

    verify(databaseReference).child(eq(UserProviderFirebase.USERS_KEY));

    verify(databaseReference).child(eq(UserFactory.DEFAULT_UID));
  }

  @Test
  public void get_success_returningUser(){
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        DataSnapshot snapshot = mockUserDataSnapshot();

        doReturn(true).when(snapshot).exists();

        ((ValueEventListener) invocation.getArgument(0)).onDataChange(snapshot);

        return null;
      }
    }).when(databaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

    TestObserver<User> observer = new TestObserver<>();

    provider.get(UserFactory.DEFAULT_UID).subscribe(observer);

    observer.assertComplete();
    observer.assertValueCount(1);
  }

  @Test
  public void get_success_userDoesNotExist_invokesOnComplete(){
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        DataSnapshot snapshot = mockUserDataSnapshot();

        doReturn(false).when(snapshot).exists();

        ((ValueEventListener) invocation.getArgument(0)).onDataChange(snapshot);

        return null;
      }
    }).when(databaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

    TestObserver<User> observer = new TestObserver<>();

    provider.get(UserFactory.DEFAULT_UID).subscribe(observer);

    observer.assertComplete();
  }

  /*
  EXISTS
   */
  @Test
  public void exists_checksCorrectPath(){
    UserInfo userInfo = mock(UserInfo.class);
    doReturn(UserFactory.DEFAULT_UID).when(userInfo).getUid();

    provider.exists(userInfo).subscribe();

    verify(databaseReference).child(eq(UserProviderFirebase.USERS_KEY));

    verify(databaseReference).child(eq(UserFactory.DEFAULT_UID));
  }

  @Test
  public void exists_success_returnsTrue(){
    UserInfo userInfo = mock(UserInfo.class);
    doReturn(UserFactory.DEFAULT_UID).when(userInfo).getUid();

    boolean expectedResult = true;

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        DataSnapshot snapshot = mock(DataSnapshot.class);
        doReturn(expectedResult).when(snapshot).exists();

        ((ValueEventListener)invocation.getArgument(0)).onDataChange(snapshot);
        return null;
      }
    }).when(databaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

    TestObserver<Boolean> observer = new TestObserver<>();

    provider.exists(userInfo).subscribe(observer);

    observer.assertComplete();
    observer.assertValue(expectedResult);
  }

  @Test
  public void exists_success_returnsFalse(){
    UserInfo userInfo = mock(UserInfo.class);
    doReturn(UserFactory.DEFAULT_UID).when(userInfo).getUid();

    boolean expectedResult = false;

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        DataSnapshot snapshot = mock(DataSnapshot.class);
        doReturn(expectedResult).when(snapshot).exists();

        ((ValueEventListener)invocation.getArgument(0)).onDataChange(snapshot);
        return null;
      }
    }).when(databaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

    TestObserver<Boolean> observer = new TestObserver<>();

    provider.exists(userInfo).subscribe(observer);

    observer.assertComplete();
    observer.assertValue(expectedResult);
  }

  /*@Test
  public void exists_error_returnsFalse(){
    UserInfo userInfo = mock(UserInfo.class);
    doReturn(UserFactory.DEFAULT_UID).when(userInfo).getUid();

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        DatabaseError error = mock(DatabaseError.class);

        ((ValueEventListener)invocation.getArgument(0)).onCancelled(error);
        return null;
      }
    }).when(databaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

    TestObserver<Boolean> observer = new TestObserver<>();

    provider.exists(userInfo).subscribe(observer);

    observer.assertComplete();
    observer.assertValue(false);
  }*/

  private void mockFullTransaction(final DataSnapshot snapshot, String expectedGroupId) {
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ((Transaction.Handler) invocation.getArgument(0)).onComplete(null, true, snapshot);

        return null;
      }
    }).when(databaseReference).runTransaction(any(Transaction.Handler.class));

    Group groupWithId = Group.empty().withId(expectedGroupId);

    doReturn(Single.just(groupWithId)).when(App.appComponent.groupProvider()).create(any(Group.class), any(User.class));

    Task task = mock(Task.class);

    doReturn(task).when(databaseReference).updateChildren(anyMap());

    doAnswer(new Answer<Task>() {
      @Override
      public Task answer(InvocationOnMock invocation) throws Throwable {
        ((OnCompleteListener) invocation.getArgument(0)).onComplete(task);
        return null;
      }
    }).when(task).addOnCompleteListener(any(OnCompleteListener.class));
  }

  private DataSnapshot mockUserDataSnapshot() {
    DataSnapshot snapshot = mock(DataSnapshot.class);

    try {
      Class<?> clazz = TestUtils.getFirebaseValueClass(User.class);

      Constructor<?> constructor = clazz.getDeclaredConstructor();

      //we need to make constructor accessible
      constructor.setAccessible(true);

      //and pass instance of Outer class as first argument
      Object o = constructor.newInstance();

      doReturn(o).when(snapshot).getValue((Class<Object>) any());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return snapshot;
  }
}
