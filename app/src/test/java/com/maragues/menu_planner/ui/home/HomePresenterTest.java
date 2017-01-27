package com.maragues.menu_planner.ui.home;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.ui.test.BasePresenterTest;

import org.junit.Test;

import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 27/1/17.
 */
public class HomePresenterTest extends BasePresenterTest<IHome, HomePresenter> {

  public HomePresenterTest() {
    super(IHome.class);
  }

  @NonNull
  @Override
  protected HomePresenter createPresenter() {
    return new HomePresenter();
  }

  @Test
  public void clickHome_navigatesToTeam(){
    initPresenter();

    presenter.onTeamClicked();

    verify(view).navigateToTeamScreen();
  }
}