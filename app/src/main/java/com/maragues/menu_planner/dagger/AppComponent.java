package com.maragues.menu_planner.dagger;

import android.content.Context;

import dagger.Component;

/**
 * Created by miguelaragues on 22/12/16.
 */
@Component(
        modules = {
                AppModule.class
        })
public interface AppComponent {
  Context context();
}
