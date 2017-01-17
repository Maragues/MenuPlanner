package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.IRecipeProvider;

import io.reactivex.Flowable;


/**
 * Created by miguelaragues on 28/12/16.
 */

public class RecipeProviderFirebase extends BaseListableFirebaseProvider<Recipe> implements IRecipeProvider {

  public RecipeProviderFirebase() {
    super(Recipe.class);
  }

  @Override
  protected Query listQuery() {
    return FirebaseDatabase.getInstance().getReference()
            .child(USER_RECIPES_KEY)
            .child(App.appComponent.userProvider().getUid());
  }

  /*public void create(@NonNull Recipe recipe) {
//  public Observable<Recipe> create(@NonNull Recipe recipe) {
    String key = getReference().child(RECIPES_KEY).push().getKey();

    recipe = recipe.withId(key);

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + RECIPES_KEY + "/" + key, recipe.toMap());
    childUpdates.put("/" + USER_RECIPES_KEY + "/" + recipe.uid() + "/" + key, recipe.toSummaryMap());

    getReference().updateChildren(childUpdates);

    *//*Single.create(new SingleOnSubscribe<Recipe>() {
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
            .subscribeOn(Schedulers.computation());*//*
  }*/

  @Override
  protected Recipe snapshotToInstance(DataSnapshot dataSnapshot) {
    return Recipe.create(dataSnapshot);
  }

  static final String RECIPES_KEY = "recipes";
  public static final String USER_RECIPES_KEY = "recipes_user";

  @Override
  public Flowable<Recipe> create(@NonNull Recipe item) {
    return null;
  }
}
