package com.maragues.menu_planner.ui.suggested_meals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.Meal;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;
import com.maragues.menu_planner.ui.meal.editor.MealEditorActivity;

import java.util.List;

public class SuggestedMealsActivity extends BaseLoggedInActivity<SuggestedMealsPresenter, ISuggested>
        implements ISuggested {

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
  }

  @NonNull
  @Override
  public SuggestedMealsPresenter providePresenter() {
    return new SuggestedMealsPresenter();
  }

  @Override
  public void navigateToCreateMeal() {
    startActivity(MealEditorActivity.createIntent(this));
  }

  @Override
  public void showSuggestedMeals(List<Meal> meals) {

  }
}
