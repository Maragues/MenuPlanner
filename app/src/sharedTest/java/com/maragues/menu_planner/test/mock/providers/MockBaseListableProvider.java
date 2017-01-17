package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IListableProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by miguelaragues on 17/1/17.
 */

public abstract class MockBaseListableProvider<T extends ISynchronizable> extends MockBaseProvider<T>
        implements IListableProvider<T> {

  private List<T> items = new ArrayList<>();

  @NonNull
  @Override
  public Flowable<List<T>> list() {
    return Flowable.just(items);
  }

  @Override
  public Flowable<T> create(@NonNull T item) {
    items.add(item);

    return Flowable.just(item);
  }

  @Nullable
  @Override
  public Flowable<T> get(@NonNull String id) {
    for (T item : items) {
      if (item.id().equals(id)) return Flowable.just(item);
    }

    return null;
  }
}
