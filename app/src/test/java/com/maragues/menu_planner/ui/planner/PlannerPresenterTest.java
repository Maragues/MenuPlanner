package com.maragues.menu_planner.ui.planner;

import android.support.annotation.NonNull;

import com.maragues.menu_planner.App;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.test.factories.MealInstanceFactory;
import com.maragues.menu_planner.ui.test.BasePresenterTest;
import com.maragues.menu_planner.utils.DateUtils;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by miguelaragues on 17/1/17.
 */
public class PlannerPresenterTest extends BasePresenterTest<IPlanner, PlannerPresenter> {

  public PlannerPresenterTest() {
    super(IPlanner.class);
  }

  @NonNull
  @Override
  protected PlannerPresenter createPresenter() {
    return new PlannerPresenter();
  }

  /*
  START UP
   */
  @Test
  public void loadMeals_requestsPreviousCurrentAndNextWeek() {
    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    verify(App.appComponent.mealInstanceProvider()).listBetween(
            eq(firstWeekDayStartOfDay.minusWeeks(1)),
            eq(firstWeekDayStartOfDay.plusWeeks(2))
    );
  }

  @Test
  public void start_setsCurrentWeekHeader() throws NoSuchFieldException, IllegalAccessException {
    Locale.setDefault(Locale.US);

    resetHeaderFormatter();

    initPresenter();

    LocalDateTime currentWeek = DateUtils.startOfWeek();

    String expectedHeader = expectedHeaderForWeek(currentWeek);

    verify(view).setHeader(eq(expectedHeader));
  }

  /*
  NAVIGATION
   */
  @Test
  public void navigateBack_requestsOneMoreWeekBackwards() {
    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    /*
    we have already requested past's week mealinstances, now fetch anotherone in case the user
    wants to navigate back again
     */
    presenter.onNavigateBack();

    verify(App.appComponent.mealInstanceProvider()).listBetween(
            eq(firstWeekDayStartOfDay.minusWeeks(2)),
            eq(firstWeekDayStartOfDay.minusWeeks(1))
    );
  }

  @Test
  public void navigateBack_updatesDefaultMealInstancesToPastWeek() {
    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    presenter.onNavigateBack();

    ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);

    verify(presenter).showNewWeek(captor.capture());

