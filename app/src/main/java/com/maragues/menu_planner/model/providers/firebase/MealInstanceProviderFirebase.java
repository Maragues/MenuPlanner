package com.maragues.menu_planner.model.providers.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
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
    return getReference()
            .child(MEAL_INSTANCES_KEY)
            .child(App.appComponent.userProvider().getGroupId())
            .orderByKey();
  }

  @Override
  protected MealInstance snapshotToInstance(DataSnapshot dataSnapshot) {
    return MealInstance.create(dataSnapshot);
  }

  @Override
  protected Map<String, Object> synchronizableToMap(MealInstance mealInstance) {
    if (App.appComponent.textUtils().isEmpty(mealInstance.id()))
      throw new IllegalArgumentException("MealInstance must have key");

    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/" + MEAL_INSTANCES_KEY
                    + "/" + App.appComponent.userProvider().getGroupId()
                    + "/" + String.valueOf(mealInstance.dateTime().toInstant(ZoneOffset.UTC).toEpochMilli()),
            mealInstance.toFirebaseValue()
    );

    return childUpdates;
  }

  @Override
  public Single<MealInstance> create(final MealInstance newMealInstance) {
    return Single.create(new SingleOnSubscribe<MealInstance>() {
      @Override
      public void subscribe(SingleEmitter<MealInstance> e) throws Exception {
        getReference().updateChildren(synchronizableToMap(newMealInstance), (databaseError, databaseReference) -> {
          if (databaseError != null)
            e.onError(databaseError.toException());
          else {
            e.onSuccess(newMealInstance);
          }
        });
      }
    });
  }

  DatabaseReference getMealInstanceReference(MealInstance mealInstance) {
    return getReference()
            .child(MEAL_INSTANCES_KEY)
            .child(App.appComponent.userProvider().getGroupId())
            .child(String.valueOf(mealInstance.dateTime().toInstant(ZoneOffset.UTC).toEpochMilli()));
  }

  static final String MEAL_INSTANCES_KEY = "meal_instances";

  private static final String TIME = "time";
  private static final String LABEL = "label";
  private static final String RECIPES = "recipes";
  private static final String RECIPE_ID = "recipeId";
  private static final String RECIPE_NAME = "name";
}
