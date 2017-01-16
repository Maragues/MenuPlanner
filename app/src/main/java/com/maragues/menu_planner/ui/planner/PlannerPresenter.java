package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealSlot;
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

  private BehaviorSubject<List<MealSlot>> mealsSubject = BehaviorSubject.createDefault(createDefaultMeals());

  @NonNull
  List<MealSlot> createDefaultMeals() {
    List<MealSlot> meals = new ArrayList<>();

    TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();

    LocalDate firstDayOfCurrentWeek = LocalDate.now().with(fieldISO, 1);
    for (int i = 0; i < 7; i++) {
      meals.add(MealSlot.fromLocalDate(firstDayOfCurrentWeek.plusDays(i)));
    }

    return meals;
  }

  private BehaviorSubject<Boolean> isLoadingSubject = BehaviorSubject.createDefault(false);

  Observable<List<MealSlot>> mealsObservable() {
    return mealsSubject;
  }

  Observable<Boolean> isLoadingObservable() {
    return isLoadingSubject;
  }


  public void onSlotClicked(@NonNull MealSlot mealSlot) {

  }

  public void onAddToSlotClicked(@NonNull MealSlot mealSlot) {

  }

  public void onAddtoDayClicked(@NonNull MealSlot mealSlot) {
    Toast.makeText(App.appComponent.context(), "Meal clicked " + mealSlot.dateTime(), Toast.LENGTH_SHORT).show();
  }
}
