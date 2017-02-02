package com.maragues.menu_planner.ui.suggested_meals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;
import com.maragues.menu_planner.ui.meal.editor.MealEditorActivity;
import com.maragues.menu_planner.ui.meal_instance.MealInstanceViewerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SuggestedMealsActivity extends BaseLoggedInActivity<SuggestedMealsPresenter, ISuggestedMeals>
        implements ISuggestedMeals {

  private static final int CREATE_MEAL_REQUEST = 5;

  @BindView(R.id.meal_list_suggestions)
  RecyclerView recyclerView;

  private final List<Meal> meals = new ArrayList<>();

  private final SuggestedMealsAdapter adapter = new SuggestedMealsAdapter(meals);

  public static Intent createIntent(Context context, @Nullable MealInstance mealInstance) {
    Intent intent = new Intent(context, SuggestedMealsActivity.class);

    if (mealInstance != null)
      intent.putExtra(MealInstanceViewerActivity.EXTRA_MEAL_INSTANCE_ID, mealInstance);

    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_suggested_meals);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupList();
  }

  @OnClick(R.id.suggested_meals_fab)
  void onCreateMealFab() {
    getPresenter().onCreateMealClicked();
  }

  private void setupList() {
    adapter.recipeClickedObservable().subscribe(meal -> getPresenter().onMealClicked(meal));

    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    recyclerView.setAdapter(adapter);
  }

  @NonNull
  @Override
  public SuggestedMealsPresenter providePresenter() {
    return new SuggestedMealsPresenter(getIntent().getParcelableExtra(MealInstanceViewerActivity.EXTRA_MEAL_INSTANCE_ID));
  }

  @Override
  public void navigateToCreateMeal(String expectedMealId) {
    startActivityForResult(MealEditorActivity.createIntent(this, expectedMealId), CREATE_MEAL_REQUEST);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == CREATE_MEAL_REQUEST)
      if (resultCode != RESULT_OK)
        getPresenter().onCreateMealCanceled();
  }

  @Override
  public void showSuggestedMeals(List<Meal> newMeals) {
    meals.clear();
    meals.addAll(newMeals);

    adapter.notifyDataSetChanged();
  }
}
