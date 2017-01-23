package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Recipe;
import com.maragues.menu_planner.model.providers.IRecipeProvider;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;


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

  @Override
  protected Map<String, Object> synchronizableToMap(@NonNull Recipe recipe) {
    if(App.appComponent.textUtils().isEmpty(recipe.id()))
      throw new IllegalArgumentException("Recipe must have key");

    if(App.appComponent.textUtils().isEmpty(recipe.groupId()))
      throw new IllegalArgumentException("Recipe must have groupId");

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + RECIPES_KEY + "/" + recipe.id(), recipe.toFirebaseValue());
    childUpdates.put("/" + USER_RECIPES_KEY + "/" + recipe.groupId() + "/" + recipe.id(), toSummaryMap(recipe));

    return childUpdates;
  }

  @Override
  protected Recipe snapshotToInstance(DataSnapshot dataSnapshot) {
    return Recipe.create(dataSnapshot);
  }

  @Override
  public Single<Recipe> create(@NonNull final Recipe item) {
    return Single.create(new SingleOnSubscribe<Recipe>() {
      @Override
      public void subscribe(SingleEmitter<Recipe> e) throws Exception {
        Recipe recipe = assignKey(item);

        getReference().updateChildren(synchronizableToMap(recipe), (databaseError, databaseReference) -> {
          if (databaseError != null)
            e.onError(databaseError.toException());
          else {
            e.onSuccess(recipe);
          }
        });
      }
    });
  }

  Recipe assignKey(Recipe recipe) {
    String key = getReference().child(RECIPES_KEY).push().getKey();

    return recipe.withId(key);
  }

   Map<String, Object> toSummaryMap(Recipe recipe) {
    HashMap<String, Object> result = new HashMap<>();
    result.put(NAME_KEY, recipe.name());

    if (!App.appComponent.textUtils().isEmpty(recipe.description()))
      result.put(SHORT_DESCRIPTION_KEY, recipe.shortDescription());

    return result;
  }

  static final String RECIPES_KEY = "recipes";
  public static final String USER_RECIPES_KEY = "recipes_user";
  public static final String NAME_KEY = "name";
  public static final String SHORT_DESCRIPTION_KEY = "shortDescription";
}
