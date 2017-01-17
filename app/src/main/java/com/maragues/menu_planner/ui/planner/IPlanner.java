package com.maragues.menu_planner.ui.planner;

import com.maragues.menu_planner.ui.common.IBaseLoggedInView;

import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

/**
 * Created by miguelaragues on 13/1/17.
 */

interface IPlanner extends IBaseLoggedInView {

  @CallOnMainThread
  void askForMealInstanceLabel();
}
