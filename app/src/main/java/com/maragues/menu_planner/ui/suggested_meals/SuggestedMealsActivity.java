package com.maragues.menu_planner.ui.suggested_meals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;
import com.maragues.menu_planner.ui.meal.editor.MealEditorActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.maragues.menu_planner.ui.meal.editor.MealEditorActivity.EXTRA_MEAL_INSTANCE;

public class SuggestedMealsActivity extends BaseLoggedInActivity<SuggestedMealsPresenter, ISuggestedMeals>
        implements ISuggestedMeals {

  @BindView(R.id.meal_list_suggestions)
  RecyclerView recyclerView;

  private final List<Meal> meals = new ArrayList<>();

  private final MealSuggesterAdapter adapter = new MealSuggesterAdapter(meals);

  public static Intent createIntent(Context context, @Nullable MealInstance mealInstance) {
    Intent intent = new Intent(context, SuggestedMealsActivity.class);

    if (mealInstance != null)
      intent.putExtra(EXTRA_MEAL_INSTANCE, mealInstance);

    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_suggested_meals);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
      }
    });

    setupList();
  }

  private void setupList() {
    adapter.recipeClickedObservable().subscribe(meal -> getPresenter().onMealClicked(meal));

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
  }

  @NonNull
  @Override
  public SuggestedMealsPresenter providePresenter() {
    return new SuggestedMealsPresenter(getIntent().getParcelableExtra(MealEditorActivity.EXTRA_MEAL_INSTANCE));
  }

  @Override
  public void navigateToCreateMeal(String key) {
    startActivity(MealEditorActivity.createIntent(this));
  }

  @Override
  public void showSuggestedMeals(List<Meal> newMeals) {
    meals.clear();
    meals.addAll(newMeals);

    adapter.notifyDataSetChanged();
  }
}
