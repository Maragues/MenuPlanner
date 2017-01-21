package com.maragues.menu_planner.ui.test;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.ui.common.BasePresenter;
import com.maragues.menu_planner.ui.common.IBaseview;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by miguelaragues on 3/1/17.
 */
@RunWith(MockitoJUnitRunner.class)
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
  }

  protected final void initPresenter() {
    presenter.create();

    presenter.attachView(view);
  }
}
