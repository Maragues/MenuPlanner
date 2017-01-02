package com.maragues.menu_planner.ui;

import com.arellomobile.mvp.MvpAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by miguelaragues on 28/12/16.
 */

public abstract class BaseActivity extends MvpAppCompatActivity {

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);

    ButterKnife.bind(this);
  }
}
