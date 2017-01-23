package com.maragues.menu_planner.model.providers.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Transaction;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.BaseFirebaseKeys;
import com.maragues.menu_planner.model.Group;
import com.maragues.menu_planner.model.User;
import com.maragues.menu_planner.test.mock.providers.MockUserProvider;

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

    doReturn(MockUserProvider.DEFAULT_UID).when(userInfo).getUid();
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
    verify(databaseReference).child(MockUserProvider.DEFAULT_UID);
  }

  @Test
  public void create_createsGroup() {
    TestObserver<User> observer = new TestObserver<>();

    DataSnapshot snapshot = mockUserDataSnapshot();

    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ((Transaction.Handler) invocation.getArgument(0)).onComplete(null, true, snapshot);

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
      Class<?> clazz = Class.forName("com.maragues.menu_planner.model.$$AutoValue_User$FirebaseValue");

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
