package com.maragues.menu_planner.model.providers.firebase;

import com.google.firebase.database.DatabaseReference;
import com.maragues.menu_planner.ui.test.BaseUnitTest;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by miguelaragues on 23/1/17.
 */

public abstract class BaseProviderFirebaseTest<T extends BaseProviderFirebase> extends BaseUnitTest {
  protected T provider;

  @Mock
  protected DatabaseReference databaseReference;

  @Before
  public void setUp() {
    super.setUp();

    provider = spy(createProvider());

    //otherwise since it's immutable we can't verify
    doReturn(databaseReference).when(databaseReference).child(anyString());
    doReturn(databaseReference).when(databaseReference).orderByKey();
    doReturn(databaseReference).when(databaseReference).startAt(any());
    doReturn(databaseReference).when(databaseReference).startAt(anyLong());
    doReturn(databaseReference).when(databaseReference).startAt(anyDouble());
    doReturn(databaseReference).when(databaseReference).endAt(any());
    doReturn(databaseReference).when(databaseReference).endAt(anyLong());
    doReturn(databaseReference).when(databaseReference).endAt(anyDouble());

    doReturn(databaseReference).when(provider).getReference();
  }

  protected void mockSingleResponse() {
    doAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        DatabaseReference.CompletionListener listener = (DatabaseReference.CompletionListener) invocation.getArguments()[1];

        listener.onComplete(null, mock(DatabaseReference.class));

        return null;
      }
    }).when(databaseReference).updateChildren(anyMap(), any(DatabaseReference.CompletionListener.class));
  }

  protected abstract T createProvider();

}
