package com.maragues.menu_planner.model.providers.firebase;

import com.google.firebase.database.DatabaseReference;
import com.maragues.menu_planner.ui.test.BaseUnitTest;

import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
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

    doReturn(databaseReference).when(provider).getReference();
  }

  protected abstract T createProvider();

}
