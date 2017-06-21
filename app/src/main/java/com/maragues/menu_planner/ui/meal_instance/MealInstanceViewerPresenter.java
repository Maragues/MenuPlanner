package com.maragues.menu_planner.ui.meal_instance;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.RecipeMeal;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;

import java.util.Iterator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by miguelaragues on 25/1/17.
 */

public class MealInstanceViewerPresenter extends BaseLoggedInPresenter<IMealInstanceViewer> {
  MealInstance mealInstance;
  private final String mealId;

  MealInstanceViewerPresenter(@NonNull String mealId) {
    this.mealId = mealId;
  }

  @Override
  protected void onCreate() {
    super.onCreate();

    if (App.appComponent.textUtils().isEmpty(mealId))
      onFetchError(new IllegalArgumentException("mealId can't be null"));
    else
      fetchMealInstance();
  }

  void fetchMealInstance() {
    disposables.add(App.appComponent.mealInstanceProvider().get(mealId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onMealInstanceFetched, this::onFetchError));
  }

  public void onFetchError(Throwable throwable) {
    if (getView() != null) {
      getView().showErrorFetchingMealInstance();
    } else {
      sendToView(IMealInstanceViewer::showErrorFetchingMealInstance);
    }
  }

  void onMealInstanceFetched(MealInstance mealInstance) {
    this.mealInstance = mealInstance;

    showRecipes();
  }

  void showRecipes() {
    final StringBuffer sb = new StringBuffer();

    Iterator<RecipeMeal> it = mealInstance.recipeCollection().iterator();
    while (it.hasNext()) {
      sb.append(it.next().name());
      if (it.hasNext()) sb.append("\n");
    }

    if (getView() != null) {
      getView().showRecipes(sb.toString());
    } else {
      sendToView(v -> v.showRecipes(sb.toString()));
    }
  }
}
