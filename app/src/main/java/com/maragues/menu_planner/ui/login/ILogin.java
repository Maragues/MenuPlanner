package com.maragues.menu_planner.ui.login;

import com.maragues.menu_planner.ui.common.IBaseview;

import net.grandcentrix.thirtyinch.distinctuntilchanged.DistinctUntilChanged;

/**
 * Created by miguelaragues on 13/1/17.
 */

public interface ILogin extends IBaseview {
  void hideProgressBar();

  @DistinctUntilChanged
  void navigateToHome();

  void finish();
}
