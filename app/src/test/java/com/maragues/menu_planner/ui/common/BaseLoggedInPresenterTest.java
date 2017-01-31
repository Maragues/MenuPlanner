package com.maragues.menu_planner.ui.common;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.test.factories.UserFactory;
import com.maragues.menu_planner.test.mock.providers.MockUserProvider;
import com.maragues.menu_planner.ui.home.IHome;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import io.reactivex.Maybe;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 28/1/17.
 */
public class BaseLoggedInPresenterTest extends BasePresenterTest<IHome, BaseLoggedInPresenterTest.StubLoggedInPresenter> {

  public BaseLoggedInPresenterTest() {
    super(IHome.class);
  }

  @Test
  public void userId_noGroupId_loadsUser() {
    ((MockUserProvider) App.appComponent.userProvider()).setGroupId(null);
    doReturn(Maybe.just(UserFactory.base()))
            .when(App.appComponent.userProvider())
            .get(anyString());

    initPresenter();

    verify(App.appComponent.userProvider()).get(eq(App.appComponent.userProvider().getUid()));
  }

  @Test
  public void userId_noGroupId_storesIncomingGroupId() {
    ((MockUserProvider) App.appComponent.userProvider()).setGroupId(null);

    String uid = App.appComponent.userProvider().getUid();
    String expectedGroupId = "My groupId";
    doReturn(Maybe.just(UserFactory.base().withGroupId(expectedGroupId)))
            .when(App.appComponent.userProvider())
            .get(eq(uid));

    initPresenter();

    verify(App.appComponent.signInPreferences()).saveGroupId(eq(expectedGroupId));
  }

  @NonNull
  @Override
  protected StubLoggedInPresenter createPresenter() {
    return new StubLoggedInPresenter();
  }

  static class StubLoggedInPresenter extends BaseLoggedInPresenter<IHome> {

  }
}