package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.providers.IMealProvider;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealProviderFirebase extends BaseListableFirebaseProvider<Meal> implements IMealProvider {

  public MealProviderFirebase() {
    super(Meal.class);
  }

  @Override
  protected Query listQuery() {
    return null;
  }

  @Override
  protected Meal snapshotToInstance(DataSnapshot dataSnapshot) {
    return Meal.create(dataSnapshot);
  }

  @Override
  protected Map<String, Object> synchronizableToMap(Meal meal) {
    if (App.appComponent.textUtils().isEmpty(meal.id()))
      throw new IllegalArgumentException("Meal must have key");

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + MEALS_KEY
                    + "/" + App.appComponent.userProvider().getGroupId()
                    + "/" + meal.id(),
            meal.toFirebaseValue()
    );

    return childUpdates;
  }

  @Override
  public Single<Meal> create(@NonNull Meal newMeal) {
    return Single.create(new SingleOnSubscribe<Meal>() {
      @Override
      public void subscribe(SingleEmitter<Meal> e) throws Exception {
        Meal meal = assignKey(newMeal);

        getReference().updateChildren(synchronizableToMap(meal), (databaseError, databaseReference) -> {
          if (databaseError != null)
            e.onError(databaseError.toException());
          else {
            e.onSuccess(meal);
          }
        });
      }
    });
  }

  Meal assignKey(Meal meal) {
    String key = getReference().child(MEALS_KEY)
            .child(App.appComponent.userProvider().getGroupId())
            .push().getKey();

    return meal.withId(key);
  }

  /*
  "meals_group": {
      "$groupId": {
        "$mealId": {
          ".read": "auth != null && auth.guid == $groupId",
          ".write": "auth != null && (!data.exists() || auth.guid === $groupId)",
          "groupId": {
            ".validate": "(data.exists() && data.val() == newData.val()) || newData.val() == $groupId"
          }
        }
      }

  public Map<String, Object> toMap(@NonNull Meal mealInstance) {
    HashMap<String, Object> result = new HashMap<>();
    result.put(BaseFirebaseKeys.USER_ID_KEY, mealInstance.uid());

    Map<String, Object> recipes = new HashMap<>();

    for (int i = 0; i < mealInstance.recipes().size(); i++) {
      RecipeMeal recipeMeal = mealInstance.recipes().get(i);

      recipes.put(recipeMeal.recipeId(), recipeMeal.toFirebaseValue());
    }

    if (!recipes.isEmpty()) {
      result.put(RECIPES, recipes);
    }

    return result;
  }
*/

  static final String MEALS_KEY = "meals_group";
  private static final String RECIPES = "recipes";
}
