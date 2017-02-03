package com.maragues.menu_planner.ui.suggested_meals;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class SuggestedMealsPresenter extends BaseLoggedInPresenter<ISuggestedMeals> {

  private BehaviorSubject<List<Meal>> suggestedMealsSubject = BehaviorSubject.createDefault(new ArrayList<>());

  MealInstance mealInstance;

  public String expectedKey;

  SuggestedMealsPresenter(@Nullable MealInstance mealInstance) {
    this.mealInstance = mealInstance;
  }

  @Override
  protected void onCreate() {
    super.onCreate();

    loadSuggestedMeals();
  }

  void loadSuggestedMeals() {
    disposables.add(App.appComponent.mealProvider().list()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onMealsReceived));
  }

  void onMealsReceived(List<Meal> meals) {
    if (meals.isEmpty()) {
      //if we have 0 meals, automatically do to create meal screen
      onCreateMealClicked();
    }

    processMeals(meals);
  }

  private void navigateToCreateMeal(String key) {
    if (getView() != null) {
      getView().navigateToCreateMeal(key);
    } else {
      sendToView(v -> v.navigateToCreateMeal(key));
    }
  }

  void finish() {
    if (getView() != null) {
      getView().finish();
    } else {
      sendToView(ISuggestedMeals::finish);
    }
  }

  private void processMeals(List<Meal> meals) {
    if (meals != null && !meals.isEmpty()) {
      Meal meal = extractMealWithExpectedKey(meals);
      if (meal != null) {
        onCreateMealSuccess(meal);
      } else {
        // TODO: 17/1/17 extract suggested meals
        showSuggestedMeals(meals);
      }
    }
  }

  @Nullable
  Meal extractMealWithExpectedKey(List<Meal> meals) {
    if (App.appComponent.textUtils().isEmpty(expectedKey) || meals.isEmpty())
      return null;

    for (int i = 0; i < meals.size(); i++) {
      if (meals.get(i).id().equals(expectedKey)) return meals.get(i);
    }

    return null;
  }

  void showSuggestedMeals(List<Meal> meals) {
    if (getView() != null)
      getView().showSuggestedMeals(meals);
    else
      sendToView(view -> view.showSuggestedMeals(meals));
  }

  public void onMealClicked(Meal meal) {
    disposables.add(App.appComponent.mealInstanceProvider().create(mealInstance.fromMeal(meal))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::onMealInstanceCreated)
            .subscribe());
  }

  void onMealInstanceCreated(MealInstance mealInstance) {
    finish();
  }

  public void onCreateMealClicked() {
    disposables.add(App.appComponent.mealProvider().generateKey()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(key -> {
              expectedKey = key;

              navigateToCreateMeal(key);
            })
            .subscribe());
  }

  void onCreateMealSuccess(@NonNull Meal meal) {
    if (mealInstance != null) {
      disposables.add(App.appComponent.mealInstanceProvider().create(mealInstance.fromMeal(meal))
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(mealInstance -> {
                finish();
              }))
      ;
    } else {
      finish();
    }
  }

  public void onCreateMealCanceled() {
    expectedKey = null;
  }
}
