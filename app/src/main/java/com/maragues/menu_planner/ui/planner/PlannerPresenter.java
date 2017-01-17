package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.ui.common.BasePresenter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 13/1/17.
 */

class PlannerPresenter extends BasePresenter<IPlanner> {

  private final BehaviorSubject<List<MealInstance>> mealsSubject = BehaviorSubject.createDefault(createDefaultMeals());

  @NonNull
  List<MealInstance> createDefaultMeals() {
    List<MealInstance> meals = new ArrayList<>();

    TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();

    LocalDate firstDayOfCurrentWeek = LocalDate.now().with(fieldISO, 1);
    for (int i = 0; i < 7; i++) {
      meals.add(MealInstance.fromLocalDate(firstDayOfCurrentWeek.plusDays(i)));
    }

    return meals;
  }

  private BehaviorSubject<Boolean> isLoadingSubject = BehaviorSubject.createDefault(false);

  Observable<List<MealInstance>> mealsObservable() {
    return mealsSubject;
  }

  Observable<Boolean> isLoadingObservable() {
    return isLoadingSubject;
  }


  public void onSlotClicked(@NonNull MealInstance mealInstance) {

  }

  public void onAddToSlotClicked(@NonNull MealInstance mealInstance) {

  }

  public void onAddtoDayClicked(@NonNull MealInstance mealInstance) {
    clickedMealInstance = mealInstance;


    askForLabel();
  }

  private void askForLabel() {
    if (getView() != null)
      getView().askForMealInstanceLabel();
    else
      sendToView(IPlanner::askForMealInstanceLabel);
  }

  MealInstance clickedMealInstance;

  public void onLabelSelected(MealInstanceLabel label) {
    clickedMealInstance = clickedMealInstance.withLabelId(label.id());
    //the mealInstance clicked is at clickedMealSubject, and label is the label we want to assign. Does this make sense?
    //now we should open a Meal editor and when it's completed, assign the label and the time to a MealInstance

    navigateToMealEditor();
  }

  private void navigateToMealEditor() {
    if (getView() != null)
      getView().navigateToMealEditor(clickedMealInstance);
    else
      sendToView(view -> view.navigateToMealEditor(clickedMealInstance));
  }

  public void onMealCreated(@Nullable String mealId) {
    clickedMealInstance = clickedMealInstance.withMealId(mealId);

    App.appComponent.mealInstanceProvider().create(clickedMealInstance);
  }
}
