package com.maragues.menu_planner;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.maragues.menu_planner.dagger.AppComponent;

/**
 * Created by maragues on 05/12/2016.
 */

public class App extends Application {

  public static AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    initTimezones();

    initDagger();
  }

  protected void initTimezones() {
    AndroidThreeTen.init(this);
  }

  protected void initDagger() {
    /*appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();*/
  }
}
