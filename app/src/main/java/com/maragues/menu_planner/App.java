package com.maragues.menu_planner;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.maragues.menu_planner.dagger.AppComponent;
import com.maragues.menu_planner.dagger.AppModule;
import com.maragues.menu_planner.dagger.DaggerAppComponent;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import net.grandcentrix.thirtyinch.TiConfiguration;
import net.grandcentrix.thirtyinch.TiPresenter;

import java.lang.ref.WeakReference;

/**
 * Created by maragues on 05/12/2016.
 */

public class App extends Application {

  public static AppComponent appComponent;

  RefWatcher refWatcher;

  @Override
  public void onCreate() {
    super.onCreate();

    launchInitTask();

    initFirebase();

    initTimezones();

    initDagger();

    initThirtyInch();
  }

  private void initFirebase() {
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
  }

  private void initThirtyInch() {
    TiPresenter.setDefaultConfig(TiConfiguration.DEFAULT);
  }

  protected void initTimezones() {
    AndroidThreeTen.init(this);
  }

  protected void initDagger() {
    appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();
  }

  @Nullable
  public static RefWatcher getRefWatcher(Context context) {
    return ((App) context.getApplicationContext()).refWatcher;
  }

  void launchInitTask() {
    new InitTask(this).execute();
  }

  static class InitTask extends AsyncTask<Void, Void, Boolean> {

    WeakReference<App> weakApp;

    InitTask(App application) {
      weakApp = new WeakReference<>(application);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      if (weakApp.get() != null) {
        if (!LeakCanary.isInAnalyzerProcess(weakApp.get()))
          weakApp.get().refWatcher = LeakCanary.install(weakApp.get());
      }

      return true;
    }
  }
}
