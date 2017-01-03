package com.maragues.menu_planner.ui;

import net.grandcentrix.thirtyinch.TiActivity;
import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.TiView;

import butterknife.ButterKnife;

/**
 * Created by miguelaragues on 28/12/16.
 */

public abstract class BaseActivity<P extends TiPresenter<V>, V extends TiView> extends TiActivity<P, V> {

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);

    ButterKnife.bind(this);
  }
}
