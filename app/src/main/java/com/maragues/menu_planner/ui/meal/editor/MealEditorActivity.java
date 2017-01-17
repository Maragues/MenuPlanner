package com.maragues.menu_planner.ui.meal.editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.maragues.menu_planner.R;

public class MealEditorActivity extends AppCompatActivity {

  public static Intent createIntent(Context context) {
    return new Intent(context, MealEditorActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meal_editor);
  }
}
