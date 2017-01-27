package com.maragues.menu_planner.ui.home;

import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

/**
 * Created by miguelaragues on 13/1/17.
 */

public class HomePresenter extends BaseLoggedInPresenter<IHome> {
  public void onTeamClicked() {
    if (getView() != null) {
      getView().navigateToTeamScreen();
    } else {
      sendToView(IHome::navigateToTeamScreen);
    }
  }
}
