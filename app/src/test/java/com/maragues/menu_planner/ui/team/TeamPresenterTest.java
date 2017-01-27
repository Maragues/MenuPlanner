package com.maragues.menu_planner.ui.team;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.test.factories.GroupFactory;
import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import io.reactivex.Flowable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 27/1/17.
 */
public class TeamPresenterTest extends BasePresenterTest<ITeam, TeamPresenter> {

  public TeamPresenterTest() {
    super(ITeam.class);
  }

  @NonNull
  @Override
  protected TeamPresenter createPresenter() {
    return new TeamPresenter();
  }

  @Test
  public void create_loadsGroup() {
    initPresenter();

    verify(presenter).loadGroup();
  }

  @Test
  public void loadGroup_requestsUserGroup() {
    initPresenter();

    verify(App.appComponent.groupProvider()).getUserGroup();
  }

  @Test
  public void loadGroup_storesGroup() {
    initPresenter();

    assertNotNull(presenter.group);
  }

  /*
  ON GROUP LOADED
   */
  @Test
  public void onUsersLoaded__emptyGroup_showsEmptyUsers() {
    //so that there are no invocations to loadUser
    doReturn(Flowable.just(GroupFactory.base())).when(App.appComponent.groupProvider()).getUserGroup();

    initPresenter();
    ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

    verify(presenter).onUsersLoaded(captor.capture());

    assertEquals(0, captor.getValue().size());
  }

  @Test
  public void onUsersLoaded_showsUsers() {
    //so that there are no invocations to loadUser
    doReturn(Flowable.just(GroupFactory.baseWithAdmin())).when(App.appComponent.groupProvider()).getUserGroup();

    initPresenter();

    verify(presenter).onUsersLoaded(anyList());
  }
}