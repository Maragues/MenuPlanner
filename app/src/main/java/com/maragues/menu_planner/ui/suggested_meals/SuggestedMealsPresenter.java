package com.maragues.menu_planner.ui.suggested_meals;

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
    App.appComponent.mealProvider().list()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onMealsReceived);
  }

  void onMealsReceived(List<Meal> meals) {
    if (meals.isEmpty()) {
      onCreateMealClicked();
    }

    extractSuggestedMeals(meals);
  }

  private void navigateToCreateMeal(String key) {
    if (getView() != null) {
      getView().navigateToCreateMeal(key);
    } else {
      sendToView(v -> v.navigateToCreateMeal(key));
    }

    finish();
  }

  void finish() {
    if (getView() != null) {
      getView().finish();
    } else {
      sendToView(ISuggestedMeals::finish);
    }
  }

  private void extractSuggestedMeals(List<Meal> meals) {
    if (meals != null && !meals.isEmpty()) {
      // TODO: 17/1/17 extract suggested meals
      if (getView() != null)
        getView().showSuggestedMeals(meals);
      else
        sendToView(view -> view.showSuggestedMeals(meals));
    }
  }

  public void onMealClicked(Meal meal) {
    App.appComponent.mealInstanceProvider().create(mealInstance.fromMeal(meal))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(this::onMealInstanceCreated)
            .subscribe();
  }

  void onMealInstanceCreated(MealInstance mealInstance) {
    finish();
  }

  public void onCreateMealClicked() {
    App.appComponent.mealProvider().getKey()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(key -> {
              expectedKey = key;

              navigateToCreateMeal(key);
            })
            .subscribe();
  }
}
