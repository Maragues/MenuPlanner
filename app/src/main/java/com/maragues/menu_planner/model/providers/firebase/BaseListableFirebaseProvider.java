package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IListableProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by miguelaragues on 17/1/17.
 */

public abstract class BaseListableFirebaseProvider<T extends ISynchronizable>
        extends BaseProviderFirebase<T>
        implements IListableProvider<T> {

  final ListMapper<T> listMapper;

  protected BaseListableFirebaseProvider(Class<T> clazz) {
    super(clazz);

    listMapper = new ListMapper<>(this::snapshotToInstance);
  }

  @NonNull
  @Override
  public Flowable<List<T>> list() {
    return list(listQuery());
  }

  protected final Flowable<List<T>> list(@NonNull Query query) {
    return RxFirebaseDatabase.observeValueEvent(query, listMapper);
  }

  @Nullable
  @Override
  public Single<T> get(@NonNull String id) {
    return RxFirebaseDatabase.observeSingleValueEvent(getReference(), this::snapshotToInstance)
            .singleOrError();
  }

  /*@Override
  public Completable create(@NonNull T item) {
    return Completable.create(emitter ->
            RxCompletableHandler.assignOnTask(emitter,
                    getReference().updateChildren(synchronizableToMap(item)))
    );
  }*/

  protected abstract Map<String, Object> synchronizableToMap(T item);

  protected abstract Query listQuery();

  protected abstract T snapshotToInstance(DataSnapshot dataSnapshot);

  private static class ListMapper<T extends ISynchronizable> implements Function<DataSnapshot, List<T>> {
    private final Function<? super DataSnapshot, T> mapper;

    protected ListMapper(@NonNull Function<? super DataSnapshot, T> mapper) {
      this.mapper = mapper;
    }

    @Override
    public List<T> apply(DataSnapshot dataSnapshot) throws Exception {
      List<T> items = new ArrayList<>();
      Iterator it = dataSnapshot.getChildren().iterator();

      while (it.hasNext()) {
        DataSnapshot childSnapshot = (DataSnapshot) it.next();
        items.add(mapper.apply(childSnapshot));
      }

      return items;
    }
  }
}
