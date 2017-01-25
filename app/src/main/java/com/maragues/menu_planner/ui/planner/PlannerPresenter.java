package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.ui.common.BasePresenter;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.WeekFields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by miguelaragues on 13/1/17.
 */

class PlannerPresenter extends BasePresenter<IPlanner> {

  private final BehaviorSubject<List<MealInstance>> mealsSubject = BehaviorSubject.createDefault(createDefaultMeals());

  @Override
  protected void onCreate() {
    super.onCreate();

    loadMeals();
  }

  @NonNull
  List<MealInstance> createDefaultMeals() {
    List<MealInstance> meals = new ArrayList<>();

    TemporalField fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek();

    LocalDateTime firstDayOfCurrentWeek = LocalDate.now().atStartOfDay().with(fieldISO, 1);
    for (int i = 0; i < 7; i++) {
      meals.add(MealInstance.fromLocalDateTime(firstDayOfCurrentWeek.plusDays(i)));
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
    clickedMealInstance = mealInstance;

    navigateToMealInstanceViewer();
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
    clickedMealInstance = clickedMealInstance.withLabel(label);
    //the mealInstance clicked is at clickedMealSubject, and label is the label we want to assign. Does this make sense?
    //now we should open a Meal editor and when it's completed, assign the label and the time to a MealInstance

    navigateToSuggestedMeals();
  }

  private void navigateToSuggestedMeals() {
    if (getView() != null)
      getView().navigateToSuggestedMeals(clickedMealInstance);
    else
      sendToView(view -> view.navigateToSuggestedMeals(clickedMealInstance));
  }

  private void navigateToMealInstanceViewer() {
    if (getView() != null)
      getView().navigateToMealInstanceViewer(clickedMealInstance);
    else
      sendToView(view -> view.navigateToMealInstanceViewer(clickedMealInstance));
  }

  public void onMealCreated(@Nullable String mealId) {
    clickedMealInstance = clickedMealInstance.withMealId(mealId);

    App.appComponent.mealInstanceProvider().create(clickedMealInstance);
  }

  public void onCreateMealCancelled() {
    clickedMealInstance = null;
  }

  private static final Comparator<MealInstance> COMPARATOR = new Comparator<MealInstance>() {
    @Override
    public int compare(MealInstance o1, MealInstance o2) {
      return o1.dateTime().compareTo(o2.dateTime());
    }
  };

  void loadMeals() {
    App.appComponent.mealInstanceProvider().list()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(this::onMealInstancesLoaded)
            .subscribe()
    ;
  }

  void onMealInstancesLoaded(List<MealInstance> mealInstances) {
    List<MealInstance> fullList = new ArrayList<>(mealsSubject.getValue());
    fullList.addAll(mealInstances);

    Collections.sort(fullList, COMPARATOR);

    mealsSubject.onNext(fullList);
  }
}
