package com.maragues.menu_planner.ui.test;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.ui.BasePresenter;
import com.maragues.menu_planner.ui.IBaseview;

import org.junit.Before;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by miguelaragues on 3/1/17.
 */

public abstract class BasePresenterTest<V extends IBaseview, P extends BasePresenter<V>>
        extends BaseUnitTest {

  protected final P presenter;
  protected final V view;

  public BasePresenterTest(Class<V> viewClass) {
    view = mock(viewClass);
    presenter = spy(createPresenter());
  }

  @NonNull
  protected abstract P createPresenter();

  @Before
  public void setUp() {
    super.setUp();

    presenter.create();

    presenter.attachView(view);

    doReturn(view).when(presenter).getView();
  }
}
