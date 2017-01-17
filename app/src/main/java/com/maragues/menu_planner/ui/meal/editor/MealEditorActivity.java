package com.maragues.menu_planner.ui.meal.editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstance;

public class MealEditorActivity extends AppCompatActivity {

  private static final String EXTRA_MEAL_ID = "extra_meal_id";

  public static Intent createIntent(Context context) {
    return createIntent(context, null);
  }

  public static Intent createIntent(Context context, @Nullable MealInstance mealInstance) {
    Intent intent = new Intent(context, MealEditorActivity.class);

    return intent;
  }

  @Nullable
  public static String extractMealId(Intent data) {
    if (data == null || !data.hasExtra(EXTRA_MEAL_ID))
      return null;

    return data.getStringExtra(EXTRA_MEAL_ID);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meal_editor);
  }
}
