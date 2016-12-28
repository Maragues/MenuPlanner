package com.maragues.menu_planner.ui;

import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by miguelaragues on 28/12/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);

    ButterKnife.bind(this);
  }
}
