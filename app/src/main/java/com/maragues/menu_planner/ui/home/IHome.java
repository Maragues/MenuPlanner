package com.maragues.menu_planner.ui.home;

import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

/**
 * Created by miguelaragues on 13/1/17.
 */

public interface IHome extends IBaseLoggedInView {
  @CallOnMainThread
  void navigateToTeamScreen();
}
