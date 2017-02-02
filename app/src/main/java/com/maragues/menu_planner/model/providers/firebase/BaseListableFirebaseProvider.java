package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.maragues.menu_planner.model.ISynchronizable;
import com.maragues.menu_planner.model.providers.IListableProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.exceptions.RxFirebaseDataException;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
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
    return list(createListQuery());
  }

  protected final Flowable<List<T>> list(@NonNull Query query) {
    return RxFirebaseDatabase.observeValueEvent(query, listMapper);
  }

  @Override
  public Maybe<T> get(@NonNull String id) {
    return Maybe.create(emitter ->
            createGetQuery(id).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                  emitter.onSuccess(snapshotToInstance(dataSnapshot));

                emitter.onComplete();
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {
                emitter.onError(new RxFirebaseDataException(databaseError));
              }
            }));
  }

  /*@Override
  public Completable create(@NonNull T item) {
    return Completable.create(emitter ->
            RxCompletableHandler.assignOnTask(emitter,
                    getReference().updateChildren(synchronizableToMap(item)))
    );
  }*/

  @NonNull
  protected abstract Map<String, Object> synchronizableToMap(T item);

  @NonNull
  protected abstract Query createListQuery();

  @NonNull
  protected abstract Query createGetQuery(String id);

  @NonNull
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
