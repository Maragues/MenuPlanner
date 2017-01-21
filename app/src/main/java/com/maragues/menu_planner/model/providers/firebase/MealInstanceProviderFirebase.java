package com.maragues.menu_planner.model.providers.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.RecipeMeal;
import com.maragues.menu_planner.model.providers.IMealInstanceProvider;

import org.threeten.bp.ZoneOffset;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class MealInstanceProviderFirebase extends BaseListableFirebaseProvider<MealInstance>
        implements IMealInstanceProvider {


  public MealInstanceProviderFirebase() {
    super(MealInstance.class);
  }

  @Override
  protected Query listQuery() {
    return null;
  }

  @Override
  protected MealInstance snapshotToInstance(DataSnapshot dataSnapshot) {
    return MealInstance.create(dataSnapshot);
  }

  @Override
  protected Map<String, Object> synchronizableToMap(MealInstance item) {
    if (App.appComponent.textUtils().isEmpty(item.id()))
      throw new IllegalArgumentException("MealInstance must have key");

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + MEAL_INSTANCES_KEY
                    + "/" + App.appComponent.userProvider().getGroupId()
                    + "/" + item.weekNumber()
                    + "/" + item.dateTime().getDayOfWeek(),
            toMap(item)
    );

    return childUpdates;
  }

  @Override
  public Single<MealInstance> create(final MealInstance newMealInstance) {
    return Single.create(new SingleOnSubscribe<MealInstance>() {
      @Override
      public void subscribe(SingleEmitter<MealInstance> e) throws Exception {
        MealInstance meal = assignKey(newMealInstance);

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

  /*
  meal_instances {
    $groupId {
        23 (week number) {
            1 (week day) {
                $mealInstanceId: {
                    "label": "Lunch",
                    "mealId": #mealId,
                    "Puerros": true,
                    "Jarretes": true,
                }
   */
  private MealInstance assignKey(MealInstance mealInstance) {
    String key = getReference()
            .child(MEAL_INSTANCES_KEY)
            .child(App.appComponent.userProvider().getGroupId())
            .child(String.valueOf(mealInstance.weekNumber()))
            .child(String.valueOf(mealInstance.dateTime().getDayOfWeek()))
            .push().getKey();

    return mealInstance.withId(key);
  }

  /*
    $mealInstanceId: {
                    "label": "Lunch",
                    "mealId": #mealId,
                    "recipes": {
                      "$recipeId": {
                        "recipeId": "myRecipeId",
                        "name": "myName"
                      }
                    }
                }
   */
  public Map<String, Object> toMap(@NonNull MealInstance mealInstance) {
    HashMap<String, Object> result = new HashMap<>();
    result.put(TIME, mealInstance.dateTime().toInstant(ZoneOffset.UTC).toEpochMilli());
    result.put(LABEL, mealInstance.labelId());

    Map<String, Object> recipes = new HashMap<>();

    for (int i = 0; i < mealInstance.recipes().size(); i++) {
      RecipeMeal recipeMeal = mealInstance.recipes().get(i);

      Map<String, Object> recipeMealMap = new HashMap<>();
      recipeMealMap.put(RECIPE_ID, recipeMeal.recipeId());
      recipeMealMap.put(RECIPE_NAME, recipeMeal.name());

      recipes.put(recipeMeal.recipeId(), recipeMealMap);
    }

    if (!recipes.isEmpty()) {
      result.put(RECIPES, recipes);
    }

    return result;
  }

  static final String MEAL_INSTANCES_KEY = "meal_instances";

  private static final String TIME = "time";
  private static final String LABEL = "label";
  private static final String RECIPES = "recipes";
  private static final String RECIPE_ID = "recipeId";
  private static final String RECIPE_NAME = "name";
}
