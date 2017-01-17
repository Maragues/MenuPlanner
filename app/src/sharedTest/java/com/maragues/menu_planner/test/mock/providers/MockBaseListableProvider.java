package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IListableProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public abstract class MockBaseListableProvider<T extends ISynchronizable> extends MockBaseProvider<T>
        implements IListableProvider<T> {

  private List<T> items = new ArrayList<>();

  @Override
  public Flowable<List<T>> list() {
    return Flowable.just(items);
  }

  @Override
  public void create(@NonNull T item) {
    items.add(item);
  }

  @Nullable
  @Override
  public Observable<T> get(@NonNull String id) {
    for (T item : items) {
      if (item.id().equals(id)) return Observable.just(item);
    }

    return null;
  }
}
