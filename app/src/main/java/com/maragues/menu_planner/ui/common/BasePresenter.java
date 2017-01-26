package com.maragues.menu_planner.ui.common;

import net.grandcentrix.thirtyinch.TiPresenter;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by miguelaragues on 3/1/17.
 */

public abstract class BasePresenter<V extends IBaseview> extends TiPresenter<V> {

  protected CompositeDisposable disposables;

  @Override
  protected void onCreate() {
    super.onCreate();

    disposables = new CompositeDisposable();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (disposables != null)
      disposables.clear();
  }
}
