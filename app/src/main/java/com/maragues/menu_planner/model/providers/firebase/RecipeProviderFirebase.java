package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.IRecipeProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Observable;

/**
 * Created by miguelaragues on 28/12/16.
 */

public class RecipeProviderFirebase extends BaseProviderFirebase<Recipe> implements IRecipeProvider {
  @Override
  public Observable<List<Recipe>> list() {
    try {
      return RxFirebaseDatabase.observeSingleValueEvent(
              getReference().child(RECIPES_KEY),
              DataSnapshotMapper.listOf(Recipe.class)
      );
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void create(@NonNull Recipe recipe) {
//  public Observable<Recipe> create(@NonNull Recipe recipe) {
    String key = getReference().child(RECIPES_KEY).push().getKey();

    recipe = recipe.withId(key);

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + RECIPES_KEY + "/" + key, recipe.toMap());
//    childUpdates.put("/" + USER_RECIPES_KEY + "/" + recipe.uid() + "/" + key, recipe.toSummaryMap());

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
  static final String USER_RECIPES_KEY = "recipes_user";
}
