package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.MealInstanceLabel;
import com.maragues.menu_planner.ui.common.BaseLoggedInPresenter;
import com.maragues.menu_planner.utils.DateUtils;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.maragues.menu_planner.utils.DateUtils.HEADER_FORMATTER;

/**
 * Created by miguelaragues on 13/1/17.
 */

class PlannerPresenter extends BaseLoggedInPresenter<IPlanner> {
  private static final int PAST_WEEKS_NUMBER = 1;
  private static final int FUTURE_WEEKS_NUMBER = 2;

  static final int INITIAL_MEAL_INSTANCES = 7;

  private final BehaviorSubject<Set<MealInstance>> currentWeekMealsSubject = BehaviorSubject
          .createDefault(new HashSet<>(createDefaultMeals()));

  private final BehaviorSubject<Set<MealInstance>> fullMealsSubject = BehaviorSubject
          .createDefault(new HashSet<>(currentWeekMealsSubject.getValue()));

  private LocalDateTime currentWeek, oldestDateTime, newestDateTime;

  @Override
  protected void onCreate() {
    super.onCreate();

    LocalDateTime startOfWeek = DateUtils.startOfWeek();

    loadMeals(startOfWeek.minusWeeks(PAST_WEEKS_NUMBER), startOfWeek.plusWeeks(FUTURE_WEEKS_NUMBER));
  }

  @Override
  protected void onAttachView(@NonNull IPlanner view) {
    super.onAttachView(view);

    showWeekHeader();
  }

  void showWeekHeader() {
    if (getView() != null) {
      getView().setHeader(generateHeader());
    } else {
      sendToView(v -> v.setHeader(generateHeader()));
    }
  }

  @Nullable
  String generateHeader() {
    if (currentWeek == null)
      return null;

    String header = HEADER_FORMATTER.format(DateUtils.startOfWeek(currentWeek.toLocalDate()));
    header += " - ";
    return header + HEADER_FORMATTER.format(DateUtils.endOfWeek(currentWeek.toLocalDate()));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  private BehaviorSubject<Boolean> isLoadingSubject = BehaviorSubject.createDefault(false);

  Observable<List<MealInstance>> mealsObservable() {
    return currentWeekMealsSubject
//            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .map(mealInstances -> {
              List<MealInstance> filteredList = new ArrayList<>(mealInstances);
              Iterator<MealInstance> it = filteredList.iterator();

              final int currentWeekNumber = DateUtils.weekOfYear(currentWeek.toLocalDate());
              while (it.hasNext()) {
                if (DateUtils.weekOfYear(it.next().dateTime().toLocalDate()) != currentWeekNumber)
                  it.remove();
              }

              Collections.sort(filteredList, COMPARATOR);

              return filteredList;
            });
  }

  Observable<Set<MealInstance>> fullMealsObservable() {
    return fullMealsSubject;
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

  void loadMeals(LocalDateTime tstartInclusive, LocalDateTime tEndExclusive) {
    oldestDateTime = tstartInclusive;
    newestDateTime = tEndExclusive;

    isLoadingSubject.onNext(true);

    disposables.add(
            App.appComponent.mealInstanceProvider().listBetween(tstartInclusive, tEndExclusive)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(this::onMealInstancesLoaded)
                    .doOnComplete(() -> isLoadingSubject.onNext(false))
                    .subscribe()
    );
  }

  synchronized void onMealInstancesLoaded(List<MealInstance> mealInstances) {
    Set<MealInstance> fullList = new HashSet<>(fullMealsSubject.getValue());
    fullList.addAll(mealInstances);

    propagateList(fullList);
  }

  void propagateList(Set<MealInstance> fullList) {
    fullMealsSubject.onNext(fullList);
    currentWeekMealsSubject.onNext(fullList);
  }

  // TODO: 26/1/17 Debounce this so that quick taps don't launch multiple requests
  void onNavigateBack() {
    showNewWeek(currentWeek.minusWeeks(1));

    loadMeals(oldestDateTime.minusWeeks(1), oldestDateTime);
  }

  void onNavigateForward() {
    showNewWeek(currentWeek.plusWeeks(1));

    loadMeals(newestDateTime, newestDateTime.plusWeeks(1));
  }

  /**
   * This method will replace the MealInstances that represent the days of the week. It relies on
   * these being the only MealInstances without recipes.
   * <p>
   * It also
   * - updates currentWeek when invoking createDayMealsForWeek
   * - updates header from newWeek
   * <p>
   * 1. Find MealInstances representing days and remove them
   * 2. Add mealInstnaces for new week
   * 3. Propagate new list
   *
   * @param newWeek The week we will generate empty MealInstances for. It will also be our new
   *                currentWeek
   */
  void showNewWeek(LocalDateTime newWeek) {
    Set<MealInstance> meals = currentWeekMealsSubject.getValue();

    Iterator<MealInstance> it = meals.iterator();

    while (it.hasNext()) {
      if (!it.next().hasRecipes()) it.remove();
    }

    meals.addAll(createDayMealsForWeek(newWeek));

    showWeekHeader();

    propagateList(meals);
  }

  @NonNull
  List<MealInstance> createDefaultMeals() {
    return createDayMealsForWeek(LocalDateTime.now());
  }

  List<MealInstance> createDayMealsForWeek(LocalDateTime week) {
    currentWeek = week;

    List<MealInstance> meals = new ArrayList<>();

    LocalDateTime firstDayOfCurrentWeek = DateUtils.startOfWeek(week.toLocalDate());
    for (int i = 0; i < INITIAL_MEAL_INSTANCES; i++) {
      meals.add(MealInstance.fromLocalDateTime(firstDayOfCurrentWeek.plusDays(i)));
    }

    return meals;
  }
}
