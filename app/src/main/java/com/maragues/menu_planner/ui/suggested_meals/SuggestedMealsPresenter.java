package com.maragues.menu_planner.ui.suggested_meals;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 17/1/17.
 */

public class SuggestedMealsPresenter extends BaseLoggedInPresenter<ISuggested> {

  private BehaviorSubject<List<Meal>> suggestedMealsSubject = BehaviorSubject.createDefault(new ArrayList<>());

  @Override
  protected void onAttachView(@NonNull ISuggested view) {
    super.onAttachView(view);

    App.appComponent.mealProvider().list()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .map(meals -> {
              if (meals.isEmpty()) {
                navigateToCreateMeal();

                return null;
              }

              return meals;
            })
            .subscribe(this::onMealsReceived);
  }

  void onMealsReceived(List<Meal> meals) {
    extractSuggestedMeals(meals);
  }

  private void navigateToCreateMeal() {
    if (getView() != null) {
      getView().navigateToCreateMeal();

      getView().finish();
    } else {
      sendToView(ISuggested::navigateToCreateMeal);

      sendToView(ISuggested::finish);
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
}
