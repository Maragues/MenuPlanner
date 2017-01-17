package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.IRecipeProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static android.content.ContentValues.TAG;


/**
 * Created by miguelaragues on 28/12/16.
 */

public class RecipeProviderFirebase extends BaseListableFirebaseProvider<Recipe> implements IRecipeProvider {

  public RecipeProviderFirebase() {
    super(Recipe.class);
  }

  static class OnSubscribeFirebase implements ObservableOnSubscribe<List<Recipe>> {

    @Override
    public void subscribe(ObservableEmitter<List<Recipe>> e) throws Exception {
      FirebaseDatabase.getInstance().getReference()
              .child(USER_RECIPES_KEY)
              .child(App.appComponent.userProvider().getUid())
              .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                  Log.d(TAG, "On data change");
                  e.onNext(dataSnapshot.getValue(new GenericTypeIndicator<List<Recipe>>() {
                  }));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                  Log.d(TAG, "onCancelled");
                  if (databaseError.getCode() == DatabaseError.PERMISSION_DENIED)
                    Log.d(TAG, "PERMISSION_DENIED");
                  else
                    e.onError(databaseError.toException());

                  FirebaseDatabase.getInstance().getReference().child(RECIPES_KEY).removeEventListener(this);
                }
              });
    }
  }

  @Override
  protected Query listQuery() {
    return FirebaseDatabase.getInstance().getReference()
            .child(USER_RECIPES_KEY)
            .child(App.appComponent.userProvider().getUid());
  }

  @Override
  public void create(@NonNull Recipe recipe) {
//  public Observable<Recipe> create(@NonNull Recipe recipe) {
    String key = getReference().child(RECIPES_KEY).push().getKey();

    recipe = recipe.withId(key);

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + RECIPES_KEY + "/" + key, recipe.toMap());
    childUpdates.put("/" + USER_RECIPES_KEY + "/" + recipe.uid() + "/" + key, recipe.toSummaryMap());

    getReference().updateChildren(childUpdates);

    /*Single.create(new SingleOnSubscribe<Recipe>() {
      @Override
      public void subscribe(SingleEmitter<Recipe> e) throws Exception {

        return getReference().addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
            e.onSuccess(dataSnapshot.);
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        }).updateChildren(childUpdates);

      }
    })

    return Observable.fromCallable(c)
            .subscribeOn(Schedulers.computation());*/
  }

  static final String RECIPES_KEY = "recipes";
  public static final String USER_RECIPES_KEY = "recipes_user";
}
