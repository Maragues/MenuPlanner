package com.maragues.menu_planner.model.providers;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.SQLBriteHelper;

import java.util.List;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import rx.functions.Func1;


/**
 * Created by miguelaragues on 22/12/16.
 */

abstract class BaseProviderDelight<T extends ISynchronizable> implements IProvider<T> {

  @Override
  public int count() {
    Cursor cursor = null;
    try {
      cursor = SQLBriteHelper.getInstance(App.appComponent.context())
              .query("SELECT count(*) from "+getTableName());

      cursor.moveToFirst();

      return cursor.getInt(0);
    } finally {
      if (cursor != null && !cursor.isClosed())
        cursor.close();
    }
  }

  @NonNull
  @Override
  public Observable<List<T>> list() {
    return RxJavaInterop.toV2Observable(SQLBriteHelper.getInstance(App.appComponent.context())
            .createQuery(getTableName(), getSelectNonDeletedQuery())
            .mapToList(getMapperFunc()));
  }

  @Nullable
  @Override
  public Observable<T> get(long id) {
    return RxJavaInterop.toV2Observable(SQLBriteHelper.getInstance(App.appComponent.context())
            .createQuery(getTableName(), getByIdQuery(), new String[]{String.valueOf(id)})
            .mapToOneOrDefault(
                    getMapperFunc(),
                    null
            ));
  }

  @NonNull
  abstract String getByIdQuery();

  abstract Func1<Cursor, T> getMapperFunc();

  @NonNull
  abstract String getSelectNonDeletedQuery();

  @NonNull
  abstract String getTableName();
}
