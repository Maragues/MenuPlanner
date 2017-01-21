package com.maragues.menu_planner.test.mock.providers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IListableProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

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

  protected Single<T> createInternal(@NonNull T item) {
    items.add(item);

    return Single.just(item);
  }

  @Nullable
  @Override
  public Single<T> get(@NonNull String id) {
    for (T item : items) {
      if (item.id().equals(id)) return Single.just(item);
    }

    return null;
  }

  public void reset() {
    items.clear();
  }
}