    assertEquals(DateUtils.weekOfYear(firstWeekDayStartOfDay.toLocalDate()) - 1,
            DateUtils.weekOfYear(captor.getValue().toLocalDate()));
  }

  @Test
  public void navigateForward_requestsOneMoreWeekForward() {
    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    /*
    we have already requested past's week mealinstances, now fetch anotherone in case the user
    wants to navigate forward again
     */
    presenter.onNavigateForward();

    verify(App.appComponent.mealInstanceProvider()).listBetween(
            eq(firstWeekDayStartOfDay.plusWeeks(2)),
            eq(firstWeekDayStartOfDay.plusWeeks(3))
    );
  }

  @Test
  public void navigateForward_updatesHeader() throws NoSuchFieldException, IllegalAccessException {
    Locale.setDefault(Locale.US);

    resetHeaderFormatter();

    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    /*
    we have already requested past's week mealinstances, now fetch anotherone in case the user
    wants to navigate forward again
     */
    presenter.onNavigateForward();

    verify(view).setHeader(eq(expectedHeaderForWeek(firstWeekDayStartOfDay.plusWeeks(1))));
  }

  @Test
  public void navigateMultipleTimes_showsCorrectWeek() throws NoSuchFieldException, IllegalAccessException {
    Locale.setDefault(Locale.US);

    resetHeaderFormatter();

    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    /*
    we have already requested past's week mealinstances, now fetch anotherone in case the user
    wants to navigate forward again
     */
    presenter.onNavigateForward();

    ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);

    verify(presenter).showNewWeek(captor.capture());

    LocalDate expectedWeek = firstWeekDayStartOfDay.toLocalDate().plusWeeks(1);
    assertEquals(
            DateUtils.weekOfYear(expectedWeek),
            DateUtils.weekOfYear(captor.getValue().toLocalDate())
    );

    presenter.onNavigateForward();

    expectedWeek = expectedWeek.plusWeeks(1);

    verify(presenter, times(2)).showNewWeek(captor.capture());
    assertEquals(
            DateUtils.weekOfYear(expectedWeek),
            DateUtils.weekOfYear(captor.getValue().toLocalDate())
    );
  }

  @Test
  public void navigateBack_updatesDefaultMealInstancesToNextWeek() {
    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    presenter.onNavigateForward();

    ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);

    verify(presenter).showNewWeek(captor.capture());

    assertEquals(DateUtils.weekOfYear(firstWeekDayStartOfDay.toLocalDate()) + 1,
            DateUtils.weekOfYear(captor.getValue().toLocalDate()));
  }

  @Test
  public void navigateBack_updatesHeader() throws NoSuchFieldException, IllegalAccessException {
    Locale.setDefault(Locale.US);

    resetHeaderFormatter();

    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    /*
    we have already requested past's week mealinstances, now fetch anotherone in case the user
    wants to navigate forward again
     */
    presenter.onNavigateBack();

    verify(view).setHeader(eq(expectedHeaderForWeek(firstWeekDayStartOfDay.minusWeeks(1))));
  }

  @Test
  public void navigateBackMultipleTimes_showsCorrectWeek() throws NoSuchFieldException, IllegalAccessException {
    Locale.setDefault(Locale.US);

    resetHeaderFormatter();

    initPresenter();

    LocalDateTime firstWeekDayStartOfDay = DateUtils.startOfWeek();

    /*
    we have already requested past's week mealinstances, now fetch anotherone in case the user
    wants to navigate forward again
     */
    presenter.onNavigateBack();

    ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);

    verify(presenter).showNewWeek(captor.capture());

    LocalDate expectedWeek = firstWeekDayStartOfDay.toLocalDate().minusWeeks(1);
    assertEquals(
            DateUtils.weekOfYear(expectedWeek),
            DateUtils.weekOfYear(captor.getValue().toLocalDate())
    );

    presenter.onNavigateBack();

    expectedWeek = expectedWeek.minusWeeks(1);

    verify(presenter, times(2)).showNewWeek(captor.capture());
    assertEquals(
            DateUtils.weekOfYear(expectedWeek),
            DateUtils.weekOfYear(captor.getValue().toLocalDate())
    );
  }

  /*
  NAVIGATE RESULTS ARRIVE
   */

  @Test
  public void navigateResults_addedToOriginalList() {
    List<MealInstance> meals = new ArrayList<>();

    LocalDateTime now = LocalDateTime.now();

    meals.add(MealInstanceFactory.withRecipes(now));
    meals.add(MealInstanceFactory.withRecipes(now.minusSeconds(1)));
    meals.add(MealInstanceFactory.withRecipes(now.plusSeconds(5)));

    doReturn(Flowable.just(meals)).when(App.appComponent.mealInstanceProvider())
            .listBetween(any(LocalDateTime.class), any(LocalDateTime.class));

    initPresenter();

    assertEquals(PlannerPresenter.INITIAL_MEAL_INSTANCES + 3, fullMealInstancesThroughObservable().size());

    meals.clear();

    meals.add(MealInstanceFactory.base(now.minusWeeks(1)));

    presenter.onNavigateBack();

    assertEquals(PlannerPresenter.INITIAL_MEAL_INSTANCES + 4, fullMealInstancesThroughObservable().size());
  }

  @Test
  public void navigateResults_sendEventsForCurrentWeek() {
    List<MealInstance> meals = new ArrayList<>();

    LocalDateTime now = LocalDateTime.now();

    meals.add(MealInstanceFactory.withRecipes(now));
    meals.add(MealInstanceFactory.withRecipes(now.minusSeconds(1)));
    meals.add(MealInstanceFactory.withRecipes(now.plusSeconds(5)));

    doReturn(Flowable.just(meals)).when(App.appComponent.mealInstanceProvider())
            .listBetween(any(LocalDateTime.class), any(LocalDateTime.class));

    initPresenter();

    assertEquals(PlannerPresenter.INITIAL_MEAL_INSTANCES + 3, weekMealInstancesThroughObservable().size());

    meals.clear();

    meals.add(MealInstanceFactory.base(now.minusWeeks(1)));

    presenter.onNavigateBack();

    assertEquals(PlannerPresenter.INITIAL_MEAL_INSTANCES + 1, weekMealInstancesThroughObservable().size());
  }

  /*
  MEALS SUBJECT
   */

  @Test
  public void mealsSubject_updatedOnNext() {
    initPresenter();

    assertEquals(7, weekMealInstancesThroughObservable().size());

    List<MealInstance> mealInstances = new ArrayList<>();
    mealInstances.add(MealInstanceFactory.base(LocalDateTime.now()));

    presenter.onMealInstancesLoaded(mealInstances);

    assertEquals(8, weekMealInstancesThroughObservable().size());
  }

  @Test
  public void mealsSubject_onlyReturnsCurrentWeekMeals() {
    initPresenter();

    List<MealInstance> mealInstances = new ArrayList<>();

    mealInstances.add(MealInstanceFactory.withRecipes(LocalDateTime.now()));
    mealInstances.add(MealInstanceFactory.withRecipes(LocalDateTime.now().minusWeeks(1)));
    mealInstances.add(MealInstanceFactory.withRecipes(LocalDateTime.now().plusWeeks(1)));

    presenter.onMealInstancesLoaded(mealInstances);

    assertEquals(8, weekMealInstancesThroughObservable().size());
  }

  @Test
  public void mealsSubject_returnsCurrentWeekInstances() {
    initPresenter();

    List<MealInstance> mealInstances = new ArrayList<>();

    mealInstances.add(MealInstanceFactory.withRecipes(LocalDateTime.now()));

    presenter.onMealInstancesLoaded(mealInstances);

    assertEquals(8, weekMealInstancesThroughObservable().size());

    presenter.onNavigateBack();

    assertEquals(7, weekMealInstancesThroughObservable().size());

    presenter.onNavigateForward();

    assertEquals(8, weekMealInstancesThroughObservable().size());

    presenter.onNavigateForward();

    assertEquals(7, weekMealInstancesThroughObservable().size());

    presenter.onNavigateBack();

    assertEquals(8, weekMealInstancesThroughObservable().size());
  }

  @Test
  public void fullMealsSubject_holdsAllResults() {
    initPresenter();

    List<MealInstance> mealInstances = new ArrayList<>();

    mealInstances.add(MealInstanceFactory.withRecipes(LocalDateTime.now()));
    mealInstances.add(MealInstanceFactory.withRecipes(LocalDateTime.now().minusWeeks(1)));
    mealInstances.add(MealInstanceFactory.withRecipes(LocalDateTime.now().plusWeeks(1)));

    presenter.onMealInstancesLoaded(mealInstances);

    assertEquals(10, presenter.fullMealsObservable().test().assertValueCount(1).values().get(0).size());
  }

  /*
  SLOT CLICKED
   */
  @Test
  public void slotClicked_opensMealViewer() {
    initPresenter();

    MealInstance mealInstance = MealInstanceFactory.base();
    presenter.onSlotClicked(mealInstance);

    verify(view).navigateToMealInstanceViewer(eq(mealInstance));
  }

  /*
  DEFAULT MEAL_INSTANCES
   */

  @Test
  public void mealInstanceObservable_default_7() {
    initPresenter();

    List<MealInstance> instances = weekMealInstancesThroughObservable();

    assertEquals(7, instances.size());
  }

  @Test
  public void defaultMealInstances_france_MondayFirstDay() {
    initPresenter();

    Locale.setDefault(Locale.FRANCE);

    List<MealInstance> instances = presenter.createDefaultMeals();

    assertEquals(DayOfWeek.MONDAY.getValue(), instances.get(0).dateTime().getDayOfWeek().getValue());
  }

  @Test
  public void defaultMealInstances_france_SevenDaysOfWeek() {
    initPresenter();

    Locale.setDefault(Locale.FRANCE);

    List<MealInstance> instances = presenter.createDefaultMeals();

    int firstDay = DayOfWeek.MONDAY.getValue();
    for (MealInstance instance : instances) {
      assertEquals(firstDay++, instance.dateTime().getDayOfWeek().getValue());
    }
  }

  @Test
  public void defaultMealInstances_SundayFirstDayInUS() {
    initPresenter();

    Locale.setDefault(Locale.US);

    List<MealInstance> instances = presenter.createDefaultMeals();

    assertEquals(DayOfWeek.SUNDAY.getValue(), instances.get(0).dateTime().getDayOfWeek().getValue());
  }

  @Test
  public void defaultMealInstances_US_SevenDaysOfWeek() {
    initPresenter();

    Locale.setDefault(Locale.US);

    List<MealInstance> instances = presenter.createDefaultMeals();

    assertEquals(DayOfWeek.SUNDAY.getValue(), instances.get(0).dateTime().getDayOfWeek().getValue());

    int secondDay = DayOfWeek.MONDAY.getValue();
    for (int i = 1; i < instances.size(); i++) {
      assertEquals(secondDay++, instances.get(i).dateTime().getDayOfWeek().getValue());
    }
  }

  /*
  REPLACE DEFAULT MEALS
   */
  @Test
  public void replaceDefault_previousWeek() {
    initPresenter();

    List<MealInstance> defaultMealInstances = weekMealInstancesThroughObservable();

    LocalDateTime currentWeekFirstDay = DateUtils.startOfWeek();

    assertEquals(currentWeekFirstDay, defaultMealInstances.get(0).dateTime());

    presenter.showNewWeek(LocalDateTime.now().minusWeeks(1));

    defaultMealInstances = weekMealInstancesThroughObservable();

    assertEquals(currentWeekFirstDay.minusWeeks(1), defaultMealInstances.get(0).dateTime());
  }

  @Test
  public void replaceDefault_nextWeek() {
    initPresenter();

    List<MealInstance> defaultMealInstances = weekMealInstancesThroughObservable();

    LocalDateTime currentWeekFirstDay = DateUtils.startOfWeek();

    assertEquals(currentWeekFirstDay, defaultMealInstances.get(0).dateTime());

    presenter.showNewWeek(LocalDateTime.now().plusWeeks(1));

    defaultMealInstances = weekMealInstancesThroughObservable();

    assertEquals(currentWeekFirstDay.plusWeeks(1), defaultMealInstances.get(0).dateTime());
  }

  /*
    CLICKS
   */
  @Test
  public void clickOnDay_asksForLabel() {
    initPresenter();

    presenter.onAddtoDayClicked(mock(MealInstance.class));

    verify(view).askForMealInstanceLabel();
  }

  private List<MealInstance> weekMealInstancesThroughObservable() {
    return presenter.mealsObservable().test().assertValueCount(1).values().get(0);
  }

  private List<MealInstance> fullMealInstancesThroughObservable() {
    return presenter.fullMealsObservable().test().assertValueCount(1).values().get(0);
  }

  @NonNull
  private String expectedHeaderForWeek(LocalDateTime week) {
    return "Sun, " + week.getDayOfMonth()
            + " - Sat, " + week.plusDays(6).getDayOfMonth();
  }

  private void resetHeaderFormatter() throws NoSuchFieldException, IllegalAccessException {
    Field field = PlannerPresenter.class.getDeclaredField("HEADER_FORMATTER");
    DateTimeFormatter originalFormatter = (DateTimeFormatter) field.get(null);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    boolean isModifierAccessible = modifiersField.isAccessible();
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    boolean isAccessible = field.isAccessible();
    field.setAccessible(true);

    field.set(null, originalFormatter.withLocale(Locale.getDefault()));
    field.setAccessible(isAccessible);
    modifiersField.setAccessible(isModifierAccessible);
  }
}