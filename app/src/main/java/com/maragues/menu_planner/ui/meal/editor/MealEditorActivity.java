package com.maragues.menu_planner.ui.meal.editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maragues.menu_planner.R;
import com.maragues.menu_planner.model.MealInstance;
import com.maragues.menu_planner.model.RecipeMeal;
import com.maragues.menu_planner.ui.common.BaseLoggedInActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealEditorActivity extends BaseLoggedInActivity<MealEditorPresenter, IMealEditor>
        implements IMealEditor {

  private static final String EXTRA_MEAL_ID = "extra_meal_id";
  private static final int ADD_RECIPE_CODE = 6;

  @BindView(R.id.meal_editor_empty)
  View emptyLayout;

  @BindView(R.id.meal_editor_recipe_list)
  RecyclerView recipeRecyclerView;

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

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupRecipeList();
  }

  private final List<RecipeMeal> recipeMeals = new ArrayList<>();
  RecipeMealAdapter recipeAdapter;

  private void setupRecipeList() {
    recipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    recipeAdapter = new RecipeMealAdapter(recipeMeals);

    recipeRecyclerView.setAdapter(recipeAdapter);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ADD_RECIPE_CODE && resultCode == RESULT_OK) {
      getPresenter().onRecipeAdded(AddRecipeToMealActivity.extractRecipe(data));
    }
  }

  @OnClick({R.id.meal_editor_add, R.id.meal_editor_empty})
  void onAddRecipeClicked() {
    getPresenter().onAddRecipeClicked();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();

    inflater.inflate(R.menu.menu_meal_editor, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_save:
        getPresenter().onSaveClicked();

        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @NonNull
  @Override
  public MealEditorPresenter providePresenter() {
    return new MealEditorPresenter();
  }

  @Override
  public void navigateToAddRecipe() {
    startActivityForResult(AddRecipeToMealActivity.createIntent(this), ADD_RECIPE_CODE);
  }

  @Nullable
  @Override
  public String getMealId() {
    return null;
  }

  @Override
  public void hideEmptyLayout() {
    emptyLayout.setVisibility(View.GONE);
  }

  @Override
  public void showEmptyLayout() {
    emptyLayout.setVisibility(View.VISIBLE);
  }

  @Override
  public void showRecipes(@NonNull List<RecipeMeal> recipes) {
    // TODO: 21/1/17 Use DiffUtil

    recipeMeals.clear();

    recipeMeals.addAll(recipes);

    recipeAdapter.notifyDataSetChanged();
  }

  private static class RecipeMealAdapter extends RecyclerView.Adapter<RecipeMealViewHolder> {

    private final List<RecipeMeal> recipes;

    private RecipeMealAdapter(List<RecipeMeal> recipes) {
      this.recipes = recipes;
    }

    @Override
    public RecipeMealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new RecipeMealViewHolder(
              LayoutInflater.from(parent.getContext())
                      .inflate(R.layout.item_recipe_meal, parent, false)
      );
    }

    @Override
    public void onBindViewHolder(RecipeMealViewHolder holder, int position) {
      holder.render(recipes.get(position));
    }

    @Override
    public int getItemCount() {
      return recipes.size();
    }
  }

  static class RecipeMealViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.recipe_meal_name)
    TextView nameTextView;

    public RecipeMealViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }

    public void render(RecipeMeal recipeMeal) {
      nameTextView.setText(recipeMeal.name());
    }
  }
}
